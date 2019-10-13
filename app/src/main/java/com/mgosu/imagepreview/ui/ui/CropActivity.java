package com.mgosu.imagepreview.ui.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.isseiaoki.simplecropview.callback.CropCallback;
import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.databinding.ActivityCropBinding;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.mgosu.imagepreview.ui.util.Utils.createFolder;

public class CropActivity extends AppCompatActivity {
    private ActivityCropBinding binding;
    private Uri sourceUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            Bitmap bmp = null;
            String filename = getIntent().getStringExtra("bitmap");
            try {
                FileInputStream is = this.openFileInput(filename);
                bmp = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveBitmapFolder(bmp);
            binding.cropImageView.setImageBitmap(bmp);

        }
        binding.setEventClick(this);
    }

    private void saveBitmapFolder(Bitmap bitmap) {
        String folder = createFolder(Environment.getExternalStorageDirectory() + File.separator + "Temp");
        FileOutputStream fos = null;
        String pathSave = folder + "/" + "ImageTemp.jpg";
        try {
            fos = new FileOutputStream(pathSave);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }
        } catch (Exception e) {

        }
//        String bitmapPath = pathSave
//        sourceUri = Uri.fromFile(new File(pathSave));
        sourceUri = Uri.fromFile(new File(pathSave));
    }

    public void clickCropImage(View view) {
        showAlertDialog();
    }

    private void cropImage() {
        binding.cropImageView.crop(sourceUri).execute(new CropCallback() {
            @Override
            public void onSuccess(Bitmap cropped) {
                Utils.setWallpaper(CropActivity.this, reSizeBitmap(cropped));
                finish();
                ToastUtils.getInstance(CropActivity.this).showMessage("Set Wallpaper Sucess");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("aaaa", e.getMessage());
            }
        });
    }

    private Bitmap reSizeBitmap(Bitmap bm) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Bitmap bitmap = Bitmap.createScaledBitmap(bm, width, height, true);
        return bitmap;
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to set as wallpaper?");
        builder.setCancelable(false);
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cropImage();
                dialogInterface.dismiss();
//                finish();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
