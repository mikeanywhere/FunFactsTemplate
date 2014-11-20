package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class FactBook extends Activity {

    protected Context context = getApplication();
    protected static SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editableSharedPreferences;
    protected final String FACT_KEY = "Facts";
    protected final static String DEF_VAL = "something appears to have gone wrong";
    protected final static String DELIMITER = ";;DELIMITER;;";
    protected final static String FAVORITE_KEY = "Favorites";
    protected final static String PREFS_NAME = "MyPreferencesFile";


    protected static List<String> favorites = new ArrayList<String>();
    protected static List<String> aFacts = new ArrayList<String>(Arrays.asList("aFact1", "aFact 2", "aFact 3"));


    protected static List<String> savedFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        String prefFavorites = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        String [] factArray = prefFavorites.split(DELIMITER);
        List<String> arrayList = Arrays.asList(factArray);
        return arrayList;
    }
    protected static String[] savedFavoritesStringArray(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        String prefFavorites = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        String[] factArray = prefFavorites.split(DELIMITER);
        return factArray;
    }

    protected static String getBaseRandomFact() {
        String fact = "";
        Random randomGenerator = new Random();
        int randomNumber = randomGenerator.nextInt(aFacts.size());
        fact = aFacts.get(randomNumber);
        return fact;
    }

    protected static void removeFact(String fact) {
        aFacts.remove(fact);
    }

    protected static void addToFavorites(Context context,String fact) {
        favorites.add(fact);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < favorites.size(); i++){
            stringBuilder.append(favorites.get(i)).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.clear()
                .putString(FAVORITE_KEY, finalString)
                .apply();
    }
}


