package com.fueled.flowr.sample;

import android.view.View;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.sample.core.AbstractFragment;

/**
 * Created by hussein@fueled.com on 18/05/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class CategoriesFragment extends AbstractFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_categories;
    }

    @Override
    protected void setupView(View view) {

    }

    @Override
    public String getTitle() {
        return "Categories Fragment";
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }
}
