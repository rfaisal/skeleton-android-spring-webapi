package com.example.skeleton;


import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.example.skeleton.models.Greeting;
import com.example.skeleton.models.GreetingList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GreetingsActivity extends Activity {
	private final String apiBaseUrl = "http://localhost:49811/";
	private final String apiRefreshUrl = apiBaseUrl + "api/greetings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        final Button btnRefresh= (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	    	new AsyncRefreshGreetings().execute();
    	    }
    	});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.greetings, menu);
        return true;
    }
    
    private class AsyncRefreshGreetings extends AsyncTask<Void, Void, ArrayList<Greeting>> {
        @Override
        protected ArrayList<Greeting> doInBackground(Void... params) {
        	try{
    	    	RestTemplate restTemplate = new RestTemplate();
    	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    	        @SuppressWarnings("unchecked")
    			ArrayList<Greeting> greetings = restTemplate.getForObject(apiRefreshUrl, ArrayList.class);
    	        return greetings;
        	}catch(Exception e){
        		Toast.makeText(getApplicationContext(), "Error: " + e.getStackTrace(), Toast.LENGTH_LONG).show();
        	}

            return new ArrayList<Greeting>();
        }

        @Override
        protected void onPostExecute(ArrayList<Greeting> greetings) {            
        	final TextView textViewGreetingsResults = (TextView) findViewById(R.id.textViewGreetingsResults);
	        if(greetings == null || greetings.size() == 0){
	        	textViewGreetingsResults.setText("[None]");
	        }
	        else{
	        	StringBuilder sb = new StringBuilder();
	        	for(Greeting g : greetings){
	        		sb.append(g.getId());
	        		sb.append(": ");
	        		sb.append(g.getContent());
	        		sb.append("\n");
	        	}
	        	textViewGreetingsResults.setText(sb.toString());
	        }
        }

    }
    
}
