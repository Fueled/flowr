package com.fueled.flowr;

import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by hussein@fueled.com on 31/05/2016.
 * Copyright (c) 2016 Fueled. All rights reserved.
 */

public interface FlowrScreen {

    /**
     * Return the FragmentManager for interacting with fragments associated
     * with this screen.
     */
    FragmentManager getScreenFragmentManager();

    /**
     * Invoke the {@code onBackPressed} method for this screen.
     */
    void invokeOnBackPressed();

    /**
     * Indicates that the fragment currently being displayed has changed.
     *
     * @param currentFragment the fragment currently displayed or {@code null} if no fragment
     *                        is being displayed.
     */
    void onCurrentFragmentChanged(@Nullable Fragment currentFragment);

    /**
     * Specify the screen orientation to be used for this screen.
     *
     * @param orientation the screen orientation to be used.
     */
    void setScreenOrientation(int orientation);

    /**
     * Specify the navigation bar color to be used for this screen.
     *
     * @param color the navigation bar color to be used.
     */
    void setNavigationBarColor(@ColorInt int color);
}
