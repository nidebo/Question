package com.example.question;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class Settings extends Activity {

	final int mode = Activity.MODE_PRIVATE;
	public static final String myPrefs = "prefs";
	String name;
	String friend;
	Context context = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.HelpTimes, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		SharedPreferences mySharedPreferences = getSharedPreferences(myPrefs,
				mode);

		if (mySharedPreferences != null) {
			showSavedPreferences();
		}

	}

	@Override
	protected void onPause() {

		savePreferences();
		super.onPause();

	}

	protected void savePreferences() {
		// comprobar si se ha metido un nombre??
		SharedPreferences mySharedPreferences = getSharedPreferences(myPrefs,
				mode);
		SharedPreferences.Editor myEditor = mySharedPreferences.edit();

		EditText usr = (EditText) findViewById(R.id.username);
		String user = usr.getText().toString();
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		String helps = spinner.getSelectedItem().toString();

		myEditor.putString("username", user);
		myEditor.putString("helps", helps);
		myEditor.commit();
	}

	public void showSavedPreferences() {

		SharedPreferences mySharedPreferences = getSharedPreferences(myPrefs,
				mode);
		String username = mySharedPreferences.getString("username", "");
		String helps = mySharedPreferences.getString("helps", "1");
		EditText usr = (EditText) findViewById(R.id.username);
		usr.setText(username);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setSelection(Integer.parseInt(helps));
	}

	public void onAddFriendClick(View view) {

		// comprobar que se ha metido un nombre en user y en add friend!

		// obtener nombres de los editText de Settings...
		EditText nm = (EditText) findViewById(R.id.username);
		EditText frnd = (EditText) findViewById(R.id.friend_name);
		name = nm.getText().toString();
		friend = frnd.getText().toString();
		
		//if hay texto en friends
		if(!friend.equals("")){
			frnd.setText("");
			
			boolean cool = true;
			
			HttpParams httpParameters = new BasicHttpParams();

			int timeoutConnection = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);

			int timeoutSocket = 5000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpPost request = new HttpPost(
					"http://soletaken.disca.upv.es:8080/WWTBAM/rest/friends");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name", name));
			pairs.add(new BasicNameValuePair("friend_name", friend));
			try {
				request.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				HttpResponse response = (BasicHttpResponse) client.execute(request);
			} catch(ConnectTimeoutException e) {
				cool = false;
				e.printStackTrace(); 
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					cool = false;
					e.printStackTrace();
				} catch (IOException e) {
					cool = false;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			if (cool) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(getResources().getText(R.string.new_friend_title));
				builder.setMessage(friend + " " + getResources()
						.getText(R.string.new_friend));
				AlertDialog timeover = builder.show();
				timeover.show();
			}

			else {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(getResources().getText(R.string.oops));
				builder.setMessage(getResources()
						.getText(R.string.friend_error));
				AlertDialog timeover = builder.show();
				timeover.show();
			}

			
		}
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(getResources().getText(R.string.oops));
			builder.setMessage(getResources()
					.getText(R.string.empty_friend_error));
			AlertDialog timeover = builder.show();
			timeover.show();
			
		}
		

	}



	

	

}
