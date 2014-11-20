package com.mike.funfactsTemplateRebuild;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.ironsource.mobilcore.MobileCore;

import java.util.Arrays;
import java.util.List;

public class FavoriteFacts extends ListActivity {

    protected SharedPreferences sharedPreferences;
    protected List<String> list;
    protected String[] factArray;
    protected String favoritesAsString;
    protected final static String PREFS_NAME = "MyPreferencesFile";
    protected final static String FAVORITE_KEY = "Favorites";
    protected final static String DEF_VAL = "You have no favorites yet!";
    protected final static String DELIMITER = ";;delimiter;;";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_favorite_facts);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        favoritesAsString = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        factArray = favoritesAsString.split(DELIMITER);
        list = Arrays.asList(factArray);

        if(list.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoriteFacts.this, android.R.layout.simple_list_item_1, FactBook.favorites);
            setListAdapter(adapter);
        }else{
            sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            favoritesAsString = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
            factArray = favoritesAsString.split(DELIMITER);
            list = Arrays.asList(factArray);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoriteFacts.this, android.R.layout.simple_list_item_1, FactBook.savedFavorites(this));
            setListAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.mike.funfactsTemplateRebuild.R.menu.favorite_facts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.mike.funfactsTemplateRebuild.R.id.back_to_facts) {
            Intent intent = new Intent(FavoriteFacts.this, FunFactsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_contact) {
            Intent intent = new Intent(FavoriteFacts.this, ContactMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_submit) {
            Intent intent = new Intent(FavoriteFacts.this, SubmitFact.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
            Intent intent = new Intent(FavoriteFacts.this, AboutMe.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
