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
import com.mgosu.imagepreview.data.database.DBHelper;
import com.mgosu.imagepreview.data.database.model.DataModel;
import com.mgosu.imagepreview.databinding.FragmentQuoteBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.adapter.ImageQuoteAdapter;
import com.mgosu.imagepreview.ui.ui.DetailListQuote;
import com.mgosu.imagepreview.ui.ui.ListQuoteActivity;

import java.util.List;

import static com.mgosu.imagepreview.ui.util.Constants.KEY_ID;
import static com.mgosu.imagepreview.ui.util.Constants.KEY_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteFragment extends Fragment implements OnActionItemClick {
    private FragmentQuoteBinding binding;
    private int categories_id;
    private DBHelper helper;
    private List<DataModel> dataFromId;
    private ImageQuoteAdapter adapter;

    public static QuoteFragment newInstance(int someInt) {
        QuoteFragment myFragment = new QuoteFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_ID, someInt);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quote, container, false);
        View view = binding.getRoot();
        getData();
        setData();
        return view;
    }

    private void setData() {
        adapter = new ImageQuoteAdapter(getContext(), null, 3);
        adapter.setListQuote(dataFromId);
        binding.recyclerQuote.setHasFixedSize(true);
        binding.recyclerQuote.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.recyclerQuote.setAdapter(adapter);
        adapter.setActionItemClick(this);
    }

    private void getData() {
        categories_id = getArguments().getInt(KEY_ID);
        helper = DBHelper.getInstance(getContext(), ListQuoteActivity.mPathDb);
        dataFromId = helper.getDataFromId(categories_id);
    }


    @Override
    public void OnActionItemClick(int i) {
        Intent intent = new Intent(getContext(), DetailListQuote.class);
        intent.putExtra(KEY_TYPE,dataFromId.get(i).getData_value());
        startActivity(intent);
    }
}
