package com.github.denisura.mordor.database;

import android.content.ContentResolver;
import android.net.Uri;

public class AppContract {

    public static final String CONTENT_AUTHORITY = "com.github.denisura.mordor";
    public static final String PROVIDER_NAMESPACE = "com.github.denisura.mordor.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PROFILES = "profiles";
    public static final String PATH_HISTORY = "history";


    public static final class ProfileEntry implements ProfileColumns {

        public static final String TABLE_NAME = "profiles";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROFILES;

    }

    public static final class HistoryEntry implements HistoryColumns {

        public static final String TABLE_NAME = "history";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HISTORY;

    }
}
