package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.ironsource.mobilcore.CallbackResponse;
import com.ironsource.mobilcore.MobileCore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mike.funfactsTemplateRebuild.R.layout.activity_fun_facts;

public class FunFactsActivity extends Activity {

    private final ColorWheel mColorWheel = new ColorWheel();
    private RelativeLayout mRelativeLayout;
    private int mTapCount;
    private ToggleButton mFavoritesToggleButton;
    private final String FACT_KEY = "Facts";
    private final static String DEF_VAL = "You don't have any favorites yet!";
    private final static String PREFS_NAME = "MyPreferencesFile";
    private final static String DELIMITER = ";;DELIMITER;;";
    private boolean hasFavorited;
    private boolean hasAlreadyRun;

    //TODO once this is complete, replace the TODO list with a set of TODOs for each new app (subject specific)
//---------------------------------------------EASY--------------------------------------------------
    //TODO Work out local and Push notifications.
    //TODO Different string resources for different locales?
    //---------------------------------------MEDIUM--------------------------------------------------
    //TODO Implement a way to manipulate favorites (ie remove/share/etc)                                            ------EDIT:Half------
    //TODO this should be implemented in the ListView of Favorites: https://github.com/daimajia/AndroidSwipeLayout
    //TODO Get colorWheel.java setting entire background images(or layouts, perhaps?) as well as colours, and set a way to choose depending on app.
    //TODO Add 'native ads' to the Favorite Facts Activity & XML.
    //TODO Add https://github.com/amlcurran/ShowcaseView to show first time users how to use the app. add these icons:? https://www.iconfinder.com/icons/172671/contract_gesture_icon#size=358
    //TODO ensure the exitAd is compliant. EDIT: This may already be compliant, awaiting response from mobileCore as of 21/11/2014.
    //----------------------------------------HARD---------------------------------------------------

    //----------------------------------------LONG---------------------------------------------------

    //---------------------------------------UNKNOWN-------------------------------------------------
    //TODO Streamline.
    //TODO implement GESTURES!!! (flick facts out of the way) this dude is awesome... http://www.youtube.com/watch?v=tG3lzBDMRQQ also search for drag and drop stuff.
    //TODO buy me a pint IAP?!?!?! https://developer.android.com/google/play/billing/index.html?hl=d
    //TODO create branches of the template: 3rd party stores(notification ads), and paid.
    //----------------------------------------DONE?--------------------------------------------------
    //TODO Recode all hardcoded strings in values file. urg.                                                        ------EDIT:DONE------
    //TODO Add optional fields 'name' and 'location' to 'submit fact' activity and to main page with if statement.  ------EDIT:DONE------
    //TODO make contact activity parse.com style.(ie contact form)                                                  ------EDIT:DONE------
    //TODO reduce the size of text in FavoriteDetailSwipe as some long facts are longer than the screen.            ------EDIT:DONE------
    //TODO Reference baseFacts from Values file.                                                                    ------EDIT:DONE------
    //TODO create a 'removeFactFromFavorites' method in FactBook.java... to be implemented for both above tasks.    ------EDIT:DONE------
    //TODO ensure favorites can't be repeated.                                                                      ------EDIT:DONE------
    //TODO Make submitted facts appear in Favorites & Saved Facts                                                   ------EDIT:DONE------
    //TODO Stop repeating randoms (See below).                                                                      ------EDIT:DONE------
    //TODO Deal with no more facts                                                                                  ------EDIT:DONE------
    //TODO Ensure all context menus appear and display right.                                                       ------EDIT:DONE------
    //TODO For now, make the app portrait only.                                                                     ------EDIT:DONE------
    //TODO apparently Arrays.asList isn't changeable in size or structure, therefore we'll need to come up with something that works. This may be fixed as of 21:10 on 21/11/2014.                                                                                ------EDIT:DONE------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.init(this, "GORDC001AID2T0TARB2WJ3TYTM0P", MobileCore.LOG_TYPE.PRODUCTION, MobileCore.AD_UNITS.INTERSTITIAL);
        setContentView(activity_fun_facts);
        final TextView FACT_LABEL = (TextView) findViewById(com.mike.funfactsTemplateRebuild.R.id.FactTextView);
        final Button SHOW_FACT_BUTTON = (Button) findViewById(com.mike.funfactsTemplateRebuild.R.id.ShowFactButton);

