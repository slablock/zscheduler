
package com.github.slablock.zcheduler.core.guice;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.InjectableValues;
import com.google.inject.Injector;
import com.google.inject.Key;

public class GuiceInjectableValues extends InjectableValues {
    private final Injector injector;

    public GuiceInjectableValues(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object findInjectableValue(
            Object valueId,
            DeserializationContext ctxt,
            BeanProperty forProperty,
            Object beanInstance
    ) {
        // From the docs:   "Object that identifies value to inject; may be a simple name or more complex identifier object,
        //                  whatever provider needs"
        // Currently we should only be dealing with `Key` instances, and anything more advanced should be handled with
        // great care
        if (valueId instanceof Key) {
            return injector.getInstance((Key) valueId);
        }
        throw new IllegalArgumentException(String.format(
                "Unknown class type [%s] for valueId [%s]",
                valueId.getClass().getName(),
                valueId.toString()
        ));
    }
}
