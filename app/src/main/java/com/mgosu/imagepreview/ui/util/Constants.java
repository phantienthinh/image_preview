package com.mgosu.imagepreview.ui.util;
import android.os.Environment;

import java.io.File;

public class Constants {
    public static final String GOOGLE_TRANSLATE_URL
            = "http://translate.google.com/m?hl=%s&sl=%s&tl=%s&ie=UTF-8&prev=_m&q=%s";

    public static final String FOLDER_NAME = "Call Splash";

    public static final String PATH_VIDEO_FOLDER = Environment.getExternalStorageDirectory() +
            File.separator + FOLDER_NAME;

    public static final int STATE_LOADING = 0;
    public static final int KEY_REQUEST = 232;
    public static final int KEY_REQUEST_2 = 233;
    public static final int KEY_REQUEST_1 = 231;
    public static final int STATE_FAILED = -1;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_LOADING_MORE = 2;
    public static final String KEY_POS = "key_pos";
    public static final String KEY_LIST_DATA = "key_list_data";
    public static final String KEY_DAILY = "key_daily";
    public static final String KEY_GRAMMAR = "key_grammar";
    public static final String KEY_TENSES = "key_tenses";
    public static final String KEY_SLANG = "key_slang";
    public static final String KEY_OBJ = "key_obj";

    public static final String KEY_TITLE = "key_title";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_ID = "key_id";
    public static final String KEY_LABEL = "KEY_LABEL";

    public static final String NAME_DB = "tex_db.db";
    public static final String TYPE_IMAGE = "IMAGE";
    public static final String TYPE_TEXT = "TEXT";
}
