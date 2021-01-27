package com.banglapuzzle.medisquare.extension.helpers.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.banglapuzzle.medisquare.extension.helpers.services.AppPreferences;
import com.banglapuzzle.medisquare.ui.activities.MainActivity;
import com.banglapuzzle.medisquare.ui.activities.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UpdateProfileAsyncTask extends AsyncTask<String, Void,String> {

    private Context context;
    private AppPreferences preferences;

    public UpdateProfileAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        preferences = new AppPreferences(context);
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuilder result = new StringBuilder();
        String check_user_url = "https://medi.medisquare.xyz/api/visitor/edit/" + preferences.getId();


        String fullName = params[0];
        String phone = params[1];
        String email = params[2];
        String date = params[3];
        String blood = params[4];
        String gender = params[5];

        try {
            URL url = new URL(check_user_url);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(fullName, "UTF-8")
                    +"&&"+URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                    +"&&"+URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")
                    +"&&"+URLEncoder.encode("blood_group", "UTF-8") + "=" + URLEncoder.encode(blood, "UTF-8")
                    +"&&"+URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8")
                    +"&&"+URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.ISO_8859_1));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            ips.close();
            http.disconnect();
            return result.toString();


        } catch (IOException e) {
            result = new StringBuilder(e.getMessage());
        }



        return result.toString();

    }


    @Override
    protected void onPostExecute(String s) {

        //Toast.makeText(context, s, Toast.LENGTH_SHORT).show();

        if(!s.equals("")){

            //AppLists.profileModelList.clear();

            try {
                JSONObject jsonObject = new JSONObject(s);

                String string = jsonObject.getString("data");

                JSONArray jsonArray = new JSONArray(string);

                int count = 0;

                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);

                    String id, first_name, last_name, email, date, blood_group, gender, phone, isVerified, google_id, provider, provider_id;

                    id = JO.getString("id");
                    first_name = JO.getString("first_name");
                    last_name = JO.getString("last_name");
                    email = JO.getString("email");
                    date = JO.getString("date");
                    blood_group = JO.getString("blood_group");
                    gender = JO.getString("gender");
                    phone = JO.getString("phone");
                    isVerified = JO.getString("isVerified");
                    google_id = JO.getString("google_id");
                    provider = JO.getString("provider");
                    provider_id = JO.getString("provider_id");


                    //saving data to AppPreferences
                    preferences.setId(Integer.parseInt(id));
                    preferences.setName(first_name);
                    preferences.setPhone(phone);
                    preferences.setEmail(email);
                    preferences.setAddress("");
                    preferences.setDate(date);
                    preferences.setBlood(blood_group);
                    preferences.setGender(gender);
                    preferences.setVerified(isVerified);
                    preferences.setLoginStatus(true);
                    preferences.setImage("");

                    count++;


                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
            Activity activity = (Activity) context;
            activity.onBackPressed();

        }

        else {

            //RegistrationActivity.relativeLayout.setVisibility(View.GONE);
            Toast.makeText(context, "আবার চেষ্টা করুন!", Toast.LENGTH_SHORT).show();

        }

    }


}