package com.platsika.finalexamimagebrowser;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        activateToolbar(true);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(PHOTO_TRANSFER);
        if(photo != null){
            Resources resources = getResources();

            TextView photoTitle = (TextView) findViewById(R.id.photo_title);
            photoTitle.setText(resources.getString(R.string.photo_title_text,photo.getTitle()));

            TextView photoTags = (TextView) findViewById(R.id.photo_tagas);
            photoTags.setText(resources.getString(R.string.photo_tags_text,photo.getTags()));

            TextView photoAuthor = (TextView) findViewById(R.id.photo_author);
            photoAuthor.setText("Author: "+photo.getAuthor());

            ImageView photoImage = (ImageView) findViewById(R.id.photo_img);
            Picasso.with(this).load(photo.getLink())
                    .error(R.drawable.broken_image)
                    .placeholder(R.drawable.loading_image)
                    .into(photoImage);
        }
    }
}
