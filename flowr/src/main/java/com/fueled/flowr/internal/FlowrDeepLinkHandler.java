package com.fueled.flowr.internal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by julienFueled on 5/11/17.
 */

public interface FlowrDeepLinkHandler {

    /**
     * Retrieve deep link info from an intent.
     *
     * @param intent the intent to retrieve the info from.
     * @return the deep link info if found for the specified intent, else null.
     */
    @Nullable
    FlowrDeepLinkInfo getDeepLinkInfoForIntent(@NonNull Intent intent);
}
