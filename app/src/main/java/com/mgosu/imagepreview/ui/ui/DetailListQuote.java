package com.mgosu.imagepreview.ui.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.roomdatabase.RoomDb;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;
import com.mgosu.imagepreview.databinding.ActivityDetailListQuoteBinding;
import com.mgosu.imagepreview.ui.util.Utils;
import com.varunest.sparkbutton.SparkEventListener;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_TYPE;
import static com.mgosu.imagepreview.ui.util.Constants.TYPE_TEXT;

//import com.like.LikeButton;
//import com.like.OnLikeListener;

public class DetailListQuote extends AppCompatActivity {
    private ActivityDetailListQuoteBinding binding;
    private String stringQuote;
    private Dialog dialog;

    private RoomDb roomDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_list_quote);
        setSupportActionBar(binding.toolbarAudio);
        roomDb = RoomDb.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        if (intent != null) {
            stringQuote = intent.getStringExtra(KEY_TYPE);
            if (stringQuote != null && !stringQuote.isEmpty()) {
                binding.tvQuote.setText(stringQuote);
            }
        }
        crateDialog();
        checkItemDB();
        binding.setQuote(this);
        binding.starButton2.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    roomDb.favoriteDao().insertItemModel(new FavoriteModel(stringQuote, TYPE_TEXT, null));
                } else {
                    roomDb.favoriteDao().deleteByUrl(stringQuote);
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
//                roomDb.favoriteDao().insertItemModel(new FavoriteModel(stringQuote, TYPE_TEXT,null));
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                roomDb.favoriteDao().deleteByUrl(stringQuote);
//            }
//        });
    }

    private void checkItemDB() {
        FavoriteModel itemFromUrl = roomDb.favoriteDao().getItemFromUrl(stringQuote);
        if (itemFromUrl != null) {
            binding.starButton2.setChecked(true);
//            binding.starButton.setLiked(true);
        } else {
            binding.starButton2.setChecked(false);
//            binding.starButton.setLiked(false);
        }
    }

    private void crateDialog() {
        dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_copy_success);

    }

    public void onClickCopy(View view) {
        Utils.setTextToClipBoard(this, stringQuote);
        dialog.show();
    }

    public void onClickShare(View view) {
        Utils.shareText(this, stringQuote);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
