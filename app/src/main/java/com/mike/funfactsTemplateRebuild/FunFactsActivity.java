package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ironsource.mobilcore.CallbackResponse;
import com.ironsource.mobilcore.MobileCore;
import com.pollfish.constants.Position;
import com.pollfish.interfaces.PollfishSurveyCompletedListener;
import com.pollfish.main.PollFish;
import com.pollfish.interfaces.PollfishOpenedListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.mike.funfactsTemplateRebuild.R.layout.activity_fun_facts;

public class FunFactsActivity extends Activity implements PollfishSurveyCompletedListener, PollfishOpenedListener {

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
    protected boolean hasAnsweredSurvey;
    protected boolean isAdFree = false;
    protected boolean isPollFree = false;
    protected int numberOfFinishedSurveys = 0;
    protected int numberOfSurveysRequiredToGoAdFree = 5;
    static int backpresses = 0;

    /*
TODO once this is complete, replace the TODO list with a set of TODOs for each new app (subject specific)
---------------------------------------ALWAYS--------------------------------------------------
TODO: Ensure manifest files work correctly for parse...
TODO Make pollfish active (non-debug mode)
TODO find the right font
This is the format for JSON which will be pushed to the device:
{ "alert": "This is the content of the notification message!",
"title": "This is the title!",
"fact": "This is a single fact! #Fact@Ack"
"factArray": "this is an array of facts! I used the final DELIMITER to split the facts on the backend!"
} (#Fact@Ack should be equal to the "hashtag" String defined in Strings. ie #Joke@ack...
----------------------------------------EASY---------------------------------------------------
TODO find the right font and apply to all INCLUDING Favorites List.
TODO change 'about' and 'contact' pages to look more like a larger company.
TODO Privacy policy, Ts&Cs etc.... (done, these are in the downloads folder)
TODO Add https://github.com/amlcurran/ShowcaseView to show first time users how to use the app.
---------------------------------------MEDIUM--------------------------------------------------
TODO this should be implemented in the ListView of Favorites: https://github.com/daimajia/AndroidSwipeLayout
TODO Add 'native ads' to the Favorite Facts Activity & XML.
----------------------------------------HARD---------------------------------------------------
----------------------------------------LONG---------------------------------------------------
---------------------------------------UNKNOWN-------------------------------------------------
TODO Streamline.
TODO implement GESTURES!!! (flick facts out of the way) this dude is awesome... http://www.youtube.com/watch?v=tG3lzBDMRQQ also search for drag and drop stuff.
TODO buy me a pint IAP?!
TODO incentivized videos/polls. served by http://www.adcolony.com/monetization/ or http://www.matomy.com/offer-wall. Incentives: Fonts, google search from app, save jpeg, remove #tag,background image packs...
TODO create branches of the template: 3rd party stores(notification ads), and paid.
----------------------------------------DONE--------------------------------------------------
TODO ensure the exitAd is compliant. EDIT: This may already be compliant, awaiting response from mobileCore as of 21/11/2014.
TODO APP IS CALLED FACT@ACK!!!
TODO Change Pollfish AlertBuilder strings into string resources and translate
TODO fix no favorites left after delete.
TODO disable all buttons but Next in on~Create of MainApp, enable them on first click of next.
TODO submit a fact. On submission, set the positive button of the alert to change the text of the submit button to 'back to facts'
TODO on return from favorites, buttons are set unclickable.
TODO Add Parse analytics
TODO change icon... YAY! (according to lint, " Notification icons must be entirely white" We'll see if this is enforced)
TODO add subitted fact to shared pref favorites.
TODO remove all #Fact@Ack and replace with a dedicated string resource.
TODO mine the qi and other fact book for about 300 facts.
TODO Get colorWheel.java setting entire background images as well as colours, and set a way to choose depending on app.
TODO implement the ad/poll booleans
TODO append all facts with FACT@ACK
TODO save and load the booleans action and resume.
TODO add pollfish
TODO horizontal Centre the facts?
TODO make all fonts the same.
TODO move much of the list activity into onResume (as the list needs to be updated on  back press from fav details.
TODO Implement a way to manipulate favorites (ie remove/share/etc)
TODO stop both previous buttons from being hardcoded
TODO make the main page a scroll page to accomodate long text.
TODO Work out local and Push notifications.
TODO Different string resources for different locales?
TODO Recode all hardcoded strings in values file. urg.
TODO Add optional fields 'name' and 'location' to 'submit fact' activity and to main page with if statement.
TODO make contact activity parse.com style.(ie contact form)
TODO reduce the size of text in FavoriteDetailSwipe as some long facts are longer than the screen.
TODO Reference baseFacts from Values file.
TODO create a 'removeFactFromFavorites' method in FactBook.java... to be implemented for both above tasks.
TODO ensure favorites can't be repeated.
TODO Make submitted facts appear in Favorites & Saved Facts
TODO Stop repeating randoms (See below).
TODO Deal with no more facts
TODO Ensure all context menus appear and display right.
TODO For now, make the app portrait only.
TODO apparently Arrays.asList isn't changeable in size or structure,
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.init(this, "GORDC001AID2T0TARB2WJ3TYTM0P", MobileCore.LOG_TYPE.PRODUCTION, MobileCore.AD_UNITS.INTERSTITIAL);
        setContentView(activity_fun_facts);
        final TextView FACT_LABEL = (TextView) findViewById(com.mike.funfactsTemplateRebuild.R.id.FactTextView);
        final Button SHOW_FACT_BUTTON = (Button) findViewById(com.mike.funfactsTemplateRebuild.R.id.NextFavorite);

        mRelativeLayout = (RelativeLayout) findViewById(com.mike.funfactsTemplateRebuild.R.id.rLay);
        mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.FavoriteShareButton);
        final Button mShareButton = (Button) findViewById(R.id.FavoriteRemoveButton);
        mTapCount = 0;
        final Button previousButton = (Button) findViewById(R.id.button_previous);
        final ImageView backgroundImage = (ImageView) findViewById(R.id.facts_background_image);
        final TextView didYouKnow = (TextView) findViewById(R.id.Submit_Title);


            previousButton.setClickable(false);
            mFavoritesToggleButton.setClickable(false);
            mShareButton.setClickable(false);


        //Set the typeFace. Typfaces are stored in the assets folder.
        Typeface type = Typeface.createFromAsset(getAssets(), "expressway rg.ttf");
        FACT_LABEL.setTypeface(type);
        previousButton.setTypeface(type);
        SHOW_FACT_BUTTON.setTypeface(type);
        mFavoritesToggleButton.setTypeface(type);
        mShareButton.setTypeface(type);

        //Build the notification service.
        final NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("An error occurred!").setContentText("The error was that you haven't opened this up in ages!").setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true);
        Intent notificationIntent = new Intent(this, FunFactsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(FunFactsActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);


        if(!hasAlreadyRun){
            Resources resources = getResources();
            String[] baseFactArray = resources.getStringArray(R.array.list_base_facts_from_resource_file);
            StringBuilder stringBuilder = new StringBuilder();
            for (String aBaseFactArray : baseFactArray) {
                stringBuilder.append(aBaseFactArray).append(DELIMITER);
            }
            String finalString = stringBuilder.toString();
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            sharedPreferences.edit().remove(FACT_KEY).putString(FACT_KEY, finalString).apply();
            hasAlreadyRun = true;
        }

        final Resources resources = getResources();

        //Display new fact
        SHOW_FACT_BUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousButton.setClickable(true);
                mFavoritesToggleButton.setClickable(true);
                mShareButton.setClickable(true);
                didYouKnow.setVisibility(View.VISIBLE);
                mFavoritesToggleButton.setBackgroundColor(Color.parseColor("#ffffffff"));
                //deal with adverts.
                if (mTapCount % 10 == 0 && mTapCount != 0 && !isAdFree) {
                    MobileCore.showInterstitial(FunFactsActivity.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount++;
                    hasFavorited = false;
                } else {
                    //set up fact screen.
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    backgroundImage.setImageResource(mColorWheel.getBackgroundImage());
                    SHOW_FACT_BUTTON.setTextColor(color);
                    mTapCount++;
                    //get and show fact
                        boolean isNext = true;
                        String fact = FactBook.getRandomFact(FunFactsActivity.this,isNext);
                        FACT_LABEL.setText(fact + resources.getString(R.string.hashtag_append));
                    hasFavorited = false;
                    if (mFavoritesToggleButton.isChecked()) {
                        mFavoritesToggleButton.setChecked(false);
                    }
                }
            }
        });
        //Display previous fact
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFavoritesToggleButton.setBackgroundColor(Color.parseColor("#ffffffff"));
                //deal with adverts.
                if (mTapCount % 10 == 0 && mTapCount != 0 && !isAdFree) {
                    MobileCore.showInterstitial(FunFactsActivity.this, MobileCore.AD_UNIT_SHOW_TRIGGER.BUTTON_CLICK, null);
                    mTapCount++;
                    hasFavorited = false;
                } else {
                    //set up fact screen.
                    int color = mColorWheel.getColor();
                    mRelativeLayout.setBackgroundColor(color);
                    mTapCount++;
                    //get and show fact
                    boolean isNext = false;
                    String fact = FactBook.getRandomFact(FunFactsActivity.this, isNext);
                    FACT_LABEL.setText(fact + resources.getString(R.string.hashtag_append));
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
                    mFavoritesToggleButton.setBackgroundColor(Color.parseColor("#ffa500"));
                    mFavoritesToggleButton.setChecked(true);
                    hasFavorited = true;
                }else{
                    String fFact = FACT_LABEL.getText().toString();
                    FactBook.removeFavoriteFromFavorites(FunFactsActivity.this,fFact);
                    mFavoritesToggleButton.setBackgroundColor(Color.parseColor("#ffffffff"));
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
        if (listString.equals(DEF_VAL)){
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

        //This deals with the user's ad-free status. More to be added as further qualifiers become available.
        if(!isPollFree){
        PollFish.init(this, "c346362d-98b8-4648-9ecc-364d2fb3bbbc", Position.MIDDLE_LEFT, 80);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        hasAnsweredSurvey = sharedPreferences.getBoolean("hasAnsweredSurvey",false);
        isAdFree = sharedPreferences.getBoolean("isAdFree",false);
        isPollFree = sharedPreferences.getBoolean("isPollFree",false);
        numberOfFinishedSurveys = sharedPreferences.getInt("numberOfFinishedSurveys",0);
        }else{
            PollFish.hide();
        }

        //This deals with incoming push messages. If they contain the magic word, the fact is added to the list, and displayed on startUp.
        TextView PushedFactLabel = (TextView) findViewById(R.id.FactTextView);
        ImageView pushedFactBG = (ImageView) findViewById(R.id.facts_background_image);
        Intent intent = getIntent();
        if (intent.hasExtra("pushedFact")) {
            String pushedFact = intent.getStringExtra("pushedFact");
            Resources resources = getResources();
            pushedFactBG.setImageResource(mColorWheel.getBackgroundImage());
            PushedFactLabel.setText(pushedFact + resources.getString(R.string.hashtag_append));
            if(!hasAlreadyRun){
                hasAlreadyRun = true;
            }
        }

        Resources resources = getResources();
        if(!PushedFactLabel.getText().toString().contains(resources.getString(R.string.hashtag))){
            //Disable all but 'next' button, to be enabled after 1st push of next.
            Button previousButton = (Button) findViewById(R.id.button_previous);
            mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.FavoriteShareButton);
            final Button mShareButton = (Button) findViewById(R.id.FavoriteRemoveButton);
            previousButton.setClickable(false);
            mFavoritesToggleButton.setClickable(false);
            mShareButton.setClickable(false);
        }else{
            Button previousButton = (Button) findViewById(R.id.button_previous);
            mFavoritesToggleButton = (ToggleButton) findViewById(com.mike.funfactsTemplateRebuild.R.id.FavoriteShareButton);
            final Button mShareButton = (Button) findViewById(R.id.FavoriteRemoveButton);
            previousButton.setClickable(true);
            mFavoritesToggleButton.setClickable(true);
            mShareButton.setClickable(true);
        }
    }


    @Override
    public void onPollfishSurveyCompleted(boolean b, int i) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.woohoo);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.start();

        //First time round:
        if(!hasAnsweredSurvey && !isPollFree){
            numberOfFinishedSurveys++;
            Resources resources = getResources();
            String preMessage = resources.getString(R.string.alert_content_pollfish_1);
            int numberOfSurveysLeft = numberOfSurveysRequiredToGoAdFree - numberOfFinishedSurveys;
            String message = String.format(preMessage,numberOfSurveysLeft);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(resources.getString(R.string.alert_title_pollfish_1)).setMessage(message).setPositiveButton(resources.getString(R.string.alert_positive_pollfish_1), null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            hasAnsweredSurvey = true;

            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putBoolean("hasAnsweredSurvey",hasAnsweredSurvey).apply();
            sharedPreferencesEditor.putInt("numberOfFinishedSurveys",numberOfFinishedSurveys).apply();
        //Each time round until the set number of polls have been taken.
        }else{
            if((numberOfFinishedSurveys+1) < numberOfSurveysRequiredToGoAdFree && !isPollFree){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            numberOfFinishedSurveys++;
                int numberOfSurveysLeft = numberOfSurveysRequiredToGoAdFree - numberOfFinishedSurveys;
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.remove("numberOfFinishedSurveys").putInt("numberOfFinishedSurveys",
                numberOfFinishedSurveys).apply();
                Resources resources = getResources();
            builder.setTitle(resources.getString(R.string.alert_title_pollfish_interim)).setMessage(String.format(resources.getString(R.string.alert_content_pollfish_interim),numberOfSurveysLeft)).setPositiveButton(resources.getString(R.string.alert_positive_pollfish_interim), null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            //User has reached the required number of polls to go ad-free.
            }else if(!isPollFree){
                Resources resources = getResources();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                numberOfFinishedSurveys++;
                builder.setTitle(resources.getString(R.string.alert_title_pollfish_final)).setMessage(resources.getString(R.string.alert_content_pollfish_final)).setPositiveButton(resources.getString(R.string.alert_positive_pollfish_final), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isAdFree = true;
                        isPollFree = true;
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.remove("isAdFree").putBoolean("isAdFree",isAdFree).remove("isPollFree").putBoolean("isPollFree",isPollFree).apply();
                        //If the user decides to keep the polls:
                    }
                }).setNegativeButton(resources.getString(R.string.alert_negative_pollfish_final), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isAdFree = true;
                        isPollFree = false;
                        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.remove("isAdFree").putBoolean("isAdFree",isAdFree).remove("isPollFree").putBoolean("isPollFree",isPollFree).apply();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        }

    }


    @Override
    public void onPollfishOpened() {
       if(!hasAnsweredSurvey) {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle(R.string.alert_title_pollfish_pre).setMessage(R.string.alert_content_pollfish_pre).setPositiveButton(R.string.alert_positive_pollfish_pre, null);
           AlertDialog dialog = builder.create();
           dialog.show();
       }
    }
}