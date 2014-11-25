package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactBook extends Activity {

    private final static String FACT_KEY = "Facts";
    private final static String DEF_VAL = "You don't have any favorites yet!";
    private final static String DELIMITER = ";;DELIMITER;;";
    private final static String FAVORITE_KEY = "Favorites";
    private final static String PREFS_NAME = "MyPreferencesFile";
    private static int factNumber = 0;
    static final List<String> favorites = new ArrayList<String>();

    //TODO can revert back to just before 22:25, when the change was made.

    static String getRandomFact(Context context){

            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String listString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
            String[] listStringArray = listString.split(DELIMITER);
            String fact;
            if(listString.isEmpty()){
                fact = "You've favorited all the facts! Updates coming soon... in the meantime, please add more via the 'submit a fact' option in the menu!";
                return fact;
            }
            if(factNumber < listStringArray.length-1){
                factNumber++;
            }else{
                factNumber = 0;
            }
            fact = listStringArray[factNumber];
            return fact;
    }

    static List<String> savedFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        String prefFavorites = sharedPreferences.getString(FAVORITE_KEY, DEF_VAL);
        String [] factArray = prefFavorites.split(DELIMITER);
        List<String> arrayList = Arrays.asList(factArray);
        return arrayList;
    }

    static void addFactToFavorites(Context context, String fact) {
        favorites.add(fact);
        StringBuilder stringBuilder = new StringBuilder();
        for (String favorite : favorites) {
            stringBuilder.append(favorite).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.remove(FAVORITE_KEY)
                .putString(FAVORITE_KEY, finalString)
                .apply();
        FactBook.removeFactFromFactList(context.getApplicationContext(), fact);
    }
    private static void removeFactFromFactList(Context context, String fact) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        String savedFactsString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
        String[] savedFactsAsArray = savedFactsString.split(DELIMITER);
        ArrayList<String> savedFactsAsList = new ArrayList<String>(Arrays.asList(savedFactsAsArray));
        savedFactsAsList.remove(fact);
        savedFactsAsList.trimToSize();
        StringBuilder stringBuilder = new StringBuilder();
        for (String aSavedFactsAsList : savedFactsAsList) {
            stringBuilder.append(aSavedFactsAsList).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        editableSharedPreferences.putString(FACT_KEY, finalString)
                .apply();
    }
    private static void addFactToFactList(Context context, String fact) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        String savedFactsString = sharedPreferences.getString(FACT_KEY,DEF_VAL);
        String[] savedFactsAsArray = savedFactsString.split(DELIMITER);
        ArrayList<String> savedFactsAsList = new ArrayList<String>(Arrays.asList(savedFactsAsArray));
        savedFactsAsList.add(fact);
        savedFactsAsList.trimToSize();
        StringBuilder stringBuilder = new StringBuilder();
        for (String aSavedFactsAsList : savedFactsAsList) {
            stringBuilder.append(aSavedFactsAsList).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        editableSharedPreferences.remove(FACT_KEY)
                .putString(FACT_KEY, finalString)
                .apply();
    }
    static void removeFavoriteFromFavorites(Context context, String fact) {
        favorites.remove(fact);
        StringBuilder stringBuilder = new StringBuilder();
        for (String favorite : favorites) {
            stringBuilder.append(favorite).append(DELIMITER);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.putString(FAVORITE_KEY, finalString)
                .apply();
        addFactToFactList(context.getApplicationContext(),fact);
    }
}


