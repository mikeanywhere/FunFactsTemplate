package com.mike.funfactsTemplateRebuild;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by mike on 14/11/2014.
 */
public class FunFactsApplication extends Application {
    public void onCreate() {
        Parse.initialize(this, "g0NC0uAhoTO9TtnrHFvHnknKIDHkxbU2BSUpmZ5K", "aiH92fzRoAPQ9QteMCxHlCqsmqKetXlZcTDtv2me");
    }


}
