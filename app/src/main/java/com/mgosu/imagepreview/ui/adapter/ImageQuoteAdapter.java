package com.mgosu.imagepreview.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.data.api.ImageCategory.ListCategoryItem;
import com.mgosu.imagepreview.data.database.model.DataModel;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;
import com.mgosu.imagepreview.databinding.ItemCategoryBinding;
import com.mgosu.imagepreview.databinding.ItemListImageBinding;
import com.mgosu.imagepreview.databinding.ItemQuoteBinding;
import com.mgosu.imagepreview.interfaceCallback.OnActionItemClick;
import com.mgosu.imagepreview.ui.util.PreferencesUtils;

import java.util.List;

public class ImageQuoteAdapter extends RecyclerView.Adapter<ImageQuoteAdapter.ViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Context context;
    private List<ListCategoryItem> list;
    private OnActionItemClick actionItemClick;
    private PreferencesUtils utils;
    private int layout = 0;
    private List<String> stringList;
    private List<DataModel> listQuote;
    private List<FavoriteModel> listItemFromType;

    public void setListQuote(List<DataModel> listQuote) {
        this.listQuote = listQuote;
    }

    public void setListItemFromType(List<FavoriteModel> listItemFromType) {
        this.listItemFromType = listItemFromType;
    }

    public ImageQuoteAdapter(Context context, List<ListCategoryItem> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        utils = PreferencesUtils.getInstance(context);
    }

    public ImageQuoteAdapter(Context context, List<String> list) {
        this.context = context;
        this.stringList = list;
        this.layout = 2;
        utils = PreferencesUtils.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = null;
        switch (layout) {
            case 1:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_category, parent, false);
                break;
            case 2:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_list_image, parent, false);
                break;
            case 3:
                binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_quote, parent, false);
                break;
        }
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        switch (layout) {
            case 1:
                Glide.with(context).load(utils.getUrl() + list.get(position).getThumb()).placeholder(R.drawable.ic_load).into(holder.imageView);
                holder.setBinding(list.get(position), position);
                break;
            case 2:
                if (listItemFromType != null && !listItemFromType.isEmpty()) {
                    Glide.with(context).load(listItemFromType.get(position).getUrl()).placeholder(R.drawable.ic_load).into(holder.imageView);
                } else {
                    Glide.with(context).load(utils.getUrl() + stringList.get(position)).placeholder(R.drawable.ic_load).into(holder.imageView);
                }
                break;
            case 3:
                if (listItemFromType != null && !listItemFromType.isEmpty()) {
                    holder.setBindingQuote(listItemFromType.get(position));
                } else {
                    holder.setBindingQuote(listQuote.get(position));
                }
                if (position % 2 == 0)
                    holder.groupAll.setBackgroundColor(context.getResources().getColor(R.color.color_quote1));
                else
                    holder.groupAll.setBackgroundColor(context.getResources().getColor(R.color.color_quote2));


                break;
        }

        holder.groupAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionItemClick != null) {
                    actionItemClick.OnActionItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        switch (layout) {
            case 1:
                if (list != null && !list.isEmpty()) {
                    return list.size();
                } else {
                    return 0;
                }
            case 2:
                if (stringList != null && !stringList.isEmpty()) {
                    return stringList.size();
                } else {
                    if (listItemFromType != null && !listItemFromType.isEmpty()) {
                        return listItemFromType.size();
                    } else {
                        return 0;
                    }

                }
            case 3:
                if (listQuote != null && !listQuote.isEmpty()) {
                    return listQuote.size();
                } else {
                    if (listItemFromType != null && !listItemFromType.isEmpty()) {
                        return listItemFromType.size();
                    } else {
                        return 0;
                    }
                }
            default:
                return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ObservableField<String> nameGrammar = new ObservableField<>();
        public ObservableField<String> size = new ObservableField<>();
        private ItemCategoryBinding Binding;
        private RelativeLayout groupAll;
        private ImageView imageView;

        private ItemQuoteBinding quoteBinding;
        private ItemListImageBinding imageBinding;

        public ViewHolder(@NonNull ViewDataBinding itemView) {
            super(itemView.getRoot());
            switch (layout) {
                case 1:
                    this.Binding = (ItemCategoryBinding) itemView;
                    groupAll = Binding.layout;
                    imageView = Binding.ImageViewItem;
                    break;
                case 2:
                    this.imageBinding = (ItemListImageBinding) itemView;
                    groupAll = imageBinding.groupItem;
                    imageView = imageBinding.imageList;
                    break;
                case 3:
                    this.quoteBinding = (ItemQuoteBinding) itemView;
                    groupAll = quoteBinding.groupQuote;
                    break;

            }
        }

        public void setBindingQuote(DataModel model) {
            if (quoteBinding.getData() == null) {
                quoteBinding.setData(this);
            }
            String data = model.getData_value();
            nameGrammar.set(data);

        }

        public void setBindingQuote(FavoriteModel model) {
            if (quoteBinding.getData() == null) {
                quoteBinding.setData(this);
            }
            String data = model.getUrl();
            nameGrammar.set(data);

        }

        public void setBinding(ListCategoryItem grammar, int pos) {
            if (Binding.getViewHolder() == null) {
                Binding.setViewHolder(this);
            }
            String title = getAdapterPosition() + 1 + ", " + grammar.getTitle();
            nameGrammar.set(title);
            size.set(list.get(pos).getItems().size() + "\t" + "Images");
        }

    }

    public void setActionItemClick(OnActionItemClick actionItemClick) {
        this.actionItemClick = actionItemClick;
    }
}