
package com.github.slablock.zcheduler.core.guice;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the object to be managed by {@link com.github.slablock.zcheduler.core.lifecycle.Lifecycle} and set to be on
 * {@link com.github.slablock.zcheduler.core.lifecycle.Lifecycle.Stage#SERVER} stage. This stage gets defined by {@link
 * LifecycleModule}.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ManageLifecycleServer {
}
