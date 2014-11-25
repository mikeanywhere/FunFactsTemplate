package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SubmitFact extends Activity {

    private ParseObject fact;
    private String userFact;
    private String userEmail;
    private static EditText eTFact;
    private EditText eTEmail;
    private final static String FACT_KEY = "Facts";
    private final static String DEF_VAL = "You don't have any favorites yet!";
    private final static String DELIMITER = ";;DELIMITER;;";
    private final static String FAVORITE_KEY = "Favorites";
    private final static String PREFS_NAME = "MyPreferencesFile";
    private List<String> favoriteList;
    private List<String> factList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_submit_fact);

        Button submitButton = (Button) findViewById(R.id.button);
        eTFact = (EditText) findViewById(com.mike.funfactsTemplateRebuild.R.id.submitFact);
        eTEmail = (EditText) findViewById(com.mike.funfactsTemplateRebuild.R.id.submitEmail);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        String[] favoriteArray = favoritesAsString.split(DELIMITER);
        favoriteList = new ArrayList<String>(Arrays.asList(favoriteArray)) ;
        String factsAsString = sharedPreferences.getString(FACT_KEY, DEF_VAL);
        String[] factArray = factsAsString.split(DELIMITER);
        factList = new ArrayList<String>(Arrays.asList(factArray)) ;


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFact = eTFact.getText().toString();
                userEmail = eTEmail.getText().toString().trim();
                if (userFact == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmitFact.this);
                    builder.setTitle(com.mike.funfactsTemplateRebuild.R.string.alert_title_oops);
                    builder.setMessage(com.mike.funfactsTemplateRebuild.R.string.alert_error_content_submit_empty_);
                    builder.setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    fact = new ParseObject("Fact");
                    fact.put("fact", userFact);
                    fact.put("email", userEmail);
                    fact.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Thanks dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(SubmitFact.this);
                            builder.setTitle(com.mike.funfactsTemplateRebuild.R.string.alert_title_thanks);
                            builder.setMessage(com.mike.funfactsTemplateRebuild.R.string.alert_content_submit_thanks);
                            builder.setPositiveButton("OK", null);
                            AlertDialog alert = builder.create();
                            alert.show();

                            //Start adding submitted fact to prefs for favorites AND facts.
                            //open preferences file
                            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //pull prefs file, and append Favorites.
                            StringBuilder stringBuilder = new StringBuilder();
                            for (String aFavoriteList : favoriteList) {
                                stringBuilder.append(aFavoriteList).append(DELIMITER);
                            }
                            stringBuilder.append(userFact).append(DELIMITER);
                            String finalString = stringBuilder.toString();
                            editor.remove(FAVORITE_KEY).putString(FAVORITE_KEY, finalString).apply();

                            //Reset StringBuilder
                            stringBuilder.delete(0, stringBuilder.length());
                            //pull prefs file, and append Facts.
                            for (String aFactList : factList) {
                                stringBuilder.append(aFactList).append(DELIMITER);
                            }
                            stringBuilder.append(userFact).append(DELIMITER);
                            finalString = stringBuilder.toString();
                            editor.remove(FACT_KEY).putString(FACT_KEY, finalString).apply();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.mike.funfactsTemplateRebuild.R.menu.submit_fact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
            if (id == com.mike.funfactsTemplateRebuild.R.id.back_to_facts) {
                Intent intent = new Intent(SubmitFact.this, FunFactsActivity.class);
                startActivity(intent);
                return true;
            }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_contact) {
                Intent intent = new Intent(SubmitFact.this, ContactMe.class);
                startActivity(intent);
                return true;
            }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
                Intent intent = new Intent(SubmitFact.this, AboutMe.class);
                startActivity(intent);
                return true;
            }
        return super.onOptionsItemSelected(item);
        }

}

