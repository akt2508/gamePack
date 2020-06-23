package com.example.hambola.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class imageDownload extends AsyncTask<String,Void, Bitmap>
{
    @Override
    protected Bitmap doInBackground(String... strings) {

        try
        {

            URL url = new URL(strings[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);

            connection.connect();

            InputStream stream = connection.getInputStream();

            return BitmapFactory.decodeStream(stream);

        } catch (Exception e)
        {
            Log.i("shit","no image downloaded");
            e.printStackTrace();
        }

        return null;
    }
}
