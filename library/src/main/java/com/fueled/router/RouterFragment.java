package com.fueled.router;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * Created by hussein@fueled.com on 09/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public interface RouterFragment {

    /**
     * Called when the fragment has been popped back from the stack
     */
    void onPoppedBackFromStack();

    /**
     * Called when the back button has been pressed. Override this method
     * if the fragment needs to handle back button presses
     *
     * @return true if the event has been handled by the fragment
     */
    boolean onBackPressed();

    /**
     * Override this method if this fragment should handle navigation icon clicks
     *
     * @return true if the click was handled by the fragment
     */
    boolean onNavigationIconClick();

    /**
     * Returns the type of the navigation icon that should be show in the current toolbar
     * <p/>
     * Note: when the returned navigation icon type is {@link NavigationIconType.CUSTOM}
     * you must also override {@link #getNavigationIcon()}
     *
     * @return the navigation icon that should be shown
     */
    NavigationIconType getNavigationIconType();

    /**
     * Returns the drawable to be used for the navigation icon when the navigation icon type
     * is {@link NavigationIconType.CUSTOM}
     *
     * @return the drawable for the navigation icon
     */
    Drawable getNavigationIcon();

    /**
     * Specify whether the toolbar should be visible or not for this fragment,
     * this option is only available if the container screen has a toolbar and the router
     * was provided with a {@link ToolbarHandler} instance.
     *
     * @return true if the toolbar should be visible for this fragment
     */
    boolean isToolbarVisible();

    /**
     * Specify whether the drawer should be enabled or not for this fragment,
     * this option is only available if the container screen has a drawer and the router
     * was provided with a {@link DrawerHandler} instance.
     *
     * @return true if the drawer should be enabled for this fragment
     */
    boolean isDrawerEnabled();

    /**
     * Returns the screen orientation to be used while this fragment is visible,
     * return {@link android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED} to use the activity
     * default orientation
     *
     * @return the screen orientation to be used for this fragment
     */
    int getScreenOrientation();

    /**
     * Returns the color to be used for the navigation bar while this fragment is visible, return
     * -1 to use the activity default color
     *
     * @return the color to be used for the navigation bar
     */
    @ColorInt int getNavigationBarColor();

    /**
     *
     * @return
     */
    String getTitle();
}
