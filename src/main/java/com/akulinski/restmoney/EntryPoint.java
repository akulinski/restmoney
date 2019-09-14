package com.akulinski.restmoney;

import com.akulinski.restmoney.core.components.ApplicationComponent;
import com.akulinski.restmoney.core.components.DaggerApplicationComponent;

import static spark.Spark.port;

public class EntryPoint {

    private ApplicationComponent applicationComponent;

    private void start() {
        port(8080);

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
