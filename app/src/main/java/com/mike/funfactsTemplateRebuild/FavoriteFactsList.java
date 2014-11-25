package com.mike.funfactsTemplateRebuild;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class FavoriteFactsList extends ListActivity {

    private String[] favoriteArray;
    private final static String PREFS_NAME = "MyPreferencesFile";
    private final static String FAVORITE_KEY = "Favorites";
    private final static String DEF_VAL = "You have no favorites yet!";
    private final static String DELIMITER = ";;delimiter;;";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_favorite_facts_list);


        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        favoriteArray = favoritesAsString.split(DELIMITER);
        List<String> list = Arrays.asList(favoriteArray);
        ListView mainListView = getListView();

        if(list.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoriteFactsList.this, android.R.layout.simple_list_item_1, FactBook.favorites);
            setListAdapter(adapter);
        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FavoriteFactsList.this, android.R.layout.simple_list_item_1, FactBook.savedFavorites(this));
            setListAdapter(adapter);
        }

        //TODO if swipe type doesn't work out, switch back to original FavoriteFactsDetail.class
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FavoriteFactsList.this, FavoriteFactDetail.class);
                intent.putExtra("Favorites", favoriteArray);
                intent.putExtra("itemClicked", i);
                startActivity(intent);
            }
        });
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
            Intent intent = new Intent(FavoriteFactsList.this, FunFactsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_contact) {
            Intent intent = new Intent(FavoriteFactsList.this, ContactMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_submit) {
            Intent intent = new Intent(FavoriteFactsList.this, SubmitFact.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
            Intent intent = new Intent(FavoriteFactsList.this, AboutMe.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
