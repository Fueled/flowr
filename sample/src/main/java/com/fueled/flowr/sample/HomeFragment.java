package com.fueled.flowr.sample;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.sample.core.AbstractFragment;
import com.fueled.flowr.sample.databinding.FragmentHomeBinding;

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
        binding.homeOpenLinkButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationIconClick() {
        return true;
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_open_view_button) {
            displayViewFragment();
        } else {
            displayLinkFragment();
        }
    }

    private void displayViewFragment() {
        getFlowr().open("/m/From%20HomeFragment")
                .setCustomTransactionAnimation(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .displayFragment();
    }

    private void displayLinkFragment() {
        getFlowr().open("/hello")
                .displayFragment();
    }
}
