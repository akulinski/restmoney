package com.akulinski.restmoney.core.components;

import com.akulinski.restmoney.ResourceRegistry;
import com.akulinski.restmoney.core.modules.ApplicationModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    ResourceRegistry resourceRegistry();
}
