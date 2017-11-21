package com.fueled.flowr.internal;

import android.transition.Transition;

/**
 * Copyright (c) 2017 Fueled. All rights reserved.
 *
 * @author chetansachdeva on 21/11/17
 */

public class TransitionConfig {

    public Transition sharedElementEnter;
    public Transition sharedElementReturn;
    public Transition enter;
    public Transition exit;

    private TransitionConfig(Builder builder) {
        sharedElementEnter = builder.sharedElementEnter;
        sharedElementReturn = builder.sharedElementReturn;
        enter = builder.enter;
        exit = builder.exit;
    }

    public static final class Builder {
        private Transition sharedElementEnter;
        private Transition sharedElementReturn;
        private Transition enter;
        private Transition exit;

        public Builder() {
        }

        public Builder sharedElementEnter(Transition val) {
            sharedElementEnter = val;
            return this;
        }

        public Builder sharedElementReturn(Transition val) {
            sharedElementReturn = val;
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
}
