package com.example.goutham.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.rundeck.api.RundeckClient;

import java.io.IOException;


public class HomePage extends Activity {

    EditText passwordText, userNameText, URLText;
    ProgressBar progressBar;
    String message = "Android : ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        URLText = (EditText) findViewById(R.id.RundeckURL);
        userNameText = (EditText) findViewById(R.id.RundeckUserName);
        passwordText = (EditText) findViewById(R.id.RundeckPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    public void login(View view) throws IOException {

        progressBar.setVisibility(View.VISIBLE);

        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        //Log.v(message, "UserName :" + userName);
        //Log.v(message, "Password :" + password);

        System.out.println("username :" + userName + " Password :" + password);
        new ExecuteTask().execute(userName, password);
    }

    class ExecuteTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            String response = postData(params);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.VISIBLE);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            if (result.equals("Success")) {
                //Swith to new window

                Intent projectIntent = new Intent(getApplicationContext(), ProjectView.class);
                // Set the request code to any code you like, you can identify the
                // callback via this code
                startActivity(projectIntent);
            }
        }


    }


    public String postData(String[] values) {
        progressBar.setVisibility(View.VISIBLE);
        String response;
        String url = URLText.getText().toString();

        System.out.println("Inside PostData" + values);
        try {

            RundeckClient client = RunDeckClientUtil.getClient(url, values[0], values[1]);
            client.testAuth();

            System.out.println("Project List " + RunDeckClientUtil.getClient().getProjects().get(0).getName());
            System.out.println("Project List " + RunDeckClientUtil.getClient().getProjects().size());
            response = "Success";
        } catch (Exception exception) {
            System.out.println(exception);
            Log.v(message, exception.toString());
            response = exception.toString();
        }
        return response;
    }
}