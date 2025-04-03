package com.example.weatherappapi;


import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonFetch;
    private TextView textViewResult;
    private final String API_KEY = "91b157fe2d008df2ee23fd8f475d3262"; // Your OpenWeatherMap API Key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonFetch = findViewById(R.id.buttonFetch);
        textViewResult = findViewById(R.id.textViewResult);

        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    new FetchWeatherTask().execute(city);
                }
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

            try {
                // Open connection to the OpenWeatherMap API
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(result);

                    // Check for errors in the response
                    if (jsonObject.has("cod") && jsonObject.getInt("cod") != 200) {
                        String errorMessage = jsonObject.getString("message");
                        textViewResult.setText("Error: " + errorMessage);
                    } else {
                        // Extract the weather data from the JSON response
                        String cityName = jsonObject.getString("name");
                        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
                        String weatherCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                        // Display the weather information
                        String finalResult = "City: " + cityName + "\n" +
                                "Temperature: " + temperature + "°C\n" +
                                "Condition: " + weatherCondition;

                        textViewResult.setText(finalResult);
                    }
                } catch (Exception e) {
                    textViewResult.setText("Error: Unable to fetch weather data");
                }
            } else {
                textViewResult.setText("Error: Invalid city name or API issue");
            }
        }
    }
}
