package com.ntseng.cnn_top_headlines.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ntseng.cnn_top_headlines.singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nicktseng on 15/6/25.
 */
public class TopNewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> mNewsItems;
    private Context mContext;
    private int position;
    private NewsItem newsItem;
    ImageView favoriteImage;

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

        TextView titleTV = (TextView)convertView.findViewById(R.id.title);
        favoriteImage = (ImageView)convertView.findViewById(R.id.favoriteImage);
        TextView dateTV = (TextView)convertView.findViewById(R.id.date);
        TextView contentTV = (TextView)convertView.findViewById(R.id.content);
        SimpleDraweeView newsImage = (SimpleDraweeView) convertView.findViewById(R.id.newsImage);

        String mediaThumbnail = newsItem.getMediaThumbnail();
        if(mediaThumbnail != null){
            Uri uri = Uri.parse(mediaThumbnail);
            newsImage.setImageURI(uri);
        }
        titleTV.setText(newsItem.getTitle().replaceAll("\n", "").trim());
        dateTV.setText(dateFormat(newsItem.getPubDate()));

        hideIfNoDescription(contentTV);
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
            DAOSingleton.getDAOInstance().saveItem(newsItem);
            notifyDataSetChanged();
        }
    };

    private void hideIfNoDescription(TextView contentTV){
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

    private String dateFormat(Date text){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss", Locale.US);
        return formatter.format(text);
    }
}

