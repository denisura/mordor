package com.github.denisura.mordor.data.network;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.github.denisura.mordor.BuildConfig;
import com.github.denisura.mordor.data.database.AppContract;
import com.github.denisura.mordor.data.database.AppProvider;
import com.github.denisura.mordor.data.database.HistoryColumns;
import com.github.denisura.mordor.data.database.ProfileColumns;
import com.github.denisura.mordor.data.model.History;
import com.github.denisura.mordor.data.model.ProfileModel;
import com.github.denisura.mordor.data.model.Sample;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static com.github.denisura.mordor.utils.Utilities.updateWidgets;

public class RateHistoryDataAPI {

    private final String LOG_TAG = RateHistoryDataAPI.class.getSimpleName();

    public static final String BASE_URL = "https://mortgageapi.zillow.com";

    final String QUERY_PARTNER_ID = "partnerId";
    final String QUERY_STATE = "queries.local.stateAbbreviation";
    final String QUERY_IS_REFINANCE = "queries.local.refinance";
    final String QUERY_PROGRAM = "queries.local.program";
    final String QUERY_LOAN_TO_VALUE = "queries.local.loanToValueBucket";
    final String QUERY_CREDIT_SCORE = "queries.local.creditScoreBucket";
    final String QUERY_TIMESPAN = "durationDays";
    final String QUERY_INCLUDE_CURRENT_RATE = "includeCurrentRate";


    private final Context mContext;
    private final EndpointInterface mApiService;


    public RateHistoryDataAPI(Context context) {
        mContext = context;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();

        // add logging as last interceptor
//        httpClient.interceptors().add(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        mApiService = retrofit.create(EndpointInterface.class);
    }


    public void fetchProfileHistory(final ProfileModel profileModel) {
        Map<String, String> options = new HashMap<>();

        options.put(QUERY_PARTNER_ID, BuildConfig.ZILLOW_PARTNER_ID);
        options.put(QUERY_STATE, profileModel.getLocation());
        options.put(QUERY_IS_REFINANCE, "false");
        options.put(QUERY_PROGRAM, profileModel.getProgram());
        options.put(QUERY_LOAN_TO_VALUE, profileModel.getLoanToValueBucket());
        options.put(QUERY_CREDIT_SCORE, profileModel.getCreditScoreBucket());
        options.put(QUERY_TIMESPAN, "7");
        options.put(QUERY_INCLUDE_CURRENT_RATE, "true");


        Call<History> call = mApiService.getRtaesHistory(options);
        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                int statusCode = response.code();

                if (statusCode != 200) {
                    //TODO set status
                    return;
                }

                Log.d(LOG_TAG, "statusCode #" + statusCode);

                History history = response.body();
                Log.d(LOG_TAG, "Count #" + history.getRates().getLocal().getSamples().size());


                ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();


                //delete all history records for this profile
                Uri thisProfileHistoryUri = AppProvider.History.withId(profileModel.getId());

                batch.add(ContentProviderOperation.newDelete(thisProfileHistoryUri).build());


                //bulk insert new fetched records
                ContentProviderOperation.Builder builder;
                for (Sample sample : history.getRates().getLocal().getSamples()) {
                    try {
                        builder = ContentProviderOperation.newInsert(thisProfileHistoryUri);
                        builder.withValue(HistoryColumns.RATE, sample.getRate())
                                .withValue(HistoryColumns.TIME, sample.getTimeMIllies())
                                .withValue(HistoryColumns.PROFILE_ID, profileModel.getId());
                        batch.add(builder.build());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Uri thisProfileUri = AppProvider.Profiles.withId(profileModel.getId());

                //define trend
                //float oldCurrentRate = profileModel.getCurrentRate();
                if (history.getRates().getLocal().getSamples().size() > 0) {
                    float oldCurrentRate = history.getRates().getLocal().getSamples().get(100).getRate();
                    float currentRate = history.getRates().getLocal().getCurrentRate().getRate();

                    Log.d(LOG_TAG, "Current rate " + currentRate);
                    if (currentRate == 0) {
                        currentRate = history.getRates().getLocal().getSamples().get(0).getRate();
                        Log.d(LOG_TAG, "Current rate from samples" + currentRate);
                    }

                    String trend = ProfileColumns.TREND_SAME;

                    if (currentRate > oldCurrentRate) {
                        trend = ProfileColumns.TREND_UP;
                    } else if (currentRate < oldCurrentRate) {
                        trend = ProfileColumns.TREND_DOWN;
                    }

                    //update profile with new trend and current rate
                    builder = ContentProviderOperation.newUpdate(thisProfileUri);
                    builder.withValue(ProfileColumns.TREND, trend)
                            .withValue(ProfileColumns.CURRENT_RATE, currentRate);
                    batch.add(builder.build());
                }

                Log.d(LOG_TAG, "Applying " + batch.size() + " content provider operations.");
                try {
                    int operations = batch.size();
                    if (operations > 0) {
                        mContext.getContentResolver().applyBatch(AppContract.CONTENT_AUTHORITY, batch);
                    }
                    Log.d(LOG_TAG, "Successfully applied " + operations + " content provider operations.");
                } catch (RemoteException ex) {
                    Log.d(LOG_TAG, "RemoteException while applying content provider operations.");
                    throw new RuntimeException("Error executing content provider batch operation", ex);
                } catch (OperationApplicationException ex) {
                    Log.d(LOG_TAG, "OperationApplicationException while applying content provider operations.");
                    throw new RuntimeException("Error executing content provider batch operation", ex);
                }

                Log.d(LOG_TAG, "Notifying changes on all top-level paths on Content Resolver.");
                ContentResolver resolver = mContext.getContentResolver();
                // all profiles
                Uri uri = AppProvider.Profiles.CONTENT_URI;
                resolver.notifyChange(uri, null);
                // for profile id
                resolver.notifyChange(thisProfileUri, null);
                // for history profile id
                resolver.notifyChange(thisProfileHistoryUri, null);

                updateWidgets(mContext);

            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure", t);
            }
        });
    }

    /**
     * Retrofit Interface
     */
    public interface EndpointInterface {

        @GET("getRates")
        Call<History> getRtaesHistory(@QueryMap Map<String, String> options);

    }

}
