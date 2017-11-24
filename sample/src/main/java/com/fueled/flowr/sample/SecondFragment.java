package com.fueled.flowr.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.fueled.flowr.Flowr;
import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.annotations.DeepLink;
import com.fueled.flowr.sample.core.AbstractFragment;

import static com.fueled.flowr.sample.core.FragmentResultPublisherImpl.backStackIdentifier;

/**
 * Created by hussein@fueled.com on 18/05/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */

@DeepLink(value = "/second")
public class SecondFragment extends AbstractFragment implements View.OnClickListener {

    public static final String RESULT_FROM_SECOND = "RESULT_FROM_SECOND";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_stack2;
    }

    @Override
    protected void setupView(View view) {
        view.findViewById(R.id.return_home_button).setOnClickListener(this);
    }

    @Override
    public String getTitle() {
        return "Second Fragment";
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }

    @Override
    public void onClick(View view) {
        getFlowr().closeUptoWithResults(
                Flowr.getResultsResponse(getArguments(), Activity.RESULT_OK, getBundle()),
                backStackIdentifier);
    }

    @NonNull
    private Bundle getBundle() {
        Bundle args = new Bundle();
        args.putString(RESULT_FROM_SECOND, "Wow! Whata Stack!");
        return args;
    }
}
