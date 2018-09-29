package com.example.mohammed.skyquestionbank.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.example.mohammed.skyquestionbank.R;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class MyDrawerImageLoader implements DrawerImageLoader.IDrawerImageLoader {


    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
        Picasso.get().load(uri).placeholder(placeholder).into(imageView);
    }

    @Override
    public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
        Picasso.get().load(uri).placeholder(placeholder).into(imageView);
    }

    @Override
    public void cancel(ImageView imageView) {

        Picasso.get().cancelRequest(imageView);
    }

    @Override
    public Drawable placeholder(Context ctx) {
        return ContextCompat.getDrawable(ctx, R.drawable.ic_account_circle_black_24dp);
    }

    @Override
    public Drawable placeholder(Context ctx, String tag) {
        return ContextCompat.getDrawable(ctx, R.drawable.ic_account_circle_black_24dp);
    }

}
