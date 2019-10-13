package com.mgosu.imagepreview.ui.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.api.ImageCategory.ListCategoryItem;
import com.mgosu.imagepreview.databinding.ActivityListImageBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.adapter.ImageQuoteAdapter;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.util.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_LIST_DATA;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_POS;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TITLE;

public class ListImageActivity extends AppCompatActivity implements OnActionItemClick {
    private ActivityListImageBinding binding;
    private ArrayList<ListCategoryItem> list;
    private ImageQuoteAdapter adapter;
    private List<String> items;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_image);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            list = bundle.getParcelableArrayList(KEY_LIST_DATA);
            pos = bundle.getInt(KEY_POS, -1);
            getSupportActionBar().setTitle(list.get(pos).getTitle());
            items = list.get(pos).getItems();
            adapter = new ImageQuoteAdapter(this, items);
            binding.recyclerViewListImage.setHasFixedSize(true);
            binding.recyclerViewListImage.setLayoutManager(new GridLayoutManager(this, 3));
            binding.recyclerViewListImage.setAdapter(adapter);
            adapter.setActionItemClick(this);
        }
    }

    @Override
    public void OnActionItemClick(int i) {
        if (Utils.isNetworkConnected(this)){
            Intent intent = new Intent(this, DetailImageActivity.class);
            intent.putExtra(KEY_TITLE, list.get(pos).getTitle());
            intent.putStringArrayListExtra(KEY_LIST_DATA, (ArrayList<String>) items);
            intent.putExtra(KEY_POS, i);
            startActivity(intent);
        }else {
            ToastUtils.getInstance(this).showMessage(getString(R.string.access_internet));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
