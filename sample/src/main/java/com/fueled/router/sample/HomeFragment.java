package com.fueled.router.sample;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.fueled.router.NavigationIconType;
import com.fueled.router.sample.core.AbstractFragment;
import com.fueled.router.sample.databinding.FragmentHomeBinding;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class HomeFragment extends AbstractFragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupView(View view) {
        binding = DataBindingUtil.bind(view);
        binding.homeOpenViewButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationIconClick() {
        return true;
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HIDDEN;
    }

    @Override
    public void onClick(View view) {
        displayViewFragment();
    }

    private void displayViewFragment() {
        getRouter().open(ViewFragment.class)
                .displayFragment();
    }
}
