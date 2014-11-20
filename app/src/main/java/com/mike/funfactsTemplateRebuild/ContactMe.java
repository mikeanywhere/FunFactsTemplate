package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ContactMe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_contact_me);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.mike.funfactsTemplateRebuild.R.menu.contact_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.mike.funfactsTemplateRebuild.R.id.back_to_facts) {
            Intent intent = new Intent(ContactMe.this, FunFactsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_submit) {
            Intent intent = new Intent(ContactMe.this, SubmitFact.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_about) {
            Intent intent = new Intent(ContactMe.this, AboutMe.class);
            startActivity(intent);
            return true;
        }else if (id == com.mike.funfactsTemplateRebuild.R.id.action_favorite) {
            Intent intent = new Intent(ContactMe.this, FavoriteFacts.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
