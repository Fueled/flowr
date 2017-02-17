package com.fueled.flowr;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by hussein@fueled.com on 31/05/2016.
 * Copyright (c) 2016 Fueled. All rights reserved.
 */
public interface ToolbarHandler {

    void setNavigationIcon(NavigationIconType navigationIconType);

    void setCustomNavigationIcon(Drawable navigationIcon);

    void setToolbarVisible(boolean visible);

    void setToolbarTitle(String title);

    void setToolbarNavigationButtonListener(View.OnClickListener onClickListener);

}
