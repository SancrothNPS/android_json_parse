package com.platsika.finalexamimagebrowser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//We implement the FickRawData interface to make sure that any object passed will have this method attached.
public class MainActivity extends BaseActivity implements FlickrJsonData.OnDataAvailable,
                                                    RecyclerItemClickListener.OnRecyclerClickListener {

    //CSV tags -> Search Criteria
    private String mTags = "Metallica";
    //Tag to be used with Logger
    private static final String TAG = "MainActivity";
    private FlickrRecycleAdapter mFlickrRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "onCreate: starts");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activateToolbar(true);
        //Creating the recyclerview to load images on our list when loaded.
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,recyclerView,this));

        mFlickrRecycleAdapter = new FlickrRecycleAdapter(this,new ArrayList<Photo>());
        recyclerView.setAdapter(mFlickrRecycleAdapter);
        //FlickrRawData rawData = new FlickrRawData(this);
        //rawData.execute("https://api.flickr.com/services/feeds/photos_public.gne?tags=darksouls&format=json&nojsoncallback=1");
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onResume(){
        Log.d(TAG, "onResume: Called");
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String queryRes = sharedPreferences.getString(FLICK_QUERY,"");
        if(queryRes.length()>0){
            FlickrJsonData flickrJsonData = new FlickrJsonData(this,true,"en-us","https://api.flickr.com/services/feeds/photos_public.gne");
            mTags=queryRes;
            flickrJsonData.execute(mTags);
        }
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
        if(id == R.id.action_search){
            Log.d(TAG, "onOptionsItemSelected: Search Clicked");
            Intent intent = new Intent(this,SearchTagsActivity.class);
            startActivity(intent);
            return true;
        }

        Log.d(TAG, "onOptionsItemSelected() returned: --Success");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status){
        Log.d(TAG, "onDataAvailable: Called");
        if(status == DownloadStatus.OK){
            mFlickrRecycleAdapter.loadNewData(data);
        }else{
            Log.e(TAG, "onDataAvailable: Error status::"+status );
        }
        Log.d(TAG, "onDataAvailable: Ends");
    }

    @Override
    public void onItemClick(View view, int pos) {
        Log.d(TAG, "onItemClick: Called");
        Toast.makeText(MainActivity.this,"Normal Tap pos at: "+pos,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int pos) {
        Log.d(TAG, "onItemLongClick: Called");
        Toast.makeText(MainActivity.this,"Long Tap pos at: "+pos,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,PhotoDetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER,mFlickrRecycleAdapter.getPhotos(pos));
        startActivity(intent);
    }
}
