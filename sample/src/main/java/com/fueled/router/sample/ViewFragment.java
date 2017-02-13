package com.fueled.router.sample;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.fueled.router.sample.core.AbstractFragment;
import com.fueled.router.sample.databinding.FragmentViewBinding;

/**
 * Created by hussein@fueled.com on 14/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class ViewFragment extends AbstractFragment {

    private FragmentViewBinding binding;

    private int navBarColor = -1;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view;
    }

    @Override
    protected void setupView(View view) {
        binding = DataBindingUtil.bind(view);
        navBarColor = getResources().getColor(R.color.colorAccent);
    }

    @Override
    public String getTitle() {
        return "View Fragment";
    }

    @Override
    public int getNavigationBarColor() {
        return navBarColor;
    }
}
