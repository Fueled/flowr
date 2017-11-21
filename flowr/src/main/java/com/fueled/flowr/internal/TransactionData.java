package com.fueled.flowr.internal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.fueled.flowr.FlowrFragment;

/**
 * Created by hussein@fueled.com on 16/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public final class TransactionData<T extends Fragment & FlowrFragment> {

    private Class<? extends T> fragmentClass;
    private Bundle args;
    private boolean skipBackStack = false;
    private boolean clearBackStack = false;
    private boolean replaceCurrentFragment = false;
    private int enterAnim;
    private int exitAnim;
    private int popEnterAnim;
    private int popExitAnim;
    private Intent deepLinkIntent;
    private View[] sharedElements;
    private TransitionConfig transitionConfig;

    public TransactionData(Class<? extends T> fragmentClass) {
        this(fragmentClass, FragmentTransaction.TRANSIT_NONE, FragmentTransaction.TRANSIT_NONE);
    }

    public TransactionData(Class<? extends T> fragmentClass, int enterAnim, int exitAnim) {
        this(fragmentClass, enterAnim, exitAnim, FragmentTransaction.TRANSIT_NONE,
                FragmentTransaction.TRANSIT_NONE);
    }

    public TransactionData(Class<? extends T> fragmentClass, int enterAnim, int exitAnim,
                           int popEnterAnim, int popExitAnim) {
        this.fragmentClass = fragmentClass;
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        this.popEnterAnim = popEnterAnim;
        this.popExitAnim = popExitAnim;
    }

    public Intent getDeepLinkIntent() {
        return deepLinkIntent;
    }

    public void setDeepLinkIntent(Intent deepLinkIntent) {
        this.deepLinkIntent = deepLinkIntent;
    }

    public int getPopEnterAnim() {
        return popEnterAnim;
    }

    public void setPopEnterAnim(int popEnterAnim) {
        this.popEnterAnim = popEnterAnim;
    }

    public int getPopExitAnim() {
        return popExitAnim;
    }

    public void setPopExitAnim(int popExitAnim) {
        this.popExitAnim = popExitAnim;
    }

    public Class<? extends T> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends T> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public Bundle getArgs() {
        return args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    public boolean isSkipBackStack() {
        return skipBackStack;
    }

    public void setSkipBackStack(boolean skipBackStack) {
        this.skipBackStack = skipBackStack;
    }

    public boolean isClearBackStack() {
        return clearBackStack;
    }

    public void setClearBackStack(boolean clearBackStack) {
        this.clearBackStack = clearBackStack;
    }

    public boolean isReplaceCurrentFragment() {
        return replaceCurrentFragment;
    }

    public void setReplaceCurrentFragment(boolean replaceCurrentFragment) {
        this.replaceCurrentFragment = replaceCurrentFragment;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public void setEnterAnim(int enterAnim) {
        this.enterAnim = enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public void setExitAnim(int exitAnim) {
        this.exitAnim = exitAnim;
    }

    public View[] getSharedElements() {
        return sharedElements;
    }

    public void setSharedElements(View[] sharedElements) {
        this.sharedElements = sharedElements;
    }

    public TransitionConfig getTransitionConfig() {
        return transitionConfig;
    }

    public void setTransitionConfig(TransitionConfig transitionConfig) {
        this.transitionConfig = transitionConfig;
    }
}
