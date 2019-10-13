package com.mgosu.imagepreview.ui.model;

import com.mgosu.imagepreview.ui.fragment.QuoteFragment;

public class ItemFragment {
    private String title;
    private QuoteFragment fragment;

    public ItemFragment(String title, QuoteFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuoteFragment getFragment() {
        return fragment;
    }

    public void setFragment(QuoteFragment fragment) {
        this.fragment = fragment;
    }
}
