package com.ntseng.cnn_top_headlines.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ntseng.cnn_top_headlines.R;

import org.json.JSONException;

import java.util.List;


/**
 * Created by nicktseng on 15/7/1.
 */
public class SearchesAdapter extends ArrayAdapter {

    private Context context;
    private List<String> searchesList;

    TextView keywordTV;

    public SearchesAdapter(Context context, List<String> searches) {
        super(context, R.layout.searches_view, searches);
        this.context = context;
        this.searchesList = searches;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.searches_view, parent, false);
        }

        String keyword = (String)getItem(position);
        keywordTV = (TextView) convertView.findViewById(R.id.keyword_tv);
        keywordTV.setText(keyword);

        return convertView;
    }
}
