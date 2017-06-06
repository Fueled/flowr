package com.fueled.flowr.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hussein@fueled.com on 05/06/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DeepLinkHandler {

    /**
     * The name of the generated deep link handler class, by default this will be the name
     * of the class annotated with "Impl" appended at the end.
     *
     * @return the name to be used for the generated class.
     */
    String value() default "";

}
