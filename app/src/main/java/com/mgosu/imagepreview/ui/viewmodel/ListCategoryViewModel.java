package com.mgosu.imagepreview.ui.viewmodel;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mgosu.imagepreview.data.api.ImageCategory.DataCategoryImage;
import com.mgosu.imagepreview.data.api.ImageCategory.ListCategoryItem;
import com.mgosu.imagepreview.data.api.ImageCategory.ListDataCategory;
import com.mgosu.imagepreview.data.api.network.ApiClient;
import com.mgosu.imagepreview.databinding.ActivityListCategoryBinding;
import com.mgosu.imagepreview.ui.util.Constants;
import com.mgosu.imagepreview.ui.util.PreferencesUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mgosu.imagepreview.ui.util.Constants.STATE_LOADING;

public class ListCategoryViewModel extends ViewModel {
    private ActivityListCategoryBinding binding;
    private MutableLiveData<List<ListCategoryItem>> liveData;
    private List<ListCategoryItem> list;
    private PreferencesUtils preferencesUtils;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBinding(ActivityListCategoryBinding binding) {
        this.binding = binding;
    }

    public MutableLiveData<List<ListCategoryItem>> getLiveData() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            createData();
        }
        return liveData;
    }

    private void createData() {
        loadData(STATE_LOADING);
    }

    private void loadData(int state) {
        preferencesUtils = PreferencesUtils.getInstance(context);
        binding.setLoadingState(state);
        Call<DataCategoryImage> callApi = ApiClient.getsInstance().getClient().getImageCategory("images");
        callApi.enqueue(new Callback<DataCategoryImage>() {
            @Override
            public void onResponse(Call<DataCategoryImage> call, Response<DataCategoryImage> response) {
                DataCategoryImage body = response.body();
                if (body != null) {
                    ListDataCategory data = body.getData();
                    if (data != null) {
                        preferencesUtils.setUrl(data.getSubUrl());
                        list = data.getList();
                        liveData.setValue(ListCategoryViewModel.this.list);
                    }

                } else {
                    binding.setLoadingState(Constants.STATE_FAILED);
                }
                if (list.size() == 0) {
                    binding.setLoadingState(Constants.STATE_FAILED);
                } else {
                    binding.setLoadingState(Constants.STATE_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<DataCategoryImage> call, Throwable t) {
                binding.setLoadingState(Constants.STATE_FAILED);
            }
        });

    }

    public void onClickButtonReload(View view) {
        loadData(STATE_LOADING);
    }
}
