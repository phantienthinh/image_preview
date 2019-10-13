package com.mgosu.imagepreview.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.api.database.DataAll;
import com.mgosu.imagepreview.data.api.database.DataBaseModel;
import com.mgosu.imagepreview.data.api.network.ApiClient;
import com.mgosu.imagepreview.databinding.ActivitySplashBinding;
import com.mgosu.imagepreview.interfaceCallback.OnResponse;
import com.mgosu.imagepreview.ui.util.DownloadTask;
import com.mgosu.imagepreview.ui.util.PreferencesUtils;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.util.Utils;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mgosu.imagepreview.ui.util.Constants.STATE_FAILED;
import static com.mgosu.imagepreview.ui.util.Constants.STATE_LOADING;
import static com.mgosu.imagepreview.ui.util.Constants.STATE_SUCCESS;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private PreferencesUtils preferencesUtils;
    private OnResponse onResponse;
    private String file_url;
    private int mVersion;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.setLoadingState(STATE_LOADING);
        preferencesUtils = PreferencesUtils.getInstance(this);
        boolean b = Utils.isNetworkConnected(this);
        binding.setEvent(this);
        if (b) {
            getDataApi();
        } else {
            if (preferencesUtils.getVersion() == 0) {
                binding.setLoadingState(STATE_FAILED);
                ToastUtils.getInstance(this).showMessage(getResources().getString(R.string.download_network));
//                finish();
            } else {
                binding.setLoadingState(STATE_SUCCESS);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void getDataApi() {
        binding.setLoadingState(STATE_LOADING);
        Call<DataAll> callApi = ApiClient.getsInstance().getClient().getDatabase("text");
        callApi.enqueue(new Callback<DataAll>() {
            @Override
            public void onResponse(Call<DataAll> call, Response<DataAll> response) {
                DataAll body = response.body();
                if (body != null) {
                    DataBaseModel data = body.getData();
                    file_url = data.getFile();
                    mVersion = data.getVer();
                    if (preferencesUtils.getVersion() < mVersion) {
                        downloadFile(file_url);
                    } else {
                        binding.setLoadingState(STATE_SUCCESS);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<DataAll> call, Throwable t) {
                ToastUtils.getInstance(SplashActivity.this).showMessage(getString(R.string.check_network));
                binding.setLoadingState(STATE_FAILED);
            }
        });
    }

    private void downloadFile(String file_url) {
//        pDialog = new Dialog(this);
//        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pDialog.setCancelable(false);
//        pDialog.setContentView(R.layout.dialog_progress);
//        Utils.clearBackground(pDialog);
//        initDialog();
        onResponse = new OnResponse() {
            @Override
            public void start() {
//                pDialog.show();
            }

            @Override
            public void failed() {
                binding.setLoadingState(STATE_FAILED);
            }

            @Override
            public void success(File file) {
//                Log.e(TAG, "success: ");
//                new UnzipTask(SplashActivity.this, response).execute();
                binding.setLoadingState(STATE_SUCCESS);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                preferencesUtils.setVersion(mVersion);
            }

            @Override
            public void onProgressUpdate(int value) {
//                seekBar.setProgress(value);
//                Log.e(TAG, "onProgressUpdate: "+value );
//                tvDialogSplash.setText("- " + value + "% -");

            }
        };
        new DownloadTask(this, onResponse).execute(file_url);
    }

    @Override
    public void onBackPressed() {
    }

    public void ClickRetry(View view) {
        getDataApi();
    }
}
