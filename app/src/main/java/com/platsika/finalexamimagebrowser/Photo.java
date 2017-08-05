package com.platsika.finalexamimagebrowser;

/**
 * Created by Plats on 7/7/2017.
 */

class Photo {
    private String mTitle;
    private String mAuthor;
    private String mAuthroId;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String author, String authroId, String image, String link, String tags, String title) {
        mAuthor = author;
        mAuthroId = authroId;
        mImage = image;
        mLink = link;
        mTags = tags;
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getAuthroId() {
        return mAuthroId;
    }

    public String getImage() {
        return mImage;
    }

    public String getTags() {
        return mTags;
    }

    public String getLink() {
        return mLink;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mAuthroId='" + mAuthroId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
