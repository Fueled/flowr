package com.fueled.flowr.sample;

import android.view.View;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.annotations.DeepLink;
import com.fueled.flowr.sample.core.AbstractFragment;

/**
 * Created by hussein@fueled.com on 18/05/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */

@DeepLink(value = {"/transition"})
public class TransitionFragment extends AbstractFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transition;
    }

    @Override
    protected void setupView(View view) {

    }

    @Override
    public String getTitle() {
        return "Transition Fragment";
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }
}
