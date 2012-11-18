package com.example.question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Scores extends Activity {

	boolean friends = false;
	Context context = this;
	String responseString;

	// boolean cool = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_scores);

		TabHost host = (TabHost) findViewById(R.id.tabhost);
		host.setup();

		TabSpec spec = host.newTabSpec("Local");
		spec.setIndicator(getResources().getText(R.string.local));
		spec.setContent(R.id.tab1);
		host.addTab(spec);

		spec = host.newTabSpec("Friends");
		spec.setIndicator(getResources().getText(R.string.friends));
		spec.setContent(R.id.tab2);
		host.addTab(spec);

		host.setCurrentTabByTag("Local");

		host.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId == "Local")
					friends = false;
				else if (tabId == "Friends")
					friends = true;
			}
		});

		TableLayout localTable = (TableLayout) findViewById(R.id.local_table);

		Cursor c;
		QuestionDB qDB = new QuestionDB(this);
		SQLiteDatabase db = qDB.getReadableDatabase();
		if (db != null) {

			String[] campos = new String[] { "name", "score" };
			String order = new String("score DESC");
			c = db.query("tabla", campos, null, null, null, null, order);
			if (c.moveToFirst()) {

				do {
					String name = c.getString(0);
					int score = c.getInt(1);
					String sco = Integer.toString(score);

					TextView sc = new TextView(this);
					TextView nm = new TextView(this);
					nm.setText(name);
					sc.setText(sco);
					TableRow row = new TableRow(this);
					row.addView(nm);
					row.addView(sc);
					row.setBackgroundColor(0xFFFFFFFF);
					TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
							TableLayout.LayoutParams.WRAP_CONTENT,
							TableLayout.LayoutParams.WRAP_CONTENT);

					int leftMargin = 2;
					int topMargin = 1;
					int rightMargin = 2;
					int bottomMargin = 1;

					tableRowParams.setMargins(leftMargin, topMargin,
							rightMargin, bottomMargin);

					row.setLayoutParams(tableRowParams);
					localTable.addView(row);

				} while (c.moveToNext());
			}
			c.close();
		}

		db.close();


			Async fr = new Async();
			fr.execute(null, null, null);

	}

	// /////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////
	public class Async extends AsyncTask<Void, Void, Void> {

		boolean cool = true;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpParams httpParameters = new BasicHttpParams();

			int timeoutConnection = 1500;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

			int timeoutSocket = 1500;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name", "nidebo"));
			HttpGet request = new HttpGet(
					"http://soletaken.disca.upv.es:8080/WWTBAM/rest/highscores?name=nidebo");
			request.setHeader("Accept", "application/json");
			BasicHttpResponse response;
			// HttpResponse response;
			try {
				response = (BasicHttpResponse) client.execute(request);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream stream = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(stream));
					StringBuilder sb = new StringBuilder();
					String line = null;
					line = reader.readLine();
					sb.append(line);
					stream.close();
					responseString = sb.toString();

				}
			} catch (ConnectTimeoutException e) {
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

			return null;
		}

		protected void onPostExecute(Void result) {
			// terminar progress
			Scores.this.setProgressBarIndeterminateVisibility(false);

			if (cool) {
				try {
					GsonBuilder builder = new GsonBuilder();
					Gson gson = builder.create();

					JSONObject json = new JSONObject(responseString);
					HighScoreList hsl = gson.fromJson(json.toString(),
							HighScoreList.class);

					TableLayout friendsTable = (TableLayout) findViewById(R.id.friends_table);

					for (int i = 0; i < hsl.getScores().size(); i++) {

						String name_f = hsl.getScores().get(i).getName();
						int score_f = hsl.getScores().get(i).getScoring();
						String sco_f = Integer.toString(score_f);
						TextView sc_f = new TextView(context);
						TextView nm_f = new TextView(context);
						nm_f.setText(name_f);
						sc_f.setText(sco_f);
						TableRow row = new TableRow(context);
						row.addView(nm_f);
						row.addView(sc_f);
						row.setBackgroundColor(0xFFFFFFFF);
						TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
								TableLayout.LayoutParams.WRAP_CONTENT,
								TableLayout.LayoutParams.WRAP_CONTENT);

						int leftMargin = 2;
						int topMargin = 1;
						int rightMargin = 2;
						int bottomMargin = 1;

						tableRowParams.setMargins(leftMargin, topMargin,
								rightMargin, bottomMargin);

						row.setLayoutParams(tableRowParams);
						friendsTable.addView(row);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(getResources().getText(R.string.oops));
				builder.setMessage(getResources()
						.getText(R.string.scores_error));
				AlertDialog timeover = builder.show();
				timeover.show();
			}
		}

		protected void onPreExecute() {
			Scores.this.setProgressBarIndeterminate(true);
			Scores.this.setProgressBarIndeterminateVisibility(true);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_scores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.local_delete:
			deleteLocal();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void deleteLocal() {

		TableLayout localtable = (TableLayout) findViewById(R.id.local_table);
		localtable.removeAllViews();
		// borrar tablas!

		QuestionDB dbh = new QuestionDB(this);
		SQLiteDatabase db = dbh.getWritableDatabase();
		if (db != null) {
			db.execSQL("DROP TABLE IF EXISTS " + QuestionDB.TABLE);
			dbh.onCreate(db);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (friends)
			menu.getItem(0).setEnabled(false);
		else
			menu.getItem(0).setEnabled(true);
		return true;
	}

}
