package com.example.question;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Play extends Activity {

	int currentq;
	int helps;
	int maxQ = 15;
	int helpused;
    final Context context = this;
    SharedPreferences mySharedPreferences;
    SharedPreferences.Editor edit;
	List<QClass> questionList = new ArrayList<QClass>();
	List<Button> bots = new ArrayList<Button>();
    TextView txt;
    TextView pFor;
    TextView nQues;
	String[] scores = {"100", "200", "300", "500", "1000", "2000", "4000", "8000", "16000", "32000",
			"64000", "12500", "250000", "500000", "1000000"};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        
        ///////LA POSICION 0 DE LA LISTA ESTA VACIA :S
        mySharedPreferences = getSharedPreferences("prefs",Activity.MODE_PRIVATE);
        edit = mySharedPreferences.edit();
        txt = (TextView) findViewById(R.id.question_text);
        pFor = (TextView) findViewById(R.id.ValPLay);
        nQues = (TextView) findViewById(R.id.ValNQuest);
        Button ans1 = (Button) findViewById(R.id.answer1);
        Button ans2 = (Button) findViewById(R.id.answer2);
        Button ans3 = (Button) findViewById(R.id.answer3);
        Button ans4 = (Button) findViewById(R.id.answer4);
        bots.add(ans1);
        bots.add(ans2);
        bots.add(ans3);
        bots.add(ans4);       
        
     	String hlps = mySharedPreferences.getString("helps", "3");
     	helps = Integer.parseInt(hlps);
     	currentq = mySharedPreferences.getInt("currentq", 1);
     	helpused = mySharedPreferences.getInt("helpused", 0);
        
        prepareQuestions();
        setQuestions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_play, menu);
        return true;
    }

    
    @Override
    public void onBackPressed() {
       Log.d("CDA", "onBackPressed Called");
       currentq = 1;
       helpused = 0;
       edit.putInt("currentq", currentq);
       edit.putInt("helpused", helpused);
       edit.commit();
       System.exit(1); 
    }    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	if(helps-helpused > 0){
        switch (item.getItemId()) {
            case R.id.m_phone:
                /////
            	bots.get((questionList.get(currentq).getPhone())-1).setBackgroundResource(R.color.BlackBlue);
            	item.setEnabled(false);
                helpused++;
                edit.putInt("helpused", helpused);
                edit.commit();
                return true;
            case R.id.m_off50:
            	/////
                bots.get((questionList.get(currentq).getFifty1())-1).setEnabled(false);
                bots.get((questionList.get(currentq).getFifty2())-1).setEnabled(false);
                item.setEnabled(false);
                helpused++;
                edit.putInt("helpused", helpused);
                edit.commit();
                
                return true;
            case R.id.m_audience:
            	/////
            	bots.get((questionList.get(currentq).getAudience())-1).setBackgroundResource(R.color.Green);
            	item.setEnabled(false);
                helpused++;
                edit.putInt("helpused", helpused);
                edit.commit();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    	}

    	return true;
    }
    
    
    
    public void enviaPuntos(){
    	SharedPreferences mySharedPreferences = getSharedPreferences("prefs",Activity.MODE_PRIVATE);
     	String usr = mySharedPreferences.getString("username", "---");
     	boolean cool = true;
		
		HttpParams httpParameters = new BasicHttpParams();

		int timeoutConnection = 5000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);

		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient client = new DefaultHttpClient(httpParameters);
    	HttpPut request = new HttpPut("http://soletaken.disca.upv.es:8080/WWTBAM/rest/highscores"); 
    	List<NameValuePair> pairs = new ArrayList<NameValuePair>();
    	pairs.add(new BasicNameValuePair("name", usr));
    	//// puntos los que se hayan conseguido
    	pairs.add(new BasicNameValuePair("scoring", scores[currentq-1])); 
    	try {
			request.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	try {
			HttpResponse response = client.execute(request);
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
		
		if (!cool) {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(getResources().getText(R.string.oops));
			builder.setMessage(getResources()
					.getText(R.string.upload_error));
			AlertDialog timeover = builder.show();
			timeover.show();
		} 

    }
    public void guardaPuntos(){
        QuestionDB dbh = new QuestionDB(this);
        SQLiteDatabase db = dbh.getWritableDatabase();
     if(db != null){           
    	SharedPreferences mySharedPreferences = getSharedPreferences("prefs",Activity.MODE_PRIVATE);
     	String username = mySharedPreferences.getString("username", "");
        ContentValues cv = new ContentValues();
        cv.put(QuestionDB.NAME, username);
        int sc = Integer.parseInt(scores[currentq-1]);
        cv.put(QuestionDB.SCORE, sc);        
                 
        db.insert("tabla", QuestionDB.NAME, cv);
        db.close();
     }
    }

    
    public void prepareQuestions() {
    	InputStream inputStream;  

		try {
			inputStream = getResources().openRawResource(R.raw.question0001);
			

		
			XmlPullParser parser;
			parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(inputStream, null);

		
    	int eventType = XmlPullParser.START_DOCUMENT;
    	 
    	while (eventType != XmlPullParser.END_DOCUMENT) { 
    	  if (eventType == XmlPullParser.START_TAG) { 
    		  

    	    String answer1 = parser.getAttributeValue(null, "answer1"); 
    	    String answer2 = parser.getAttributeValue(null, "answer2");
    	    String answer3 = parser.getAttributeValue(null, "answer3");
    	    String answer4 = parser.getAttributeValue(null, "answer4");
    	    String audience = parser.getAttributeValue(null, "audience");
    	    String fifty1 = parser.getAttributeValue(null, "fifty1");
    	    String fifty2 = parser.getAttributeValue(null, "fifty2");
    	    String number = parser.getAttributeValue(null, "number");
    	    String phone = parser.getAttributeValue(null, "phone");
    	    String right = parser.getAttributeValue(null, "right");
    	    String text = parser.getAttributeValue(null, "text");
    	    
    	    QClass pregunta = new QClass(number,text,answer1,answer2,answer3,answer4,right,audience,phone,fifty1,fifty2);
    	    questionList.add(pregunta);

    	  } 
			eventType = parser.next();

    	} 
    	inputStream.close(); 
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    	
    }

    public void setButtons(){
        
        bots.get(0).setText(questionList.get(currentq).getAnswer1());
        bots.get(1).setText(questionList.get(currentq).getAnswer2());
        bots.get(2).setText(questionList.get(currentq).getAnswer3());
        bots.get(3).setText(questionList.get(currentq).getAnswer4());
        txt.setText(questionList.get(currentq).getText());   
        nQues.setText(Integer.toString(currentq));
        pFor.setText(scores[currentq-1]);
        bots.get(0).setEnabled(true);
        bots.get(1).setEnabled(true);
        bots.get(2).setEnabled(true);
        bots.get(3).setEnabled(true);

        bots.get(0).setBackgroundResource(R.color.button_color);    
        bots.get(1).setBackgroundResource(R.color.button_color);    
        bots.get(2).setBackgroundResource(R.color.button_color);    
        bots.get(3).setBackgroundResource(R.color.button_color);    
    }
    

    public void setQuestions(){
      		setButtons();
    		int buena = questionList.get(currentq).getRight();
    		
            for (int i = 0; i < bots.size(); i++) {      	
            	if((i+1) == buena){
            		bots.get(i).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Perform action on click
                    	// TODO Auto-generated method stub
                        currentq++;                               
                        edit.putInt("currentq", currentq);
                        edit.commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.corr);
                        builder.setMessage(R.string.youright);
                        builder.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface builder, int id) {

                               setQuestions();
                            }  
                        });
                        builder.setNegativeButton(R.string.leave, new DialogInterface.OnClickListener() {  
                            public void onClick(DialogInterface builder, int id) {
                                guardaPuntos();
                                enviaPuntos();
                            	currentq = 1;
                            	helpused = 0;
                            	edit.putInt("currentq", currentq);
                            	edit.putInt("helpused", helpused);
                                edit.commit();
                            	System.exit(1);
                            }  
                        });
                        AlertDialog timeover = builder.show();
                        timeover.show();
                    }
                });
            	}
            	else {
            		bots.get(i).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // Perform action on click
                           	if(currentq == 5 || currentq == 10){
                        		guardaPuntos();
                        		enviaPuntos();
                        	}
                        	currentq = 1;
                        	helpused = 0;
                        	edit.putInt("currentq", currentq);
                        	edit.putInt("helpused", helpused);
                            edit.commit();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(R.string.incor);
                            builder.setMessage(R.string.yournot);
                            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface builder, int id) {
 
                                    System.exit(1);  
                                }  
                            });
                            AlertDialog timeover = builder.show();
                            timeover.show();
                            }
                    });
            	}    	   		   		
            }  	      		  			
    		if(currentq == 15){
    			AlertDialog.Builder builder = new AlertDialog.Builder(context);
    			builder.setTitle(R.string.grats);
    			builder.setMessage(R.string.win);
    			guardaPuntos();
    			enviaPuntos();
            	currentq = 1;
            	helpused = 0;
            	edit.putInt("currentq", currentq);
            	edit.putInt("helpused", helpused);
                edit.commit();
    			builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {  
                                public void onClick(DialogInterface builder, int id) {

                                    System.exit(1);  
                                }  
                });
                AlertDialog timeover = builder.show();
                timeover.show();
                }
    	}	
}
