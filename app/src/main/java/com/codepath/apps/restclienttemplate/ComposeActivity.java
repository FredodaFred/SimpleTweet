package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    //Android snack bar instead of toasts
    public static final int MAX_TWEET_LENGTH =280;
    public static final String POST_ENDPOINT = "statuses/update";
    EditText etCompose;
    Button btnTweet;
    TextView charCount;
    TwitterClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.publishButton);
        charCount = findViewById(R.id.char_count);
        charCount.setText("0/280");
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                charCount.setText("0/280");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start-before < 0)
                    charCount.setText("0/280");
                else
                    charCount.setText(start-before+"/280");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Set click listener on button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Sorry your tweet is too long", Toast.LENGTH_SHORT).show();

                }
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i("ComposeActivity", "onSuccess Publish ");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Log.i("ComposeActivity", "Published tweet");
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, intent);
                            //close activity
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ComposeActivity", e.toString());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e("ComposeActivity", "onSuccess Failure ", throwable);
                    }
                });
                return;
            }
        });
        //Make an API call to Twitter to publish the tweet
    }
}
