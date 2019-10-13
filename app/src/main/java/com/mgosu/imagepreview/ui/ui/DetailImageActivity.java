package com.mgosu.imagepreview.ui.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.roomdatabase.RoomDb;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;
import com.mgosu.imagepreview.databinding.ActivityDetailImageBinding;
import com.mgosu.imagepreview.ui.util.PreferencesUtils;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.util.Utils;
import com.varunest.sparkbutton.SparkEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_LIST_DATA;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_POS;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_REQUEST;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_REQUEST_1;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_REQUEST_2;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TITLE;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TYPE;
import static com.mgosu.imagepreview.ui.util.Constants.TYPE_IMAGE;
import static com.mgosu.imagepreview.ui.util.Utils.createFolder;

//import com.like.LikeButton;
//import com.like.OnLikeListener;

public class DetailImageActivity extends AppCompatActivity {
    private ActivityDetailImageBinding binding;
    private ArrayList<String> listImage;
    private int intExtra;
    private PreferencesUtils preferencesUtils;
    private String url;
    private String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private Dialog dialog;
    private AlertDialog alertDialog;
    private FavoriteModel model, modelIntent;

    private RoomDb roomDb;
    private long id;
    private String title;
    private boolean typeFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_image);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        preferencesUtils = PreferencesUtils.getInstance(this);
        roomDb = RoomDb.getInstance(this);
        url = preferencesUtils.getUrl();
        alertDialog = new SpotsDialog.Builder().setContext(this).setMessage(getString(R.string.loading)).build();
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            title = intent.getStringExtra(KEY_TITLE);
            getSupportActionBar().setTitle(title);
            intExtra = intent.getIntExtra(KEY_POS, 0);
            typeFrom = intent.getBooleanExtra(KEY_TYPE, false);
            if (typeFrom) {
                modelIntent = (FavoriteModel) intent.getSerializableExtra(KEY_LIST_DATA);
                Glide.with(this).load(modelIntent.getUrl()).into(binding.imageDetail);
                binding.btBack.setVisibility(View.GONE);
                binding.btNext.setVisibility(View.GONE);
            } else {
                listImage = intent.getStringArrayListExtra(KEY_LIST_DATA);
                Glide.with(this).load(url + listImage.get(intExtra)).into(binding.imageDetail);
                if (intExtra != 0) {
                    binding.btBack.setVisibility(View.VISIBLE);
                } else {
                    binding.btBack.setVisibility(View.GONE);
                }
                if (intExtra == listImage.size() - 1) {
                    binding.btNext.setVisibility(View.GONE);
                }
            }

        }
        checkItemDataBase();
        binding.setEventClick(this);
        binding.starButton2.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    roomDb.favoriteDao().insertItemModel(model);
                } else {
                    if (!typeFrom)
                        roomDb.favoriteDao().deleteByUrl(url + listImage.get(intExtra));
                    else roomDb.favoriteDao().deleteByUrl(modelIntent.getUrl());
                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
//        binding.starButton.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//                roomDb.favoriteDao().insertItemModel(model);
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                if (!typeFrom)
//                    roomDb.favoriteDao().deleteByUrl(url + listImage.get(intExtra));
//                else roomDb.favoriteDao().deleteByUrl(modelIntent.getUrl());
//            }
//        });


    }

    private void checkItemDataBase() {
        FavoriteModel itemFromUrl;
        if (typeFrom) {
            itemFromUrl = roomDb.favoriteDao().getItemFromUrl(modelIntent.getUrl());
            if (itemFromUrl != null)
            model = new FavoriteModel(itemFromUrl.getUrl(), TYPE_IMAGE, title);
        } else {
            itemFromUrl = roomDb.favoriteDao().getItemFromUrl(url + listImage.get(intExtra));
//            if (itemFromUrl != null)
            model = new FavoriteModel(url + listImage.get(intExtra), TYPE_IMAGE, title);
        }
        if (itemFromUrl != null) {
            binding.starButton2.setChecked(true);
//            binding.starButton.setLiked(true);
        } else {
            binding.starButton2.setChecked(false);
//            binding.starButton.setLiked(false);
        }


    }


    public void onClickBack(View view) {
        if (intExtra > 1) {
            intExtra--;
            loadImage();
            binding.btBack.setVisibility(View.VISIBLE);
            if (intExtra <= listImage.size() - 2) {
                binding.btNext.setVisibility(View.VISIBLE);
            }
        } else if (intExtra == 1) {
            intExtra--;
            loadImage();
            binding.btBack.setVisibility(View.GONE);
        }
        checkItemDataBase();
    }

    public void onClickNext(View view) {
        if (intExtra < listImage.size() - 2) {
            intExtra++;
            loadImage();
            binding.btNext.setVisibility(View.VISIBLE);
            if (intExtra >= 1) {
                binding.btBack.setVisibility(View.VISIBLE);
            }
        } else if (intExtra == listImage.size() - 2) {
            intExtra++;
            loadImage();
            binding.btNext.setVisibility(View.GONE);
        }
        checkItemDataBase();
    }

    private void loadImage() {
        Glide.with(this).load(url + listImage.get(intExtra)).into(binding.imageDetail);
    }

    public void onClickSave(View view) {
        if (Utils.hasPermissions(this, permission)) {
            showAlertDialog();
        } else {
            Utils.permission(this, permission, KEY_REQUEST_2);
        }
    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to download ?");
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
                dialogInterface.dismiss();
                new AsyncSaveImage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public class AsyncSaveImage extends AsyncTask<Void, Void, File> {

        private String urlPath;
        private int count;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected File doInBackground(Void... voids) {
            String folder = createFolder(Environment.getExternalStorageDirectory() + File.separator + "Image Save");
            String fname = null;
            if (typeFrom) {
                urlPath = modelIntent.getUrl();
                if (urlPath.contains(url)) {
                    String replace = urlPath.replace(url, "");
                    if (replace.contains("/")) {
                        fname = replace.replace("/", "");
                    }
                }
            } else {
                fname = listImage.get(intExtra);
                if (fname.contains("/")) {
                    fname = fname.replace("/", "");
                }
                urlPath = url + listImage.get(intExtra);
            }
            try {
                URL url = new URL(urlPath);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                File file = new File(folder, fname);
                FileOutputStream output = new FileOutputStream(file);
                FileDescriptor fd = output.getFD();

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return file;
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (file != null) {
                alertDialog.dismiss();
                ToastUtils.getInstance(DetailImageActivity.this).showMessage("Save Image Success");
            } else {
                ToastUtils.getInstance(DetailImageActivity.this).showMessage("DownLoad Fail");
                alertDialog.dismiss();
            }

        }
    }

    public void onClickShare(View view) {
        if (Utils.hasPermissions(this, permission)) {
            shareImage();
        } else {
            Utils.permission(this, permission, KEY_REQUEST);
        }

    }

    public void onClickSetWallpaper(View view) {
        if (Utils.hasPermissions(this, permission)) {
            cropImage();
        } else {
            Utils.permission(this, permission, KEY_REQUEST_1);
        }
    }

    private void cropImage() {
        binding.imageDetail.buildDrawingCache();
        Bitmap bmap = binding.imageDetail.getDrawingCache();
        sendBitmap(bmap);
    }

    private void sendBitmap(Bitmap bmp) {
        try {
            Log.e("zzzz", "sendBitmap: ");
            String filename = "bitmap.png";
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            bmp.recycle();
            Intent in1 = new Intent(this, CropActivity.class);
            in1.putExtra("bitmap", filename);
            startActivity(in1);
            binding.imageDetail.setDrawingCacheEnabled(false);
            binding.imageDetail.destroyDrawingCache();
        } catch (Exception e) {
            Log.e("zzzz", "sendBitmap: " + e.getMessage());
        }
    }

    private void shareImage() {
        binding.imageDetail.destroyDrawingCache();
        binding.imageDetail.buildDrawingCache();
        Bitmap bmap = binding.imageDetail.getDrawingCache();
        Utils.shareImage(this, bmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == KEY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareImage();
            }
        }
        if (requestCode == KEY_REQUEST_1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cropImage();
            }
        }
        if (requestCode == KEY_REQUEST_2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAlertDialog();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkItemDataBase();
    }
}
