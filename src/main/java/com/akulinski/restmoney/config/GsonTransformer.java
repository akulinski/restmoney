package com.akulinski.restmoney.config;

import com.google.gson.Gson;
import spark.ResponseTransformer;

import javax.inject.Inject;

public class GsonTransformer implements ResponseTransformer {

    private final Gson gson;

    @Inject
    public GsonTransformer(Gson gson){
        this.gson = gson;
    }

    @Override
    public String render(Object model) throws Exception {
        return gson.toJson(model);
    }
}
