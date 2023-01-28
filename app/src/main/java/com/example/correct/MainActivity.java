package com.example.correct;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity  {

    public EditText my_text;
    public TextView fix_text;
    private final OkHttpClient httpClient = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MainActivity obj = new MainActivity();
        //button click
        Button button = (Button) findViewById(R.id.button_id);
        Button clearbutton = (Button) findViewById(R.id.clear_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sendPost();
               // fix_text.setText(my_text.getText());

            }
        });
        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fix_text.setText(null);
                my_text.setText(null);
            }
        });



    }
    private void  sendPost() {

        fix_text =(TextView) findViewById(R.id.fix);
        my_text = (EditText) findViewById(R.id.edit_text);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", "Correct this to standard English:\\n\\n"+my_text.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", "Bearer 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")
                .post(body)
                .build();



        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response.code());
            //System.out.println(response.peekBody(99999L).string());
            String responseData = response.body().string();
            JSONObject objt = new JSONObject(responseData);
            JSONArray choice = objt.getJSONArray("choices");
            JSONObject fixtext = choice.getJSONObject(0);
            String name = fixtext.getString("text");
            //System.out.println("-------------"+name);
            fix_text.setText(name);

        } catch (IOException | JSONException exception) {
            exception.printStackTrace();
        }
    }
}