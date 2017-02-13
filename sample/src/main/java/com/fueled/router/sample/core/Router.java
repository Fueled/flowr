package com.fueled.router.sample.core;

import android.support.annotation.IdRes;

import com.fueled.router.AbstractRouter;
import com.fueled.router.FragmentsResultPublisher;
import com.fueled.router.RouterScreen;
import com.fueled.router.ToolbarHandler;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class Router extends AbstractRouter<AbstractFragment> {

    public Router(@IdRes int mainContainerId, RouterScreen screen, ToolbarHandler toolbarHandler,
                  FragmentsResultPublisher resultPublisher) {
        super(mainContainerId, screen, toolbarHandler, null, resultPublisher);
    }

}
