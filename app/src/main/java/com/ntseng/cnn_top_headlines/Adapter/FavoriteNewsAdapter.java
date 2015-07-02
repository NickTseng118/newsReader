package com.ntseng.cnn_top_headlines.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ntseng.cnn_top_headlines.Model.NewsItem;
import com.ntseng.cnn_top_headlines.R;

import java.util.List;

/**
 * Created by nicktseng on 15/6/29.
 */
public class FavoriteNewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> mNewsItems;
    private Context mContext;
    NewsItem newsItem;

    TextView titleTV;
    TextView dateTV;
    SimpleDraweeView draweeView;

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

        titleTV = (TextView)convertView.findViewById(R.id.favorite_newsTitle);
        dateTV = (TextView)convertView.findViewById(R.id.favorite_pubDate);
        draweeView = (SimpleDraweeView) convertView.findViewById(R.id.favorite_newsImage);

        String mediaThumbnail = newsItem.getMediaThumbnail();
        if(mediaThumbnail != null){
            Uri uri = Uri.parse(mediaThumbnail);
            draweeView.setImageURI(uri);
        }

        titleTV.setText(newsItem.getTitle().replaceAll("\n","").trim());
        dateTV.setText(newsItem.getPubDate());

        return convertView;
    }

}
