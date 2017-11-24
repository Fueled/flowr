package com.fueled.flowr.sample;

import android.view.View;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.annotations.DeepLink;
import com.fueled.flowr.sample.core.AbstractFragment;

import static com.fueled.flowr.sample.core.FragmentResultPublisherImpl.sourceFragmentId;

/**
 * Created by hussein@fueled.com on 18/05/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */

@DeepLink(value = "/first")
public class FirstFragment extends AbstractFragment implements View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void setupView(View view) {
        view.findViewById(R.id.add_stack_button).setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "First Fragment";
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }

    @Override
    public void onClick(View view) {
        getFlowr().open("/second")
                .displayFragmentForResults(sourceFragmentId, HomeFragment.RC_STACK);
    }
}
