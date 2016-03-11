package com.example.goutham.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.rundeck.api.RundeckClient;
import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Executor extends Activity {

    ProgressBar progressBar;
    TextView displayStatus;
    //String message = "Android : ";

    String jobID;
    List<RundeckJob> jobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        Intent getIntent = getIntent();
        displayStatus = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        jobID = getIntent.getStringExtra(ProjectView.JOB_KEY);
    }

    public void executeJob(View view) throws IOException {

        progressBar.setVisibility(View.VISIBLE);
        new ExecuteJob().execute(jobID);
        Toast.makeText(getApplicationContext(),jobID, Toast.LENGTH_SHORT).show();
    }

    class ExecuteJob extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            //System.out.println ("Param 0 : " + params[0]);
            //System.out.println ("Job id in Executor class 0 : " + jobID);

            RundeckExecution jobState = RunDeckClientUtil.getClient().runJob(jobID);
            return jobState.getStatus().toString();

        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.VISIBLE);

            if (result == "RUNNING"){
                displayStatus.setText(result);
            }
            else if (result == "SUCCEEDED"){
                displayStatus.setTextColor(Color.rgb(34,167,1));
                displayStatus.setText(result);
            }else if (result == "FAILED"){
                displayStatus.setTextColor(Color.rgb(255, 0, 0));
                displayStatus.setText(result);
            }else{
                displayStatus.setText(result);
            }

            //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            //System.out.println(result);
        }

    }
}
