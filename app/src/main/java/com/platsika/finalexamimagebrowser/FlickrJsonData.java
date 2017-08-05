package com.platsika.finalexamimagebrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pliats on 13/7/2017.
 */

class FlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements FlickrRawData.OnDownloadComplete {
    private static final String TAG ="GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private boolean mMatchAll;

    private final OnDataAvailable mCallBack;
    private boolean runOnSameTh = false;

    interface OnDataAvailable{
        void onDataAvailable(List<Photo> data,DownloadStatus status);
    }

    public FlickrJsonData(OnDataAvailable callBack, boolean matchAll, String language, String baseURL) {
        Log.d(TAG, "FlickrJsonData: Called");
        mCallBack = callBack;
        mMatchAll = matchAll;
        mLanguage = language;
        mBaseURL = baseURL;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: Starts");
        if(mCallBack!=null){
            mCallBack.onDataAvailable(mPhotoList,DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: Ended");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: Called");
        String destUri = createUri(params[0],mLanguage,mMatchAll);
        FlickrRawData flickrRawData = new FlickrRawData(this);
        flickrRawData.runInSameThread(destUri);
        Log.d(TAG, "doInBackground: Ended");
        return null;
    }

    void execOnSameThread(String searchCriteria){
        Log.d(TAG, "execOnSameThread: Called");
        runOnSameTh = true;
        String destURI = createUri(searchCriteria,mLanguage,mMatchAll);
        FlickrRawData rawData = new FlickrRawData(this);
        rawData.execute(destURI);
        Log.d(TAG, "execOnSameThread: Finished");
    }

    private String createUri(String searchCriteria,String lang,boolean matchAll){
        Log.d(TAG, "createUri: Called");
        //Creating the URI to be used by appending all the options.
        //e.g https://api.flickr.com/services/feeds/photos_public.gne?tags=darksouls&nojsoncallback=1&format=json
        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags",searchCriteria)
                .appendQueryParameter("tagmode",matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang",lang)
                .appendQueryParameter("format","json")
                .appendQueryParameter("nojsoncallback","1")
                .build().toString();
    }
    
    @Override
    public void onDownloadComplete(String data,DownloadStatus status){
        Log.d(TAG, "onDownloadComplete: called.Status::"+status);
        if(status == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();
            try{
                //Reading the JSON response and creating an Object holding it
                JSONObject jsonData = new JSONObject(data);
                //Reading the array "items"containing information regarding the search made
                JSONArray jsonItems = jsonData.getJSONArray("items");
                for(int i=0;i<jsonItems.length();i++){
                    JSONObject jsonPhoto = jsonItems.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");
                    //saving the Object's "media" with key "m" that is the actual photos link
                    JSONObject media = jsonPhoto.getJSONObject("media");
                    String photoUrl = media.getString("m");
                    //Replacing the last letter before the image extension(e.g _m.jpeg to _b.jpeg)
                    //https://www.flickr.com/services/api/misc.urls.html
                    //As seen on the link we can get different size of pictures based on this variable letter
                    String link = photoUrl.replaceFirst("_m.","_b.");
                    Photo photoObj = new Photo(author,authorId,photoUrl,link,tags,title);

                    mPhotoList.add(photoObj);
                    Log.d(TAG, "onDownloadComplete: added photoObj to List::"+photoObj.toString());
                }
            }catch(JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error on JSON Data::"+e.getMessage() );
                status = DownloadStatus.FAILED_OR_NULL;
            }
        }

        //Call callback only when we are on a different thread(async) and data exists
        if(mCallBack != null && runOnSameTh){
            mCallBack.onDataAvailable(mPhotoList,status);
        }

        Log.d(TAG, "onDownloadComplete: Ended");
    }
}
