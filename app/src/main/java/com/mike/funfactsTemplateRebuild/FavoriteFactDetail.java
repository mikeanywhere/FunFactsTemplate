package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public class FavoriteFactDetail extends Activity {

    private final static String DELIMITER = ";;DELIMITER;;";
    private final static String FAVORITE_KEY = "Favorites";
    private final static String PREFS_NAME = "MyPreferencesFile";
    private final static String DEF_VAL = "You don't have any favorites yet!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_fact_detail);

        Button shareButton = (Button) findViewById(R.id.shareButton3);
        ToggleButton favoritesToggle = (ToggleButton) findViewById(R.id.toggleButton2);
        ColorWheel colorWheel = new ColorWheel();
        RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.favFactRelativeLayout);
        TextView favoriteTextView = (TextView) findViewById(R.id.favFactDetailed);
        TextView favoriteNumberTextView = (TextView) findViewById(R.id.factNumber);
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        String favoritesAsString = sharedPreferences.getString(FAVORITE_KEY,DEF_VAL);
        String[] favoritesAsStringArray = favoritesAsString.split(DELIMITER);

        Intent intent = getIntent();
        int position = intent.getIntExtra("itemClicked", 1);
        final String favorite = favoritesAsStringArray[position];

        mRelativeLayout.setBackgroundColor(colorWheel.getColor());
        favoriteTextView.setText(favorite);
        favoriteNumberTextView.setText("#" + (position + 1));
        favoritesToggle.setChecked(true);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, favorite);
                startActivity(Intent.createChooser(intent, "How would you like to send?"));
            }
        });
        favoritesToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
