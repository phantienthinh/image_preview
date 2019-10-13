package com.mgosu.imagepreview.ui.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mgosu.imagepreview.interfaceCallback.OnResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends AsyncTask<String, Integer, File> {

    private Context context;

    private OnResponse mOnResponse;

    public DownloadTask(Context context, OnResponse onResponse) {
        this.mOnResponse = onResponse;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mOnResponse != null) {
            mOnResponse.start();
        }
    }

    @Override
    protected File doInBackground(String... strings) {
        String urlPath = strings[0];
        int count;
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            File file = new File(RootPath.getInstance(context).getRootPath(), Constants.NAME_DB);
            FileOutputStream output = new FileOutputStream(file);
            FileDescriptor fd = output.getFD();

            byte data[] = new byte[1024];
            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress(+(int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return file;
        } catch (Exception e) {
            Log.e("TAG", "doInBackground: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (mOnResponse != null) {
            if (file != null) {
                mOnResponse.success(file);
            } else {
                mOnResponse.failed();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progressValue = values[0];
        if (mOnResponse != null) {
            mOnResponse.onProgressUpdate(progressValue);
        }
    }
}
