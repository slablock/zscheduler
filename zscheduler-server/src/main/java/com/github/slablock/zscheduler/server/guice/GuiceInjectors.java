/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.slablock.zscheduler.server.guice;

import com.github.slablock.zcheduler.core.guice.JsonConfigProvider;
import com.github.slablock.zcheduler.core.guice.ZGuiceExtensions;
import com.github.slablock.zcheduler.core.guice.ZSecondaryModule;
import com.github.slablock.zscheduler.server.initialization.ExtensionsConfig;
import com.github.slablock.zscheduler.server.initialization.ModulesConfig;
import com.github.slablock.zscheduler.server.jackson.JacksonModule;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class GuiceInjectors {
    public static Collection<Module> makeDefaultStartupModules() {
        return ImmutableList.of(
                new ZGuiceExtensions(),
                new JacksonModule(),
                new ConfigModule(),
                new PropertiesModule(Arrays.asList("common.runtime.properties", "runtime.properties")),
                binder -> {
                    binder.bind(ZSecondaryModule.class);
                    JsonConfigProvider.bind(binder, "druid.extensions", ExtensionsConfig.class);
                    JsonConfigProvider.bind(binder, "druid.modules", ModulesConfig.class);
                }
        );
    }

    public static Injector makeStartupInjector() {
        return Guice.createInjector(makeDefaultStartupModules());
    }

    public static Injector makeStartupInjectorWithModules(Iterable<? extends Module> modules) {
        List<Module> theModules = new ArrayList<>();
        theModules.addAll(makeDefaultStartupModules());
        for (Module theModule : modules) {
            theModules.add(theModule);
        }
        return Guice.createInjector(theModules);
    }
}
