package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FactBook extends Activity {

    protected final static String FACT_KEY = "Facts";
    protected final static String DEF_VAL = "You don't have any favorites yet!";
    protected final static String DELIMITER = ";;DELIMITER;;";
    protected final static String FAVORITE_KEY = "Favorites";
    protected final static String PREFS_NAME = "MyPreferencesFile";
    protected static int factNumber = 0;

    protected static List<String> favorites = new ArrayList<String>();
    protected static List<String> facts = new ArrayList<String>(Arrays.asList("aFact1", "aFact2", "aFact3"));

    protected static String getRandomFact(Context context, boolean getFromBase){
        if(!getFromBase){
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String listString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
            String[] listStringArray = listString.split(DELIMITER);
            facts = Arrays.asList(listStringArray);
            String fact = "";
            //TODO get this working in conjunction with the initial shake-up on FunFactsActivity. Somewhere down the line,
            //TODO the preference file is being deleted.
//            String fact = facts.get(factNumber);
//            if(factNumber <= facts.size()){
//                factNumber++;
//            }else{
//                factNumber = 0;
//            }
            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(facts.size());
            fact = facts.get(randomNumber);
            return fact;
        }else{
            String fact = "";
            Random randomGenerator = new Random();
            int randomNumber = randomGenerator.nextInt(facts.size());
            fact = facts.get(randomNumber);
            return fact;
        }
    }

    protected static List<String> savedFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        String prefFavorites = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        String [] factArray = prefFavorites.split(DELIMITER);
        List<String> arrayList = Arrays.asList(factArray);
        return arrayList;
    }

    protected static void removeFactFromFactList(Context context, String fact) {
        facts.remove(fact);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < facts.size(); i++){
            stringBuilder.append(facts.get(i)).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.putString(FACT_KEY, finalString)
                .apply();
    }

    protected static void addFactToFavorites(Context context, String fact) {
        favorites.add(fact);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < favorites.size(); i++){
            stringBuilder.append(favorites.get(i)).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.remove(FAVORITE_KEY).remove(FAVORITE_KEY)
                .putString(FAVORITE_KEY, finalString)
                .apply();
        removeFactFromFactList(context.getApplicationContext(), fact);
    }
}


