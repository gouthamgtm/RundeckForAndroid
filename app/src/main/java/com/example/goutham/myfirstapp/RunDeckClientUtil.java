package com.example.goutham.myfirstapp;

import org.rundeck.api.RundeckClient;

public class RunDeckClientUtil {
    //use only one client for session handling
    private static RundeckClient client = null;

        public static RundeckClient getClient(String url, String userName, String password) {

        client = new RundeckClient(url, userName, password);
        return client;
    }

    public static RundeckClient getClient() {
        return client;
    }
}
