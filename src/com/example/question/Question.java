package com.example.question;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Question extends Activity {
	final Context context = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question, menu);
        return true;
    }
    
    public void onPressPlay(View view) {
        // Do something in response to button
        	Intent intent = new Intent(this, Play.class);
        	startActivity(intent);
        }

    public void onPressScores(View view) {
        // Do something in response to button
        	Intent intent = new Intent(this, Scores.class);
        	startActivity(intent);
        }
    
    public void onPressCredits(View view) {
        // Do something in response to button
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.credits, null));
        AlertDialog cred = builder.show();
        cred.show();	

        }
    
    public void startSettings() {
        // Do something in response to button
        	Intent intent = new Intent(this, Settings.class);
        	startActivity(intent);
        }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
