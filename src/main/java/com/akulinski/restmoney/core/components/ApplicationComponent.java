package com.akulinski.restmoney.core.components;

import com.akulinski.restmoney.ResourceRegistry;
import com.akulinski.restmoney.core.modules.ApplicationModule;
import com.akulinski.restmoney.core.modules.DbModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ApplicationModule.class, DbModule.class})
public interface ApplicationComponent {
    ResourceRegistry resourceRegistry();
}
