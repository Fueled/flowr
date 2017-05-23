package com.fueled.flowr.internal;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fueled.flowr.FlowrFragment;

/**
 * Class that contains all the necessary info for Deep Links.
 * Created by julienFueled on 5/11/17.
 */

public class FlowrDeepLinkInfo<T extends Fragment & FlowrFragment> {
    public final Bundle data;
    public final Class<? extends T> fragment;

    public FlowrDeepLinkInfo(Bundle data, Class<? extends T> fragment) {
        this.data = data;
        this.fragment = fragment;
    }
}
