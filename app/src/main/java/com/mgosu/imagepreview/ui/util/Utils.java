package com.mgosu.imagepreview.ui.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.mgosu.imagepreview.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.provider.MediaStore.Images.Media.insertImage;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_LABEL;
import static com.mgosu.imagepreview.ui.util.Constants.PATH_VIDEO_FOLDER;

public class Utils {
    @SuppressLint("DefaultLocale")
    public static String convertTime(int time) {
        String result;
        int hour = time / 3600;
        int remainSecond = time % 3600;
        int minutes = remainSecond / 60;
        int second = remainSecond % 60;
        result = String.format("%02d:%02d", minutes, second);
        return result;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] USER_AGENT = new String[]{
            "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 2.3; en-us) AppleWebKit/999+ (KHTML, like Gecko) Safari/999.9",
            "Mozilla/5.0 (Linux; U; Android 4.0.3; de-ch; HTC Sensation Build/IML74K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
            "Mozilla/5.0 (Linux; U; Android 2.3.5; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 2.3.4; en-us; T-Mobile myTouch 3G Slide Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 2.3.3; zh-tw; HTC Pyramid Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 2.3.3; en-us; HTC_DesireS_S510e Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 2.3.3; en-us; HTC_DesireS_S510e Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile"};

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static String convertTitle(String title) {
        if (title != null && !title.isEmpty()) {
            int i = title.indexOf("(");
            if (i == -1) {
                return title;
            } else {
                String substring = title.substring(i);
                if (!substring.isEmpty()) {
                    String replace = substring.replace("(", "");
                    return replace.replace(")", "");
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }


    public static void clearBackground(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static boolean hasDrawOverlayOtherApps(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return true;
        }
    }

    @SuppressLint("DefaultLocale")
    public static String convertTime(long time) {
        String result;
        result = String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
        return result;
    }

    public static String getJson(Context activity, int a) {
        String json = "";
        Resources resources = activity.getResources();
        InputStream inputStream = resources.openRawResource(a);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder builder = new StringBuilder();
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        json = builder.toString();

        return json;
    }

    public static void permissionOverLay(Activity context, int overRequest) {
        if (!Utils.hasDrawOverlayOtherApps(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            context.startActivityForResult(intent, overRequest);
        }
    }

    public static void permission(Context context, String[] PERMISSIONS, int REQUEST) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!hasPermissions(context, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST);
            } else {

            }
        } else {

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean setWallpaper(Context context, Bitmap bitmap) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
//        Bitmap bitmap = BitmapFactory.decodeFile(pathFile);
        try {
            myWallpaperManager.setBitmap(bitmap);
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    public static void setTextToClipBoard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(KEY_LABEL, text);
        clipboard.setPrimaryClip(clip);
    }

    public static void shareText(Context context,String shareBody){
        Log.e("11111", "shareText: "+shareBody );
        Intent sharingIntent = new Intent(ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(EXTRA_SUBJECT, "Share");
        sharingIntent.putExtra(EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent,context.getResources().getString(R.string.share_using)));
    }

    public static void shareImage(Context context, Bitmap bitmap) {
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File file = new File(pathSave);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpg");
        context.startActivity(Intent.createChooser(intent, "Choose in app"));
    }

    public static void createFolder() {
        File folder = new File(PATH_VIDEO_FOLDER);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }

    public static String createFolder(String path) {
        File folder = new File(path);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    public static int getRandom(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

//    public

    public static int getRandom(int max, int min) {
        Random r = new Random();
        int i1 = r.nextInt(max - min) + min;
        return i1;
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service :
                manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    public static boolean checkCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public static String getFileNameFromPath(String url) {
        if (!url.contains("/")) {
            return null;
        }
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    public static String getFileExtension(String fileName) {
        if (!fileName.contains(".")) {
            return fileName;
        }
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index + 1);
    }


    public static boolean unZip(String dirPath, String fileName) {
        try {
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(new FileInputStream(dirPath + "/" + fileName)));
            ZipEntry ze;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(dirPath, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                int read;
                try {
                    while ((read = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, read);
                    }
                } finally {
                    fout.close();
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return false;
    }
}