        mRelativeLayout = (RelativeLayout) findViewById(com.mike.funfactsTemplateRebuild.R.id.rLay);
        mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.toggleButton);
        Button mShareButton = (Button) findViewById(R.id.shareButton);
        mTapCount = 0;

        //Set the typeFace. Typfaces are stored in the assets folder.
        Typeface type = Typeface.createFromAsset(getAssets(), "cibreo.ttf");
        FACT_LABEL.setTypeface(type);


        if(!hasAlreadyRun){
            Resources resources = getResources();
            String[] baseFactArray = resources.getStringArray(R.array.list_base_facts_from_resource_file);
            StringBuilder stringBuilder = new StringBuilder();
            for (String aBaseFactArray : baseFactArray) {
                stringBuilder.append(aBaseFactArray).append(DELIMITER);
            }
            String finalString = stringBuilder.toString();
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            sharedPreferences.edit().clear().putString(FACT_KEY,finalString).apply();
            hasAlreadyRun = true;
        }

        //Display new fact
        SHOW_FACT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deal with adverts.
                if (mTapCount % 10 == 0 && mTapCount != 0) {
                    MobileCore.showInterstitial(FunFactsActivity.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount++;
                    hasFavorited = false;
                } else {
                    //set up fact screen.
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    SHOW_FACT_BUTTON.setTextColor(color);
                    mTapCount++;
                    //get and show fact
                        String fact = FactBook.getRandomFact(FunFactsActivity.this);
                        FACT_LABEL.setText(fact);
                    hasFavorited = false;
                    if (mFavoritesToggleButton.isChecked()) {
                        mFavoritesToggleButton.setChecked(false);
                    }
                }
            }
        });

        //Toggle favorites
        mFavoritesToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasFavorited) {
                    String fFact = FACT_LABEL.getText().toString();
                    FactBook.addFactToFavorites(FunFactsActivity.this, fFact);
                    mFavoritesToggleButton.setChecked(true);
                    hasFavorited = true;
                }else{
                    String fFact = FACT_LABEL.getText().toString();
                    FactBook.removeFavoriteFromFavorites(FunFactsActivity.this,fFact);
                    mFavoritesToggleButton.setChecked(false);
                    hasFavorited = false;
                }
            }
        });

        //Share current fact
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fact = FACT_LABEL.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, fact);
                startActivity(Intent.createChooser(intent, "How would you like to send?"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.mike.funfactsTemplateRebuild.R.menu.fun_facts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == com.mike.funfactsTemplateRebuild.R.id.action_submit) {
            Intent intent = new Intent(FunFactsActivity.this, SubmitFact.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_contact) {
            Intent intent = new Intent(FunFactsActivity.this, ContactMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
            Intent intent = new Intent(FunFactsActivity.this, AboutMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_favorite) {
            Intent intent = new Intent(FunFactsActivity.this, FavoriteFactsList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String listString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
        boolean getFactsFromBase;
        if (listString.contains(DEF_VAL)){
                getFactsFromBase = true;
            }else{
                getFactsFromBase = false;
            }
                if (!getFactsFromBase) {
            String[] listStringArray = listString.split(DELIMITER);
                    List<String> listList = new ArrayList<String>(Arrays.asList(listStringArray));
            Collections.shuffle(listList);
            StringBuilder stringBuilder = new StringBuilder();
                    for (String aListList : listList) {
                        stringBuilder.append(aListList).append(DELIMITER);
                    }
            String finalString = stringBuilder.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(FACT_KEY).putString(FACT_KEY,finalString).apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    MobileCore.refreshOffers();
    }

    @Override
    public void onBackPressed() {
        MobileCore.showInterstitial(this, MobileCore.AD_UNIT_SHOW_TRIGGER.APP_EXIT, new CallbackResponse() {

            @Override
            public void onConfirmation(TYPE type) {
                FunFactsActivity.this.finish();
            }
        });
    }
}