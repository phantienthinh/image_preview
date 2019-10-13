package com.mgosu.imagepreview.interfaceCallback;

import java.io.File;

public interface OnResponse {
    void start();

    void failed();

    void success(File file);

    void onProgressUpdate(int value);
}
