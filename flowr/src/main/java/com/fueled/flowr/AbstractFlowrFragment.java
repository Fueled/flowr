package com.fueled.flowr;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public abstract class AbstractFlowrFragment extends Fragment implements FlowrFragment {

    private static final String KEY_FRAGMENT_ID = "key_fragment_id";

    private String fragmentId;
    private boolean shown;

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            fragmentId = savedInstanceState.getString(KEY_FRAGMENT_ID, null);
        }

        if (fragmentId == null) {
            fragmentId = UUID.randomUUID().toString();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fragmentId != null) {
            outState.putString(KEY_FRAGMENT_ID, fragmentId);
        }
    }

    protected void onFragmentResults(int requestCode, int resultCode, Bundle data) {
        // Do Nothing. No Default implementation is required.
    }

    /**
     * Returns the unique id for this fragment instance.
     *
     * @return a unique id for this fragment instance.
     */
    public String getFragmentId() {
        return fragmentId;
    }


    /**
     * @inheritDoc
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean onNavigationIconClick() {
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.BACK;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Drawable getNavigationIcon() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isToolbarVisible() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isDrawerEnabled() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getNavigationBarColor() {
        return -1;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onShown() {
        this.shown = true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onHidden() {
        this.shown = false;
    }

    /**
     * Returns whether this fragment is currently at the top of the back stack.
     *
     * @return true if this fragment is at the top of the back stack.
     */
    protected boolean isShown() {
        return shown;
    }
}
