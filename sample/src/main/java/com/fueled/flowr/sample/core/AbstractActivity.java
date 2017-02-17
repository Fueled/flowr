package com.fueled.flowr.sample.core;

import android.support.v4.content.ContextCompat;

import com.fueled.flowr.AbstractFlowrActivity;
import com.fueled.flowr.Flowr;
import com.fueled.flowr.sample.R;

public abstract class AbstractActivity extends AbstractFlowrActivity<Flowr> {

    @Override
    protected int getDefaultNavigationBarColor() {
        return ContextCompat.getColor(this, R.color.navigationBarColor);
    }
}
