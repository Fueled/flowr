package com.fueled.flowr;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public abstract class AbstractFlowrActivity<T extends Flowr> extends AppCompatActivity
        implements FlowrScreen {

    public abstract T getFlowr();

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getFlowr() != null) {
            getFlowr().syncScreenState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getFlowr() != null) {
            getFlowr().onDestroy();
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
        if (getFlowr() == null || !getFlowr().onBackPressed()) {
            super.onBackPressed();
        }
    }

    protected int getDefaultOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    protected int getDefaultNavigationBarColor() {
        return Color.BLACK;
    }

    @Override
    public void onCurrentFragmentChanged(@Nullable Fragment currentFragment) {
        // Do Nothing. No Default implementation is required.
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
