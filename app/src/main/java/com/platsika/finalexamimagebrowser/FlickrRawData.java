package com.platsika.finalexamimagebrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Pliats on 3/7/2017.
 */

//Enum which will hold all the possible states
enum DownloadStatus {
    IDLE, PROCESSING, NOT_INIT, FAILED_OR_NULL, OK
};

class FlickrRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "FlickrRawData";
    private final OnDownloadComplete mCallback;

    interface OnDownloadComplete {
        void onDownloadComplete(String data, DownloadStatus status);
    }

    private DownloadStatus mDownloadStatus;

    public FlickrRawData(OnDownloadComplete callback) {
        mDownloadStatus = DownloadStatus.IDLE;
        mCallback = callback;
    }


    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "onPostExecute: parameter ::" + s);
        if (mCallback != null) {
            mCallback.onDownloadComplete(s, mDownloadStatus);
        }
        Log.d(TAG, "onPostExecute: End");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection con = null;
        BufferedReader reader = null;

        //Check if a URL has been pashed in to get data.
        //Not
        if (params == null) {
            mDownloadStatus = DownloadStatus.NOT_INIT;
            return null;
        }

        try {
            //If parameters exist the we are on a state or Processing the URL
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            //initializing an HTTP Connection using an HTTP GET method
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            //Saving the HTTP Response Code for later use and debug.
            int resp = con.getResponseCode();
            Log.d(TAG, "doInBackground: Response Code was ::" + resp);

            //Builder to concat results from inputStream
            StringBuilder result = new StringBuilder();
            //Creation of input Stream to get data from the HTTP Connection
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            //Reading one line at a time and appending the new line char
            //Loop stops when no more lines exists(null)
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            //If we reach this point it means all data requested have been delivered.Status == OK
            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: IO Exception Reading data" + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground:  Security Exception." + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing Con " + e.getMessage());
                }
            }
        }

        return null;
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: Called");
        if(mCallback != null){
            String res = doInBackground(s);
            mCallback.onDownloadComplete(res,mDownloadStatus);
        }
        Log.d(TAG, "runInSameThread: Ended");
    }
}
