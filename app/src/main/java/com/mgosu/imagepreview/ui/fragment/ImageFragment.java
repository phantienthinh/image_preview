package com.mgosu.imagepreview.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.roomdatabase.RoomDb;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;
import com.mgosu.imagepreview.databinding.FragmentImageBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.adapter.ImageQuoteAdapter;
import com.mgosu.imagepreview.ui.ui.DetailImageActivity;
import com.mgosu.imagepreview.ui.util.PreferencesUtils;

import java.util.Collections;
import java.util.List;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_LIST_DATA;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_POS;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TITLE;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TYPE;
import static com.mgosu.imagepreview.ui.util.Constants.TYPE_IMAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements OnActionItemClick {
    private RoomDb roomDb;
    private FragmentImageBinding binding;
    private List<FavoriteModel> listItemFromType;
    private ImageQuoteAdapter adapter;
    private PreferencesUtils utils;

    public ImageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image, container, false);
        View view = binding.getRoot();
        roomDb = RoomDb.getInstance(getContext());
        utils = PreferencesUtils.getInstance(getContext());
        getData();

        return view;
    }

    private void getData() {
        listItemFromType = roomDb.favoriteDao().getItemFromType(TYPE_IMAGE);
        if (listItemFromType != null && !listItemFromType.isEmpty()) {
            adapter = new ImageQuoteAdapter(getContext(), null, 2);
            Collections.sort(listItemFromType, FavoriteModel.ID);
            adapter.setListItemFromType(listItemFromType);
            binding.recyclerViewImage.setHasFixedSize(true);
            binding.recyclerViewImage.setLayoutManager(new GridLayoutManager(getContext(), 3));
            binding.recyclerViewImage.setAdapter(adapter);
            adapter.setActionItemClick(this);
        } else {
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnActionItemClick(int i) {
        Intent intent = new Intent(getContext(), DetailImageActivity.class);
        intent.putExtra(KEY_TYPE, true);
        intent.putExtra(KEY_TITLE, listItemFromType.get(i).getTitle());
        intent.putExtra(KEY_POS, i);
        intent.putExtra(KEY_LIST_DATA, listItemFromType.get(i));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        listItemFromType = roomDb.favoriteDao().getItemFromType(TYPE_IMAGE);
        if (listItemFromType != null && !listItemFromType.isEmpty()) {
            Collections.sort(listItemFromType, FavoriteModel.ID);
            adapter.setListItemFromType(listItemFromType);
            adapter.notifyDataSetChanged();
        } else {
            if (adapter!=null){
                adapter.setListItemFromType(listItemFromType);
                adapter.notifyDataSetChanged();
            }

            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }

}
