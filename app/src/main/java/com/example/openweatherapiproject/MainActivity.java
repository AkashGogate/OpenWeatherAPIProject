package com.example.openweatherapiproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView zipcodeText;

    Button button;
    EditText editText;

    TextView longi;
    TextView lat;
    TextView place;


    TextView time0;
    TextView time1;
    TextView time2;
    TextView time3;


    ImageView pic0;
    ImageView pic1;
    ImageView pic2;
    ImageView pic3;

    TextView picDesc0;
    TextView picDesc1;
    TextView picDesc2;
    TextView picDesc3;

    TextView temp0;
    TextView temp1;
    TextView temp2;
    TextView temp3;

    TextView feels0;
    TextView feels1;
    TextView feels2;
    TextView feels3;




    //Present Line 74, Android Manifest and network security configuration file for Permissions Section of Rubric
    private static final String API_KEY = "4a0200f7df8d65297ec8215555dfc68c";
    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        zipcodeText = findViewById(R.id.zipcode);
        button = findViewById(R.id.getWeather);

        //Show this line for second part of rubric
        editText = findViewById(R.id.editText);

        longi = findViewById(R.id.longitude);
        lat = findViewById(R.id.latitude);
        place = findViewById(R.id.place);

        time0 = findViewById(R.id.time0);
        pic0 = findViewById(R.id.pic0);
        picDesc0 = findViewById(R.id.picDesc0);
        temp0 = findViewById(R.id.temp0);
        feels0 = findViewById(R.id.feels0);


        time1 = findViewById(R.id.time1);
        pic1 = findViewById(R.id.pic1);
        picDesc1 = findViewById(R.id.picDesc1);
        temp1 = findViewById(R.id.temp1);
        feels1 = findViewById(R.id.feels1);

        time2 = findViewById(R.id.time2);
        pic2 = findViewById(R.id.pic2);
        picDesc2 = findViewById(R.id.picDesc2);
        temp2 = findViewById(R.id.temp2);
        feels2 = findViewById(R.id.feels2);

        time3 = findViewById(R.id.time3);
        pic3 = findViewById(R.id.pic3);
        picDesc3 = findViewById(R.id.picDesc3);
        temp3 = findViewById(R.id.temp3);
        feels3 = findViewById(R.id.feels3);

        pic0.setImageResource(0);
        pic1.setImageResource(0);
        pic2.setImageResource(0);
        pic3.setImageResource(0);


        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // Start here from part 2
        // Show onClick function and how you implemented asynctask
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTask<Void, Void, JSONArray> async = new AsyncTask<Void, Void, JSONArray>() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    protected JSONArray doInBackground(Void... params) {

                        String zipcode = editText.getText().toString();
                        if (zipcode == null || zipcode.isEmpty()) {
                            return null;
                        }
                        try {
                            //present for part 2 in the rubric
                            URL geoApiUrl1 = new URL("https://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + ",US&appid=" + API_KEY);
                            URLConnection urlCon1 = geoApiUrl1.openConnection();
                            InputStream is1 = urlCon1.getInputStream();
                            BufferedReader bf1 = new BufferedReader(new InputStreamReader(is1));
                            String content = readAllLines(bf1);
                            System.out.println("Read:" + content);
                            JSONObject jsonObject1 = new JSONObject(content);

                            //present for third part of rubric
                            URL geoApiUrl2 = new URL("https://api.openweathermap.org/data/2.5/onecall?lat=" + jsonObject1.getString("lat") + "&lon=" + jsonObject1.getString("lon") + "&units=imperial&exclude=minutely,daily,current,alerts&appid=" + API_KEY);
                            URLConnection urlCon2 = geoApiUrl2.openConnection();
                            InputStream is2 = urlCon2.getInputStream();
                            BufferedReader bf2 = new BufferedReader(new InputStreamReader(is2));
                            String weather = readAllLines(bf2);
                            System.out.println("Read:" + weather);
                            JSONObject jsonObject2 = new JSONObject(weather);

                            // Since I had to two API calls that returned two different JSON objects, I had to save them in a JSON array and make sure
                            JSONArray arr = new JSONArray();
                            arr.put(jsonObject1);
                            arr.put(jsonObject2);
                            return arr;



                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(JSONArray array) {
                        try {
                            if(array == null || array.length() < 2){
                                Toast x = Toast.makeText(getApplicationContext(),"Invalid zipcode, Please Try Again", Toast.LENGTH_LONG);
                                x.show();
                                return;
                            }
                            //present the following for second part of rubric
                            longi.setText( "Longitude: " + array.getJSONObject(0).getString("lon"));
                            place.setText("City: " + array.getJSONObject(0).getString("name") + ", " + array.getJSONObject(0).getString("country"));
                            lat.setText("Latitude: " + array.getJSONObject(0).getString("lat"));


                            JSONArray hourly = array.getJSONObject(1).getJSONArray("hourly");


                            //present for third part of rubric
                            long time = hourly.getJSONObject(0).getLong("dt");
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(time*1000);
                            time0.setText(new SimpleDateFormat("h:mm aa").format(c.getTime()));
                            temp0.setText(Math.round(hourly.getJSONObject(0).getDouble("temp")*100.0)/100.0 + " °F");
                            feels0.setText(Math.round(hourly.getJSONObject(0).getDouble("feels_like")*100.0)/100.0 + " °F");

                            JSONArray weather = hourly.getJSONObject(0).getJSONArray("weather");
                            picDesc0.setText(weather.getJSONObject(0).getString("description"));
                            Resources resources = getResources();

                            int resourceId = resources.getIdentifier("a" + weather.getJSONObject(0).getString("icon"), "drawable",getPackageName());
                            pic0.setImageResource(resourceId);




                            //present for third part of rubric
                            time = hourly.getJSONObject(1).getLong("dt");
                            c = Calendar.getInstance();
                            c.setTimeInMillis(time*1000);
                            time1.setText(new SimpleDateFormat("h:mm aa").format(c.getTime()));
                            temp1.setText(Math.round(hourly.getJSONObject(1).getDouble("temp")*100.0)/100.0 + " °F");
                            feels1.setText(Math.round(hourly.getJSONObject(1).getDouble("feels_like")*100.0)/100.0 + " °F");

                            weather = hourly.getJSONObject(1).getJSONArray("weather");
                            picDesc1.setText(weather.getJSONObject(0).getString("description"));

                            resourceId = resources.getIdentifier("a" + weather.getJSONObject(0).getString("icon"), "drawable",getPackageName());
                            pic1.setImageResource(resourceId);




                            //present for third part of rubric
                            time = hourly.getJSONObject(2).getLong("dt");
                            c = Calendar.getInstance();
                            c.setTimeInMillis(time*1000);
                            time2.setText(new SimpleDateFormat("h:mm aa").format(c.getTime()));
                            temp2.setText(Math.round(hourly.getJSONObject(2).getDouble("temp")*100.0)/100.0 + " °F");
                            feels2.setText(Math.round(hourly.getJSONObject(2).getDouble("feels_like")*100.0)/100.0 + " °F");

                            weather = hourly.getJSONObject(2).getJSONArray("weather");
                            picDesc2.setText(weather.getJSONObject(0).getString("description"));

                            resourceId = resources.getIdentifier("a" + weather.getJSONObject(0).getString("icon"), "drawable",getPackageName());
                            pic2.setImageResource(resourceId);




                            //present for third part of rubric
                            time = hourly.getJSONObject(3).getLong("dt");
                            c = Calendar.getInstance();
                            c.setTimeInMillis(time*1000);
                            time3.setText(new SimpleDateFormat("h:mm aa").format(c.getTime()));
                            temp3.setText(Math.round(hourly.getJSONObject(3).getDouble("temp")*100.0)/100.0 + " °F");
                            feels3.setText(Math.round(hourly.getJSONObject(3).getDouble("feels_like")*100.0)/100.0 + " °F");

                            weather = hourly.getJSONObject(3).getJSONArray("weather");
                            picDesc3.setText(weather.getJSONObject(0).getString("description"));

                            resourceId = resources.getIdentifier("a" + weather.getJSONObject(0).getString("icon"), "drawable",getPackageName());
                            pic3.setImageResource(resourceId);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                async.execute();

                //For the Third Part go to the emulator and show the output weather on the tableLayout
                //For fourth part, put a new zipcode and press the button again
                //For the fifth part, show that it can display latitude, city, longitude and the weather in the click of one button
                //For sixth part

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readAllLines(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        return content.toString();
    }

}