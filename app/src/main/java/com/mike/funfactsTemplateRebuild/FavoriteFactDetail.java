package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pollfish.main.PollFish;
import com.pollfish.constants.Position;

import com.ironsource.mobilcore.MobileCore;

import java.util.ArrayList;
import java.util.Arrays;


public class FavoriteFactDetail extends Activity {

    private final static String DELIMITER = ";;DELIMITER;;";
    private final static String FAVORITE_KEY = "Favorites";
    private final static String PREFS_NAME = "MyPreferencesFile";
    private final static String DEF_VAL = "You don't have any favorites yet!";
    int mTapCount =0;
    private ColorWheel mColorWheel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_facts_detail);

        final Button backToFactsButton = (Button) findViewById(R.id.ButtonBackToMainFacts);
        final Button shareButton = (Button) findViewById(R.id.FavoriteShareButton);
        final Button favoritesRemove = (Button) findViewById(R.id.FavoriteRemoveButton);
        ColorWheel colorWheel = new ColorWheel();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
        String[] favoritesAsStringArray = favoritesAsString.split(DELIMITER);
        Intent intent = getIntent();
        final RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.rLay_Favorite);
        final TextView favoriteTextView = (TextView) findViewById(R.id.FavoriteFact);
        final Button previous = (Button) findViewById(R.id.FavoritePreviousButton);
        final Button next = (Button) findViewById(R.id.NextFavorite);
        final int position = intent.getIntExtra("itemClicked", 1);
        final String favorite = favoritesAsStringArray[position];
        final boolean isAdFree = sharedPreferences.getBoolean("isAdFree",false);
        boolean isPollFree = sharedPreferences.getBoolean("isPollFree", false);
        final ImageView BG = (ImageView) findViewById(R.id.favorite_fact_bg);

        final View.OnClickListener onClickListenerBackToFacts = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoriteFactDetail.this, FunFactsActivity.class);
                startActivity(intent);
            }
        };

        next.setClickable(true);
        previous.setClickable(true);
        backToFactsButton.setVisibility(View.INVISIBLE);
        backToFactsButton.setClickable(false);

        //Set typeFace for all elements
        Typeface type = Typeface.createFromAsset(getAssets(), "expressway rg.ttf");
        favoriteTextView.setTypeface(type);
        backToFactsButton.setTypeface(type);
        shareButton.setTypeface(type);
        favoritesRemove.setTypeface(type);
        previous.setTypeface(type);
        next.setTypeface(type);


        mColorWheel = new ColorWheel();

        mRelativeLayout.setBackgroundColor(colorWheel.getColor());
        favoriteTextView.setText(favorite);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTapCount % 10 == 0 && mTapCount != 0 && !isAdFree) {
                    MobileCore.showInterstitial(FavoriteFactDetail.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount++;
                } else {
                    //set up next fact.
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    BG.setImageResource(mColorWheel.getBackgroundImage());
                    next.setTextColor(color);
                    mTapCount++;
                    //get and show fact
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
                    String[] favoritesAsStringArray = favoritesAsString.split(DELIMITER);

                    if(mTapCount < favoritesAsStringArray.length) {
                        String nextFact = favoritesAsStringArray[mTapCount];
                        favoriteTextView.setText(nextFact);
                    }else{
                        mTapCount = 0;
                        String nextFact = favoritesAsStringArray[mTapCount];
                        favoriteTextView.setText(nextFact);

                    }
                 }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTapCount % 10 == 0 && mTapCount != 0 && !isAdFree) {
                    MobileCore.showInterstitial(FavoriteFactDetail.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount--;
                } else {
                    //set up next fact.
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    BG.setImageResource(mColorWheel.getBackgroundImage());
                    mTapCount--;

                    //get and show fact
                    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                    String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
                    String[] favoritesAsStringArray = favoritesAsString.split(DELIMITER);
                    int favoriteTotal = favoritesAsStringArray.length;

                    if(mTapCount >= 1){
                    String nextFact = favoritesAsStringArray[mTapCount];
                    favoriteTextView.setText(nextFact);
                    mTapCount--;
                    }else{
                        String nextFact = favoritesAsStringArray[mTapCount+1];
                        favoriteTextView.setText(nextFact);
                        mTapCount = favoriteTotal;
                    }
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, favorite);
                startActivity(Intent.createChooser(intent, "How would you like to send?"));
            }
        });

        favoritesRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fFact = favoriteTextView.getText().toString();
                FactBook.removeFavoriteFromFavorites(FavoriteFactDetail.this, fFact);
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
                String[] favoritesAsStringArray = favoritesAsString.split(DELIMITER);
                String prevFact = favoritesAsStringArray[favoritesAsStringArray.length-1];
                BG.setImageResource(mColorWheel.getBackgroundImage());
                favoriteTextView.setText(prevFact);
                if (favoritesAsString.isEmpty()){
                    BG.setImageResource(R.drawable.exclaimation);
                    favoriteTextView.setText(DEF_VAL);
                    next.setOnClickListener(onClickListenerBackToFacts);
                    next.setText(R.string.action_facts);
                    favoritesRemove.setClickable(false);
                    shareButton.setClickable(false);
                    previous.setClickable(false);

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_fact_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.mike.funfactsTemplateRebuild.R.id.back_to_facts) {
            Intent intent = new Intent(FavoriteFactDetail.this, FunFactsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_contact) {
            Intent intent = new Intent(FavoriteFactDetail.this, ContactMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_submit) {
            Intent intent = new Intent(FavoriteFactDetail.this, SubmitFact.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
            Intent intent = new Intent(FavoriteFactDetail.this, AboutMe.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
