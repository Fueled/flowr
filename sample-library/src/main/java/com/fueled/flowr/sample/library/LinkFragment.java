package com.fueled.flowr.sample.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fueled.flowr.AbstractFlowrFragment;
import com.fueled.flowr.annotations.DeepLink;

/**
 * Created by hussein@fueled.com on 05/06/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
@DeepLink("/hello")
public class LinkFragment extends AbstractFlowrFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_link, container, false);
    }

    @Override
    public String getTitle() {
        return "Sample Library Link Fragment";
    }
}
