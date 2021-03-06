package com.platsika.finalexamimagebrowser;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * Created by Pliatsika on 1/8/2017.
 */

public class BaseActivity extends AppCompatActivity{
    private static final String TAG = "BaseActivity";
    static final String FLICK_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolbar(boolean enableHome){
        Log.d(TAG, "actiavateToolbar: Starts");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null){
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            if(toolbar!=null){
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
