package com.mike.funfactsTemplateRebuild;

import android.app.Application;

import com.parse.Parse;

public class FunFactsApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "g0NC0uAhoTO9TtnrHFvHnknKIDHkxbU2BSUpmZ5K", "aiH92fzRoAPQ9QteMCxHlCqsmqKetXlZcTDtv2me");
    }


}
