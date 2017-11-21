package com.fueled.flowr.internal;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 21/11/17
 */

public class TransitionConfig {

    public Transition sharedElementEnter;
    public Transition sharedElementExit;
    public Transition enter;
    public Transition exit;

    private TransitionConfig(Builder builder) {
        sharedElementEnter = builder.sharedElementEnter;
        sharedElementExit = builder.sharedElementExit;
        enter = builder.enter;
        exit = builder.exit;
    }

    public static final class Builder {
        private Transition sharedElementEnter;
        private Transition sharedElementExit;
        private Transition enter;
        private Transition exit;

        public Builder() {
        }

        public Builder sharedElementEnter(Transition val) {
            sharedElementEnter = val;
            return this;
        }

        public Builder sharedElementExit(Transition val) {
            sharedElementExit = val;
            return this;
        }

        public Builder enter(Transition val) {
            enter = val;
            return this;
        }

        public Builder exit(Transition val) {
            exit = val;
            return this;
        }

        public TransitionConfig build() {
            return new TransitionConfig(this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static class Provider {
        public static Transition fade = new Fade();
        public static Transition explode = new Explode();
        public static Transition slideRight = new Slide(Gravity.RIGHT);
        public static Transition slideLeft = new Slide(Gravity.LEFT);
        public static Transition changeBounds = new ChangeBounds();
    }
}
