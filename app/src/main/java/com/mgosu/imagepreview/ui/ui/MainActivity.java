package com.mgosu.imagepreview.ui.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.databinding.ActivityMainBinding;
import com.mgosu.imagepreview.ui.adapter.SliderAdapter;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.wiget.slide.SliderAnimations;
import com.mgosu.imagepreview.ui.wiget.slide.SliderView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private Integer[] list = {R.drawable.slide_image_1,R.drawable.slide_image_2,R.drawable.slide_image_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        final SliderAdapter adapter = new SliderAdapter(this, list);
        adapter.setCount(3);
        mainBinding.imageSlider.setSliderAdapter(adapter);
        mainBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
        mainBinding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        mainBinding.imageSlider.startAutoCycle();
        mainBinding.setClickEvent(this);
    }
    public void onclickButtonImage(View view){
        startActivity(new Intent(this,ListCategory.class));
    }
    public void onclickButtonText(View view){
        startActivity(new Intent(this,ListQuoteActivity.class));
    }
    public void onclickButtonFavorite(View view){
        startActivity(new Intent(this,FavoriteActivity.class));
    }
    public void onclickButtonGame(View view){
        ToastUtils.getInstance(this).showMessage(getString(R.string.coming_soon));
    }
}
