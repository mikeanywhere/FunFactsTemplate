package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.mike.funfactsTemplateRebuild.R.layout.activity_fun_facts;

public class FunFactsActivity extends Activity {

    protected ColorWheel mColorWheel = new ColorWheel();
    protected RelativeLayout mRelativeLayout;
    protected int mTapCount;
    protected ToggleButton mFavoritesToggleButton;
    protected static boolean getFactsFromBase;
    protected final String FACT_KEY = "Facts";
    protected final static String DEF_VAL = "You don't have any favorites yet!";
    protected final static String DELIMITER = ";;DELIMITER;;";
    protected final static String FAVORITE_KEY = "Favorites";
    protected final static String PREFS_NAME = "MyPreferencesFile";
    protected Button mShareButton;

    //TODO Implement a way to manipulate favorites (ie remove/share/etc).
    //TODO Reference baseFacts from Values file. (possible and done using Resources, however can't be done via static. Can we use instance? (test)
    //TODO Stop repeating randoms (See below).
    //TODO Ensure all context menus appear and display right.
    //TODO Make submitted facts appear in Favorites.
    //TODO For now, make the app portrait only.
    //TODO Use isInterstitialReady to ensure the exitAd is compliant.
    //TODO Get colorWheel.java setting entire background images(or layouts, perhaps?) as well as colours, and set a way to choose depending on app.
    //TODO Add 'native ads' to the Favorite Facts Activity & XML.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.init(this, "GORDC001AID2T0TARB2WJ3TYTM0P", MobileCore.LOG_TYPE.DEBUG, MobileCore.AD_UNITS.INTERSTITIAL);
        setContentView(activity_fun_facts);
        final TextView FACT_LABEL = (TextView) findViewById(com.mike.funfactsTemplateRebuild.R.id.FactTextView);
        final Button SHOW_FACT_BUTTON = (Button) findViewById(com.mike.funfactsTemplateRebuild.R.id.ShowFactButton);

        mRelativeLayout = (RelativeLayout) findViewById(com.mike.funfactsTemplateRebuild.R.id.rLay);
        mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.toggleButton);
        mShareButton = (Button) findViewById(R.id.shareButton);
        mTapCount = 0;

        //TODO get this working! The idea is to shake the array up once at the beginning and then loop through with i++'s.
        //TODO the 'Collections.Shuffle' bit below takes care of the initial shake.
        //Initial setup. This randomizes the facts once.
//        if (!getFactsFromBase) {
//            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
//            String listString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
//            String[] listStringArray = listString.split(DELIMITER);
//            List<String> list = Arrays.asList(listStringArray);
//            Collections.shuffle(list);
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < list.size(); i++){
//                stringBuilder.append(list.get(i)).append(DELIMITER);
//            }
//            String finalString = stringBuilder.toString();
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.remove(FACT_KEY).putString(FACT_KEY,finalString).apply();
//        }

        //Display new fact
        SHOW_FACT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTapCount % 10 == 0 && mTapCount != 0) {
                    MobileCore.showInterstitial(FunFactsActivity.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount++;
                } else {
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    SHOW_FACT_BUTTON.setTextColor(color);
                    mTapCount++;
                        String fact = FactBook.getRandomFact(FunFactsActivity.this, getFactsFromBase);
                        FACT_LABEL.setText(fact);
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
                String fFact = FACT_LABEL.getText().toString();
                FactBook.addFactToFavorites(FunFactsActivity.this, fFact);
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
                startActivity(intent);
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
            Intent intent = new Intent(FunFactsActivity.this, FavoriteFacts.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String facts = sharedPreferences.getString(FACT_KEY,DEF_VAL);
            if (facts.contains(DEF_VAL)){
                getFactsFromBase = true;
            }else{
                getFactsFromBase = false;
            }
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