package com.fueled.flowr.internal;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fueled.flowr.FlowrFragment;

/**
 * Created by hussein@fueled.com on 16/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public final class TransactionData<T  extends Fragment & FlowrFragment> {

    private Class<? extends T> fragmentClass;
    private Bundle args;
    private boolean skipBackStack = false;
    private boolean clearBackStack = false;
    private boolean replaceCurrentFragment = false;
    private int enterAnim;
    private int exitAnim;

    public TransactionData(Class<? extends T> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public TransactionData(Class<? extends T> fragmentClass, int enterAnim, int exitAnim) {
        this.fragmentClass = fragmentClass;
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
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
}
