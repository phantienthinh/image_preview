package com.mgosu.imagepreview.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.roomdatabase.RoomDb;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;
import com.mgosu.imagepreview.databinding.FragmentListQuoteBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.adapter.ImageQuoteAdapter;
import com.mgosu.imagepreview.ui.ui.DetailListQuote;

import java.util.Collections;
import java.util.List;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_TYPE;
import static com.mgosu.imagepreview.ui.util.Constants.TYPE_TEXT;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListQuoteFragment extends Fragment implements OnActionItemClick {
    private RoomDb roomDb;
    private FragmentListQuoteBinding binding;
    private List<FavoriteModel> listItemFromType;
    private ImageQuoteAdapter adapter;

    public ListQuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_quote, container, false);
        View view = binding.getRoot();
        roomDb = RoomDb.getInstance(getContext());
        getData();
        return view;
    }

    private void getData() {
        listItemFromType = roomDb.favoriteDao().getItemFromType(TYPE_TEXT);
        if (listItemFromType != null && !listItemFromType.isEmpty()) {
            adapter = new ImageQuoteAdapter(getContext(), null, 3);
            Collections.sort(listItemFromType, FavoriteModel.ID);
            adapter.setListItemFromType(listItemFromType);
            binding.recyclerViewListQuote.setHasFixedSize(true);
            binding.recyclerViewListQuote.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.recyclerViewListQuote.setAdapter(adapter);
            adapter.setActionItemClick(this);
        } else {
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void OnActionItemClick(int i) {
        Intent intent = new Intent(getContext(), DetailListQuote.class);
        intent.putExtra(KEY_TYPE, listItemFromType.get(i).getUrl());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        listItemFromType = roomDb.favoriteDao().getItemFromType(TYPE_TEXT);
        if (listItemFromType != null && !listItemFromType.isEmpty()) {
            Collections.sort(listItemFromType, FavoriteModel.ID);
            adapter.setListItemFromType(listItemFromType);
            adapter.notifyDataSetChanged();
        } else {
            if (adapter != null) {
                adapter.setListItemFromType(listItemFromType);
                adapter.notifyDataSetChanged();
            }
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }

    }
}
