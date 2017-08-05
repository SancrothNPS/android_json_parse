package com.platsika.finalexamimagebrowser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

//We implement the FickRawData interface to make sure that any object passed will have this method attached.
public class MainActivity extends AppCompatActivity implements FlickrJsonData.OnDataAvailable {

    //CSV tags -> Search Criteria
    private String mTags = "darksouls, prepare to die";
    //Tag to be used with Logger
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FlickrRawData rawData = new FlickrRawData(this);
        //rawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=darksouls&format=json&nojsoncallback=1");
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume: Called");
        super.onResume();
        FlickrJsonData flickrJsonData = new FlickrJsonData(this,true,"en-us","https://api.flickr.com/services/feeds/photos_public.gne");
        //flickrJsonData.execOnSameThread("android, nougat");
        flickrJsonData.execute(mTags);
        Log.d(TAG, "onResume: Ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        Log.d(TAG, "onCreateOptionsMenu() returned: " + true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: --Success");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){
        if(status == DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: Data::"+data);
        }else{
            Log.e(TAG, "onDataAvailable: Error status::"+status );
        }
    }
}
