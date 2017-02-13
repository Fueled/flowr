package com.fueled.router;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public abstract class AbstractRouterActivity<T extends AbstractRouter> extends AppCompatActivity
        implements RouterScreen {

    public abstract T getRouter();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getRouter() != null) {
            getRouter().onDestroy();
        }
    }

    @Override
    public FragmentManager getScreenFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void invokeOnBackPressed() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (getRouter() == null || !getRouter().onBackPressed()) {
            super.onBackPressed();
        }
    }

    protected int getDefaultOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    protected int getDefaultNavigationBarColor() {
        return Color.BLACK;
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void setScreenOrientation(int orientation) {
        // if the orientation is unspecified we set it to activity default
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            setRequestedOrientation(getDefaultOrientation());
        } else {
            setRequestedOrientation(orientation);
        }
    }

    @Override
    public void setNavigationBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color == -1) {
                getWindow().setNavigationBarColor(getDefaultNavigationBarColor());
            } else {
                getWindow().setNavigationBarColor(color);
            }
        }
    }
}
