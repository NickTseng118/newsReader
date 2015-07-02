package com.ntseng.cnn_top_headlines.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ntseng.cnn_top_headlines.Model.NewsItem;
import com.ntseng.cnn_top_headlines.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicktseng on 15/6/25.
 */
public class TopNewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> mNewsItems;
    private Context mContext;
    private int position;
    NewsItem newsItem;

    TextView titleTV;
    ImageView favoriteImage;
    TextView dateTV;
    TextView contentTV;
    SimpleDraweeView newsImage;

    public TopNewsAdapter(Context context, List<NewsItem> newsItems) {
        super(context, R.layout.top_news_view, newsItems);
        mContext = context;
        mNewsItems = newsItems;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        this.position = position;

        newsItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.top_news_view, parent, false);
        }

        titleTV = (TextView)convertView.findViewById(R.id.title);
        favoriteImage = (ImageView)convertView.findViewById(R.id.favoriteImage);
        dateTV = (TextView)convertView.findViewById(R.id.date);
        contentTV = (TextView)convertView.findViewById(R.id.content);
        newsImage = (SimpleDraweeView) convertView.findViewById(R.id.newsImage);

        String mediaThumbnail = newsItem.getMediaThumbnail();
        if(mediaThumbnail != null){
            Uri uri = Uri.parse(mediaThumbnail);
            newsImage.setImageURI(uri);
        }
        titleTV.setText(newsItem.getTitle().replaceAll("\n","").trim());
        dateTV.setText(newsItem.getPubDate());

        contentViewVisibility();
        setFavoriteImage();

        return convertView;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            NewsItem newsItem = (NewsItem)v.getTag();

            if(newsItem.getFavorite() != null && newsItem.getFavorite()) {
                favoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
                newsItem.setFavorite(false);
            } else {
                favoriteImage.setImageResource(R.drawable.ic_star_black_36dp);
                newsItem.setFavorite(true);
            }
            newsItem.save();

            notifyDataSetChanged();
        }
    };

    private void contentViewVisibility(){
        if(newsItem.getDescription().length() <= 1){
            contentTV.setVisibility(View.GONE);
        }else{
            contentTV.setText(newsItem.getDescription());
        }
    }

    private void setFavoriteImage(){
        if(newsItem.getFavorite() != null && newsItem.getFavorite()) {
            favoriteImage.setImageResource(R.drawable.ic_star_black_36dp);
        } else {
            favoriteImage.setImageResource(R.drawable.ic_star_border_black_36dp);
        }
        favoriteImage.setOnClickListener(mOnClickListener);
        favoriteImage.setTag(newsItem);
    }
}

