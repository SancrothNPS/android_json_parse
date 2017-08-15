package com.platsika.finalexamimagebrowser;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecuclerItemClickListen";

    interface OnRecyclerClickListener{
        void onItemClick(View view,int pos);
        void onItemLongClick(View view,int pos);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetectorCompat;

    public RecyclerItemClickListener(Context context , final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetectorCompat = new GestureDetectorCompat(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp:Starts - Evernt ::"+e);
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView !=null && mListener !=null){
                    Log.d(TAG, "onSingleTapUp: calling listener.onitemClick");
                    mListener.onItemClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: Start - Event ::"+e);
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView !=null && mListener!=null){
                    Log.d(TAG, "onLongPress: calling listener.onItemLongCLick");
                    mListener.onItemLongClick(childView,recyclerView.getChildAdapterPosition(childView));
                }
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: Called");
        if(mGestureDetectorCompat != null){
            boolean res = mGestureDetectorCompat.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned "+res);
            return res;
        }else{
            Log.d(TAG, "onInterceptTouchEvent: returned False");
            return false;
        }
    }
}
