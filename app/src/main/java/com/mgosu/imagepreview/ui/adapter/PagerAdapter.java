package com.mgosu.imagepreview.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mgosu.imagepreview.data.database.model.CategoryModel;
import com.mgosu.imagepreview.ui.fragment.QuoteFragment;
import com.mgosu.imagepreview.ui.model.ItemFragment;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.List;

public class PagerAdapter extends FragmentPagerItemAdapter {
    private List<ItemFragment> list;

    public PagerAdapter(FragmentManager fm, FragmentPagerItems pages, List<ItemFragment> list) {
        super(fm, pages);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getTitle();
    }

}
