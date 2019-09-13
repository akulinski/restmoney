package com.akulinski.restmoney;

import spark.ResponseTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.get;

@Singleton
public class ResourceRegistry {

    private ResponseTransformer responseTransformer;

    @Inject
    ResourceRegistry(ResponseTransformer responseTransformer) {
        this.responseTransformer = responseTransformer;
    }

    public void registerRoutes() {
        get("/test", "application/json", (req, resp) -> "Hello word", responseTransformer);
    }
}
