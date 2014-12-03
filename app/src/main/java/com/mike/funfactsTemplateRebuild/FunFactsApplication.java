package com.mike.funfactsTemplateRebuild;

import android.app.Application;
import android.provider.Settings;

import com.parse.Parse;
import com.parse.ParsePush;
import com.parse.PushService;

public class FunFactsApplication extends Application {
    public void onCreate() {
        Parse.initialize(this,"g0NC0uAhoTO9TtnrHFvHnknKIDHkxbU2BSUpmZ5K","aiH92fzRoAPQ9QteMCxHlCqsmqKetXlZcTDtv2me");
        // Also in this method, specify a default Activity to handle push notifications
        PushService.setDefaultPushCallback(this, FunFactsActivity.class);

    }

}
