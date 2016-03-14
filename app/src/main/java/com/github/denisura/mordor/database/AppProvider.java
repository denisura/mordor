package com.github.denisura.mordor.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyDelete;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.NotifyUpdate;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = AppContract.CONTENT_AUTHORITY,
        database = AppDatabase.class,
        packageName = AppContract.PROVIDER_NAMESPACE)
public class AppProvider {

    static final String LOG_TAG = AppProvider.class.getCanonicalName();


    private AppProvider() {
    }

    static final Uri BASE_CONTENT_URI = AppContract.BASE_CONTENT_URI;

    interface Path {
        String PROFILES = AppContract.PATH_PROFILES;
        String HISTORY = AppContract.PATH_HISTORY;
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = AppContract.ProfileEntry.TABLE_NAME)
    public static class Profiles {

        @ContentUri(
                path = Path.PROFILES,
                type = AppContract.ProfileEntry.CONTENT_TYPE,
                defaultSort = ProfileColumns._ID + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.PROFILES);

        @InexactContentUri(
                path = Path.PROFILES + "/#",
                name = "PROFILE_ID",
                type = AppContract.ProfileEntry.CONTENT_ITEM_TYPE,
                whereColumn = AppContract.ProfileEntry._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.PROFILES, String.valueOf(id));
        }

        @NotifyInsert(paths = Path.PROFILES)
        public static Uri[] onInsert(ContentValues values) {
            return new Uri[]{
                    AppProvider.Profiles.CONTENT_URI
            };
        }

        @NotifyUpdate(paths = Path.PROFILES + "/#")
        public static Uri[] onUpdate(Context context, Uri uri, String where, String[] whereArgs) {
            final long profileId = Long.valueOf(uri.getPathSegments().get(1));
            return new Uri[]{
                    Profiles.withId(profileId), AppProvider.Profiles.CONTENT_URI
            };
        }

        @NotifyDelete(paths = Path.PROFILES + "/#")
        public static Uri[] onDelete(Context context, Uri uri) {
            final long profileId = Long.valueOf(uri.getPathSegments().get(1));
            return new Uri[]{
                    Profiles.withId(profileId), AppProvider.Profiles.CONTENT_URI
            };
        }

        public static long getProfileIdFromUri(Uri uri) {
            return Long.valueOf(uri.getPathSegments().get(1));
        }
    }


    @TableEndpoint(table = AppContract.HistoryEntry.TABLE_NAME)
    public static class History {

        @InexactContentUri(
                path = Path.HISTORY + "/#",
                name = "HISTORY_PROFILE_ID",
                type = AppContract.HistoryEntry.CONTENT_ITEM_TYPE,
                whereColumn = AppContract.HistoryEntry.PROFILE_ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.HISTORY, String.valueOf(id));
        }
    }

}