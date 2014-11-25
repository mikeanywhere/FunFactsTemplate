package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class ContactMe extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_contact_me);

        ImageView contactButton = (ImageView) findViewById(R.id.contact_button);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:mikeanywhere@me.com"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_address_me)});
                int stringId = ContactMe.this.getApplicationInfo().labelRes;
                String appName = ContactMe.this.getString(stringId);
                intent.putExtra(Intent.EXTRA_SUBJECT, "RE: " + appName);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else Toast.makeText(ContactMe.this, "no apps!", Toast.LENGTH_SHORT).show();
            }
        });

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
            Intent intent = new Intent(ContactMe.this, FavoriteFactsList.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
