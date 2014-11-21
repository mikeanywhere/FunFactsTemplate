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
    protected FileTools mFileTools;
    protected static boolean getFactsFromBase;
    protected final String FACT_KEY = "Facts";
    protected final static String DEF_VAL = "something appears to have gone wrong";
    protected final static String DELIMITER = ";;DELIMITER;;";
    protected final static String FAVORITE_KEY = "Favorites";
    protected final static String PREFS_NAME = "MyPreferencesFile";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.init(this, "GORDC001AID2T0TARB2WJ3TYTM0P", MobileCore.LOG_TYPE.DEBUG, MobileCore.AD_UNITS.INTERSTITIAL);
        setContentView(activity_fun_facts);
        final TextView FACT_LABEL = (TextView) findViewById(com.mike.funfactsTemplateRebuild.R.id.FactTextView);
        final Button SHOW_FACT_BUTTON = (Button) findViewById(com.mike.funfactsTemplateRebuild.R.id.ShowFactButton);

        mRelativeLayout = (RelativeLayout) findViewById(com.mike.funfactsTemplateRebuild.R.id.rLay);
        mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.toggleButton);
        mTapCount = 0;
        mFileTools = new FileTools();

        //Toggle favorites
        mFavoritesToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fFact = FACT_LABEL.getText().toString();
                FactBook.addFactToFavorites(FunFactsActivity.this, fFact);
            }
        });

        //Display new fact
        SHOW_FACT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTapCount % 10 == 0 && mTapCount != 0) {
                    MobileCore.showInterstitial(FunFactsActivity.this, null);
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
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            String facts = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
            String[] factsArray = facts.split(DELIMITER);
            intent.putExtra("facts",factsArray);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String facts = sharedPreferences.getString(FACT_KEY,"null");
            if (facts.contains("null")){
                getFactsFromBase = true;
            }else{
                getFactsFromBase = false;
            }
    }
    @Override
    public void onBackPressed() {
        MobileCore.showInterstitial(this, new CallbackResponse() {

            @Override
            public void onConfirmation(TYPE type) {
                FunFactsActivity.this.finish();
            }
        });
    }
}
