package com.codepath.apps.restclienttemplate.adapters;

import java.util.List;
import java.util.Date;
import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TimeFormatter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    //For each row, inflate the tweet layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get data at position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with the viewholder
        holder.bind(tweet);
    }


    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //Clean elements of recycler view
    public void clear(){
        tweets.clear();
        notifyDataSetChanged();
    }
    //Add a list of items
    public void addAll(List<Tweet> tweetList){
        tweets.addAll(tweetList);
        notifyDataSetChanged();
    }

    //Define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvName;
        TextView timeStamp;
        TextView tvTag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage=  itemView.findViewById(R.id.profile_pic);
            tvBody = itemView.findViewById(R.id.screen_tweet);
            tvName = itemView.findViewById(R.id.profile_name);
            timeStamp = itemView.findViewById(R.id.tweet_time_stamp);
            tvTag = itemView.findViewById(R.id.profile_tag);

        }

        public void bind(Tweet tweet){
            tvBody.setText(tweet.body);
            tvName.setText(tweet.user.name);
            timeStamp.setText(TimeFormatter.getTimeDifference(tweet.createdAt));
            tvTag.setText("@"+tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageUrl).transform(new RoundedCornersTransformation(30, 10)).into(ivProfileImage);
        }
    }
}
