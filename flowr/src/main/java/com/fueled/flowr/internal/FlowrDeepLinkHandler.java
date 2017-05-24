package com.fueled.flowr.internal;

import android.content.Intent;

/**
 * Created by julienFueled on 5/11/17.
 */

public interface FlowrDeepLinkHandler {
    FlowrDeepLinkInfo routeIntentToScreen(Intent intent);
}
