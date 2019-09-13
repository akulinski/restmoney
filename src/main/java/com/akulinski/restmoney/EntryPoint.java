package com.akulinski.restmoney;

import com.akulinski.restmoney.core.components.ApplicationComponent;
import com.akulinski.restmoney.core.components.DaggerApplicationComponent;

public class EntryPoint {

    private ApplicationComponent applicationComponent;

    private void start() {

        initializeDagger();

        registerRoutes();
    }

    private void initializeDagger() {
        applicationComponent = DaggerApplicationComponent.create();
    }

    private void registerRoutes() {
        applicationComponent.resourceRegistry().registerRoutes();
    }

    public static void main(String[] args) {
        new EntryPoint().start();
    }

}
