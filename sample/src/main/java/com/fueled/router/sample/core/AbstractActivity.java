package com.fueled.router.sample.core;

import android.support.v4.content.ContextCompat;

import com.fueled.router.AbstractRouterActivity;
import com.fueled.router.Router;
import com.fueled.router.sample.R;

public abstract class AbstractActivity extends AbstractRouterActivity<Router> {

    @Override
    protected int getDefaultNavigationBarColor() {
        return ContextCompat.getColor(this, R.color.navigationBarColor);
    }
}
