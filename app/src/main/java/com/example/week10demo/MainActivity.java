package com.example.week10demo;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button getWeatherButton;
    WebView webView;
    EditText editText;
    TextView resultTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWeatherButton = findViewById(R.id.getWeatherButton);
        webView = findViewById(R.id.webView);
        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextView);

        webView.loadUrl("https://api.openweathermap.org/data/2.5/weather?q=London&appid=e1471c9fa2c09a964798ce6b507a535d");

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        getWeatherButton.setOnClickListener(v -> {
            String cityName = editText.getText().toString();
            String jsonUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName +"&appid=e1471c9fa2c09a964798ce6b507a535d";
            webView.loadUrl(jsonUrl);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, jsonUrl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String visibility;
                            try {
                                visibility = response.getString("visibility");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            resultTextView.setText(visibility);


                            JSONObject jsonObject1;
                            try {
                                 jsonObject1 = response.getJSONObject("coord");
//                                jsonObject1 = response.getJSONObject("main");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String coord = jsonObject1.optString("lon");
//                            resultTextView.setText(coord);


                            JSONArray jsonArray = response.optJSONArray("weather");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = jsonArray.getJSONObject(0);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String description = jsonObject.optString("description");
//                            resultTextView.setText(description);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });


            requestQueue.add(jsonObjectRequest);





        });


    }
}