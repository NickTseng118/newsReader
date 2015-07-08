package com.ntseng.cnn_top_headlines.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nicktseng on 15/6/29.
 */
public class FavoriteNewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> mNewsItems;
    private Context mContext;
    NewsItem newsItem;

    public FavoriteNewsAdapter(Context context, List<NewsItem> newsItems) {
        super(context, R.layout.top_news_view, newsItems);
        mContext = context;
        mNewsItems = newsItems;
    }
    public View getView(int position, View convertView, ViewGroup parent){

        newsItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_news_view, parent, false);
        }

        TextView titleTV = (TextView)convertView.findViewById(R.id.favorite_newsTitle);
        TextView dateTV = (TextView)convertView.findViewById(R.id.favorite_pubDate);
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.favorite_newsImage);

        String mediaThumbnail = newsItem.getMediaThumbnail();
        if(mediaThumbnail != null){
            Uri uri = Uri.parse(mediaThumbnail);
            draweeView.setImageURI(uri);
        }

        titleTV.setText(newsItem.getTitle().replaceAll("\n","").trim());
        dateTV.setText(dateFormat(newsItem.getPubDate()));

        return convertView;
    }
    private String dateFormat(Date text){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss", Locale.US);
        return formatter.format(text);
    }

}
