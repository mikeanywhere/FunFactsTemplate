package com.mike.funfactsTemplateRebuild;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.List;

public class FileTools {

    protected SharedPreferences.Editor editableSharedPreferences;
    protected final static String PREFS_NAME = "MyPreferencesFile";
    protected final static String FACT_KEY = "Facts";
    protected final static String FAVORITE_KEY = "Favorites";
    protected static String defVal = "something appears to have gone wrong";
    protected static String delimiter = ";;delimiter;;";



    public void addFactsToPrefs(Context context, List<String> array){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.size(); i++){
            stringBuilder.append(array.get(i)).append(delimiter);
        }
        String finalString = stringBuilder.substring(0, stringBuilder.length()-1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("Facts",0);
        editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.clear();
        editableSharedPreferences.putString("Facts", finalString);
        editableSharedPreferences.apply();
    }

    public List<String> getFactsFromPrefs(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Facts",0);
        sharedPreferences.getString(FACT_KEY, defVal);
        String[] factArray = FACT_KEY.split(delimiter);
        List<String> arrayList = Arrays.asList(factArray);
        return arrayList;
    }

    public void addFavoritesToPrefs(Context context,List<String> array){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.size(); i++){
            stringBuilder.append(array.get(i)).append(delimiter);
        }
        String finalString = stringBuilder.toString();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        editableSharedPreferences = sharedPreferences.edit();
        editableSharedPreferences.clear()
                .putString(FAVORITE_KEY, finalString)
                .apply();
    }

    public List<String> getFavoritesFromPrefs(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME,0);
        String favoritesString = sharedPreferences.getString(FAVORITE_KEY, defVal);
        String[] factArray = favoritesString.split(delimiter);
        List<String> arrayList = Arrays.asList(factArray);
        return arrayList;
    }


}
