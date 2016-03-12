package com.example.goutham.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.rundeck.api.domain.RundeckExecution;
import org.rundeck.api.domain.RundeckJob;

import java.io.IOException;
import java.util.List;

public class Executor extends Activity {

    ProgressBar progressBar;
    TextView displayStatus,displayProjectName,displayJobName;
    String result;

    String jobID,projectName,jobName;
    List<RundeckJob> jobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_executor);

        Intent getIntent = getIntent();
        displayStatus = (TextView) findViewById(R.id.textView);
        displayProjectName = (TextView) findViewById(R.id.textView2);
        displayJobName = (TextView) findViewById(R.id.textView3);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.GONE);
        jobID = getIntent.getStringExtra(ProjectView.JOB_KEY);
        projectName = getIntent.getStringExtra(ProjectView.PROJECT_NAME);
        jobName = getIntent.getStringExtra(ProjectView.JOB_NAME);

        displayProjectName.setText("Project : " + projectName);
        displayJobName.setText("Job     : "+ jobName);
    }

    public void executeJob(View view) throws IOException {

        progressBar.setVisibility(View.VISIBLE);
        //progressBarHor.setVisibility(View.VISIBLE);
        new ExecuteJob().execute(jobID);
        Toast.makeText(getApplicationContext(),jobID, Toast.LENGTH_SHORT).show();
    }

    class ExecuteJob extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            //System.out.println ("Param 0 : " + params[0]);
            //System.out.println ("Job id in Executor class 0 : " + jobID);
            result="";
            RundeckExecution jobState = RunDeckClientUtil.getClient().runJob(jobID);

            result = jobState.getStatus().toString();

            //publishProgress(result);
            return result;
        }

        /*@Override
        protected void onProgressUpdate (String... values){
            //progressBarHor.setProgress(values[0]);
            displayStatus.setText(values[0]);
        }*/

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);

            if (result == "SUCCEEDED"){
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
