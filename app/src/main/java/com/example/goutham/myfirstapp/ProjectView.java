package com.example.goutham.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import org.rundeck.api.domain.RundeckJob;
import org.rundeck.api.domain.RundeckProject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectView extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader,listProjects,listJobsNames,listJobId;
    HashMap<String, List<String>> listDataChild,listJobs;
    HashMap<String, String> listJobNameID;
    //public final static String PROJECT_KEY = "com.example.goutham.PROJECT_KEY";
    public final static String JOB_KEY = "com.example.goutham.JOB_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_view);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
            getData();
    }

    public void getData() {
        RetrieveProjectDetails objRPD = new RetrieveProjectDetails();
        objRPD.execute("success");
    }

    class RetrieveProjectDetails extends AsyncTask<String, Void,String> {

        protected String doInBackground(String... params) {

            listProjects = new ArrayList<String>();
            //listJobsNames = new ArrayList<String>();
            listJobs = new HashMap<String, List<String>>();
            listJobNameID =  new HashMap<String, String>();

            List<RundeckProject> project = RunDeckClientUtil.getClient().getProjects();


            for (int i = 0; i < (project.size()); i++) {
                System.out.println("Project List " + project.get(i).getName());
                listProjects.add(project.get(i).getName());
            }

            for (int i = 0; i < (listProjects.size()); i++) {
                String projectJob=listProjects.get(i);
                System.out.println("Job List" + RunDeckClientUtil.getClient().getJobs(listProjects.get(i)).get(i).getName());
                List<RundeckJob> jobs = RunDeckClientUtil.getClient().getJobs(project.get(i).getName());
                listJobsNames = new ArrayList<String>();
                listJobId = new ArrayList<String>();

                for (int j = 0;j<jobs.size();j++){
                    String jobName=jobs.get(j).getName();
                    listJobsNames.add(jobs.get(j).getName());
                   // listJobId.add(jobs.get(j).getId());
                    listJobNameID.put(projectJob+jobName, jobs.get(j).getId());
                }

                //listJobs.put(listProjects.get(i), listJobsNames);

                listJobs.put(listProjects.get(i),listJobsNames);
            }

            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String dummy) {
            prepareListData();
            System.out.println("inside postExecute" + listProjects.toString());
            createView();
        }
    }

    public void createView(){
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                String projectName = listDataHeader.get(groupPosition).toString();
                String jobName= listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).toString();
                String jobId="";
                if(!listJobNameID.isEmpty())
                {
                    jobId=listJobNameID.get(projectName+jobName);
                    System.out.println("Job Id value " + jobId);
                }
                Intent projectIntent = new Intent(getApplicationContext(), Executor.class);
                //projectIntent.putExtra(PROJECT_KEY, projectName);
                projectIntent.putExtra(JOB_KEY, jobId);
                System.out.println("outside loop Job Id value " + jobId);
                startActivity(projectIntent);
                //System.out.println("On Click" + RunDeckClientUtil.getClient().getJobs(listProjects.get(groupPosition)).get(childPosition).getName().toString());


                return false;
            }
        });

    }
    public void prepareListData() {

        listDataHeader = new ArrayList<String>();


        listDataChild = new HashMap<String, List<String>>();
        // Adding child data

        System.out.println("Returned from Async Task" + listProjects);
        System.out.println("Returned from Async Task" + listProjects.size());

        listDataHeader = listProjects;
        listDataChild.putAll(listJobs);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_activity2, menu);
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
}
