package com.banglapuzzle.medisquare.extension.helpers.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.banglapuzzle.medisquare.extension.helpers.services.AppLists;
import com.banglapuzzle.medisquare.extension.helpers.services.AppPreferences;
import com.banglapuzzle.medisquare.extension.models.Banner;
import com.banglapuzzle.medisquare.ui.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BannerImageLoaderAsyncTask extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private AppPreferences preferences;
    public BannerImageLoaderAsyncTask(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    protected String doInBackground(Void... voids) {

        try {

            URL categoryApi = new URL("https://medi.medisquare.xyz/api/home/banner");
            InputStream responseInputStream;
            BufferedReader bufferedReader;
            StringBuilder responseStringBuilder = new StringBuilder();
            String line;

            HttpURLConnection urlConnection = (HttpURLConnection) categoryApi.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();

            responseInputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(responseInputStream));

            // Convert BufferReader Object to StringBuilder Object
            while ( (line = bufferedReader.readLine()) !=null ) {
                responseStringBuilder.append(line);
            }

            // Close
            responseInputStream.close();
            bufferedReader.close();

            return responseStringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        if(s.equals("")){

            new BannerImageLoaderAsyncTask(mContext).execute();
        }

        else {

            try {
                JSONObject obj = new JSONObject(s);
                String info = obj.getString("data");
                JSONArray jsonArray = new JSONArray(info);

                for (int i =0; i <= jsonArray.length(); i++) {

                    JSONObject ignoredObject= jsonArray.getJSONObject(i);

                    String imageUri = "https://medisquare.xyz/assets/backend/images/home/" + ignoredObject.getString("image");
                    int sec = 5;

                    AppLists.banners.add( new Banner( imageUri, sec ) );
                }



            } catch (Exception e) {
                e.printStackTrace();
            }


            //loading location data
            new LocationAsyncTask(mContext).execute();
        }

    }

}
