package com.fueled.flowr.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.fueled.flowr.annotations.DeepLink;
import com.fueled.flowr.sample.core.AbstractFragment;
import com.fueled.flowr.sample.databinding.FragmentViewBinding;

/**
 * Created by hussein@fueled.com on 14/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
@DeepLink(value = {"/m/{message}", "/test2"})
public class ViewFragment extends AbstractFragment implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private FragmentViewBinding binding;

    private int navBarColor = -1;
    private boolean isToolbarVisible = true;
    private boolean isDrawerEnabled = true;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view;
    }

    @Override
    protected void setupView(View view) {
        binding = DataBindingUtil.bind(view);

        binding.setClickListener(this);
        binding.drawerSwitch.setOnCheckedChangeListener(this);
        binding.toolbarSwitch.setOnCheckedChangeListener(this);
        Bundle data = getArguments();
        if (data != null && data.containsKey("message")) {
            binding.setMessage(data.getString("message"));
        } else {
            binding.setMessage("View Fragment");
        }
    }

    @Override
    public String getTitle() {
        return "View Fragment";
    }

    @Override
    public int getNavigationBarColor() {
        return navBarColor;
    }

    @Override
    public boolean isToolbarVisible() {
        return isToolbarVisible;
    }

    @Override
    public boolean isDrawerEnabled() {
        return isDrawerEnabled;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_color_deep_purple:
                setNavColor(R.color.deep_purple);
                break;
            case R.id.nav_color_blue:
                setNavColor(R.color.blue);
                break;
            case R.id.nav_color_teal:
                setNavColor(R.color.teal);
                break;
            case R.id.nav_color_red:
                setNavColor(R.color.red);
                break;
            default:
                break;
        }

        getFlowr().syncScreenState();
    }

    private void setNavColor(@ColorRes int colorResId) {
        navBarColor = ContextCompat.getColor(getContext(), colorResId);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {
            case R.id.toolbar_switch:
                isToolbarVisible = checked;
                break;
            case R.id.drawer_switch:
                isDrawerEnabled = checked;
                break;
            default:
                break;
        }

        getFlowr().syncScreenState();
    }
}
