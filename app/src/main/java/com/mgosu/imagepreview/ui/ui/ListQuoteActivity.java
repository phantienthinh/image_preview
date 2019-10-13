package com.mgosu.imagepreview.ui.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.database.DBHelper;
import com.mgosu.imagepreview.data.database.model.CategoryModel;
import com.mgosu.imagepreview.databinding.ActivityListQuoteBinding;
import com.mgosu.imagepreview.ui.adapter.PagerAdapter;
import com.mgosu.imagepreview.ui.fragment.QuoteFragment;
import com.mgosu.imagepreview.ui.model.ItemFragment;
import com.mgosu.imagepreview.ui.util.RootPath;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListQuoteActivity extends AppCompatActivity {
    private ActivityListQuoteBinding binding;
    private String mPathFolder;
    public static String mPathDb;
    private DBHelper dbHelper;
    private List<CategoryModel> listCategory;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_quote);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPathFolder = RootPath.getInstance(this).getRootPath();

        File file2 = new File(mPathFolder);
        if (file2.exists()) {
            File[] files = file2.listFiles();
            for (File files1 : files) {
                if (files1.getName().endsWith(".db")) {
                    mPathDb = files1.getPath();
                }
            }
        }
        dbHelper = DBHelper.getInstance(this, mPathDb);
        dbHelper.createDataBase();
        listCategory = dbHelper.getAllCategory();
        createTablayout();
    }
    private void createTablayout() {
        ArrayList<ItemFragment> fragments = new ArrayList<>();
        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (CategoryModel model : listCategory) {
            fragments.add(new ItemFragment(model.getCategory_name(), QuoteFragment.newInstance(model.getCategory_id())));
        }
        PagerAdapter adapter = new PagerAdapter(
                getSupportFragmentManager(), pages, fragments);
        binding.ViewPager.setAdapter(adapter);
        binding.tabLayout.setViewPager(binding.ViewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
