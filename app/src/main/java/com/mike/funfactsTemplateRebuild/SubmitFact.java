package com.mike.funfactsTemplateRebuild;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class SubmitFact extends Activity {

    //TODO set modifiers for these:
    ParseObject fact;
    String userFact;
    String userEmail;
    protected static EditText eTFact;
    EditText eTEmail;
    Button submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mike.funfactsTemplateRebuild.R.layout.activity_submit_fact);

        submitButton = (Button) findViewById(com.mike.funfactsTemplateRebuild.R.id.button);
        eTFact = (EditText) findViewById(com.mike.funfactsTemplateRebuild.R.id.submitFact);
        eTEmail = (EditText) findViewById(com.mike.funfactsTemplateRebuild.R.id.submitEmail);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userFact = eTFact.getText().toString();
                userEmail = eTEmail.getText().toString().trim();
                if(userFact == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SubmitFact.this);
                    builder.setTitle(com.mike.funfactsTemplateRebuild.R.string.alert_submit_empty_title);
                    builder.setMessage(com.mike.funfactsTemplateRebuild.R.string.alert_submit_empty_main);
                    builder.setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {

                    fact = new ParseObject("Fact");
                    fact.put("fact", userFact);
                    fact.put("email", userEmail);
                    fact.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SubmitFact.this);
                            builder.setTitle(com.mike.funfactsTemplateRebuild.R.string.alert_thanks_title);
                            builder.setMessage(com.mike.funfactsTemplateRebuild.R.string.alert_thanks_main);
                            builder.setPositiveButton("OK", null);
                            AlertDialog alert = builder.create();
                            alert.show();
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

