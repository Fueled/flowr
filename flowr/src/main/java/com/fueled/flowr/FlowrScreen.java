package com.fueled.flowr;

import android.support.annotation.ColorInt;
import android.support.v4.app.FragmentManager;

/**
 * Created by hussein@fueled.com on 31/05/2016.
 * Copyright (c) 2016 Fueled. All rights reserved.
 */

public interface FlowrScreen {

    FragmentManager getScreenFragmentManager();

    void invokeOnBackPressed();

    void setScreenOrientation(int orientation);

    void setNavigationBarColor(@ColorInt int color);
}
