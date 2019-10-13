package com.mgosu.imagepreview.ui.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.databinding.ActivityFavoriteBinding;
import com.mgosu.imagepreview.ui.fragment.ImageFragment;
import com.mgosu.imagepreview.ui.fragment.ListQuoteFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

public class FavoriteActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ActivityFavoriteBinding binding;
    private FragmentPagerItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.rlImage.setBackgroundColor(getResources().getColor(R.color.color_tab));
        binding.tvImage.setTextColor(getResources().getColor(R.color.white));

        FavoriterAdapter adapter = new FavoriterAdapter(getSupportFragmentManager(), 2);
        binding.ViewPager.setAdapter(adapter);
        binding.ViewPager.addOnPageChangeListener(this);
        binding.setEventClick(this);

    }

    public void onClickImage(View view) {
        binding.ViewPager.setCurrentItem(0);
    }

    private void selectImage() {
        binding.tvImage.setTextColor(getResources().getColor(R.color.white));
        binding.tvList.setTextColor(getResources().getColor(R.color.color_black));
        binding.rlImage.setBackgroundColor(getResources().getColor(R.color.color_tab));
        binding.rlList.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void onClickListQuote(View view) {
        binding.ViewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                selectImage();
                break;
            case 1:
                selectList();
                break;

        }
    }

    private void selectList() {
        binding.tvList.setTextColor(getResources().getColor(R.color.white));
        binding.tvImage.setTextColor(getResources().getColor(R.color.color_black));
        binding.rlImage.setBackgroundColor(getResources().getColor(R.color.white));
        binding.rlList.setBackgroundColor(getResources().getColor(R.color.color_tab));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class FavoriterAdapter extends FragmentPagerAdapter {
        private int numberTab;

        public FavoriterAdapter(@NonNull FragmentManager fm, int numberTab) {
            super(fm);
            this.numberTab = numberTab;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ImageFragment();
                case 1:
                    return new ListQuoteFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return numberTab;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
