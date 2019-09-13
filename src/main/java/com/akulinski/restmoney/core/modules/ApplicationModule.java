package com.akulinski.restmoney.core.modules;

import com.akulinski.restmoney.config.GsonTransformer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import spark.ResponseTransformer;


@Module
public class ApplicationModule {

    @Provides
    public Gson provideGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Provides
    public ResponseTransformer provideResponseTransformer(GsonTransformer gsonTransformer) {
        return gsonTransformer;
    }
}
