package com.mgosu.imagepreview.ui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.api.ImageCategory.ListCategoryItem;
import com.mgosu.imagepreview.databinding.ActivityListCategoryBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.adapter.ImageQuoteAdapter;
import com.mgosu.imagepreview.ui.util.ToastUtils;
import com.mgosu.imagepreview.ui.util.Utils;
import com.mgosu.imagepreview.ui.viewmodel.ListCategoryViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_LIST_DATA;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_POS;

public class ListCategory extends AppCompatActivity implements OnActionItemClick {
    private ActivityListCategoryBinding binding;
    private ImageQuoteAdapter adapter;
    private ListCategoryViewModel viewModel;
    private MutableLiveData<List<ListCategoryItem>> liveData;
    private List<ListCategoryItem> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_category);
        setSupportActionBar(binding.toolbarAudio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new ImageQuoteAdapter(this, list,1);
        adapter.setActionItemClick(this);
        binding.recyclerViewAudio.setHasFixedSize(true);
        binding.recyclerViewAudio.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerViewAudio.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(ListCategoryViewModel.class);
        viewModel.setContext(this);
        viewModel.setBinding(binding);
        liveData = viewModel.getLiveData();
        liveData.observe(this, new Observer<List<ListCategoryItem>>() {
            @Override
            public void onChanged(List<ListCategoryItem> itemList) {
                list.addAll(itemList);
                adapter.notifyDataSetChanged();
            }
        });
        binding.setCategory(viewModel);
    }

    @Override
    public void OnActionItemClick(int i) {
        if (Utils.isNetworkConnected(this)){
            Intent intent = new Intent(this,ListImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(KEY_LIST_DATA, (ArrayList<? extends Parcelable>) list);
            bundle.putInt(KEY_POS,i);
            intent.putExtras(bundle);
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
