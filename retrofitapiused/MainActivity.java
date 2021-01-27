package com.example.retrofitapiused;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://medi.medisquare.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<model> call = jsonPlaceHolderApi.getIssue("55");
        call.enqueue(new Callback<model>() {
            @Override
            public void onResponse(Call<model> call, Response<model> response) {

                Toast.makeText(MainActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                Log.e(" mainAction", "  response "+ response.body().toString());
                textViewResult.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<model> call, Throwable t) {
                Log.e("MainActivity ", "  error "+ t.toString());

            }
        });*/


        //classic way to retrieve data through retrofit API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://medi.medisquare.xyz/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<String> call = jsonPlaceHolderApi.getPosts("55");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                    //textViewResult.setText("Code: " + response.code());

                String id, patient_id, patient_name = "", name, start_date;
                try {
                    JSONObject jsonObject = new JSONObject(response.body());

                    String string = jsonObject.getString("data");

                    if(!string.equals("")){


                        JSONArray jsonArray = new JSONArray(string);

                        int count = 0;

                        while (count < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(count);


                            id = JO.getString("id");
                            patient_id = JO.getString("patient_id");
                            patient_name = JO.getString("patient_name");
                            name = JO.getString("name");
                            start_date = JO.getString("start_date");



                            count++;


                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                textViewResult.setText(patient_name);


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }


        });
    }
}