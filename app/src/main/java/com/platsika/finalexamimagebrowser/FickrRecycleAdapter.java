package com.platsika.finalexamimagebrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class FlickrRecycleAdapter extends RecyclerView.Adapter<FlickrRecycleAdapter.FlickrImgHolder>{
    private static final String TAG = "FlickrRecycleAdapter";
    private List<Photo> mPhotoList;
    private Context mContext;

    public FlickrRecycleAdapter(Context context,List<Photo> photoList) {
        mPhotoList = photoList;
        mContext = context;
    }

    @Override
    public FlickrImgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Called by layout manager when we need a new view.
        Log.d(TAG, "onCreateViewHolder: new View Requested");
        //TSEKARE POIO EBALE TELIKO AN ITAN TO IMAGE_HOLD I KAPOIO ALLO KAI SBISE TO COMMENT.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_hold,parent,false);
        return new FlickrImgHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImgHolder holder, int position) {
        //Called by recyclerView when we need new data to be displayed
        //we get the values and place them on viewHolders

        if((mPhotoList==null || (mPhotoList.size()==0))){
            holder.thumbn.setImageResource(R.drawable.loading_image);
            holder.title.setText(R.string.empty_json);
        }else {
            Photo photoItem = mPhotoList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ==> " + position);
            Picasso.with(mContext).load(photoItem.getImage())
                    .error(R.drawable.broken_image)
                    .placeholder(R.drawable.loading_image)
                    .into(holder.thumbn);
            holder.title.setText(photoItem.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Started");
        return (((mPhotoList !=null) && (mPhotoList.size() !=0))? mPhotoList.size() :1);
    }

    void loadNewData(List<Photo> newPhotos){
        mPhotoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhotos(int pos){
        return((mPhotoList != null) && (mPhotoList.size()!=0) ? mPhotoList.get(pos):null);
    }

    static class FlickrImgHolder extends RecyclerView.ViewHolder{
        private static final String TAG = "FlickrImgHolder";
        ImageView thumbn = null;
        TextView title= null;

        public FlickrImgHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImgHolder Constructon called");
            this.thumbn = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
