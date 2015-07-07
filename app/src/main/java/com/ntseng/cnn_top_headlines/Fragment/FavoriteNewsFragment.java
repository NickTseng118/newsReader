package com.ntseng.cnn_top_headlines.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ntseng.cnn_top_headlines.singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.adapter.FavoriteNewsAdapter;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.NewsDetailActivity;
import com.ntseng.cnn_top_headlines.R;


import java.util.List;


public class FavoriteNewsFragment extends Fragment {

    private List<NewsItem> favoriteItemList;
    private ListView favoriteNewsListview;
    FavoriteNewsAdapter mFavoriteNewsAdapter;
    AlertDialog builder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_news, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        favoriteNewsListview = (ListView)getActivity().findViewById(R.id.favoriteNewslistView);
        favoriteItemList = DAOSingleton.getDAOInstance().getFavoriteNews(true);
        mFavoriteNewsAdapter = new FavoriteNewsAdapter(getActivity(), favoriteItemList);
        favoriteNewsListview.setAdapter(mFavoriteNewsAdapter);

        favoriteNewsListview.setOnItemClickListener(onItemClickListener);
        favoriteNewsListview.setOnItemLongClickListener(onItemLongClickListener);
    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            bundle.putSerializable("newsItem", favoriteItemList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final NewsItem newsItem = favoriteItemList.get(position);
            final int itemPosition = position;

            builder = new AlertDialog.Builder(getActivity())
                    .setMessage("Do you want to delete from favorite？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newsItem.setFavorite(false);
                            DAOSingleton.getDAOInstance().saveItem(newsItem);
                            favoriteItemList.remove(itemPosition);
                            mFavoriteNewsAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {	}
                    })
                    .show();

            return true;
        }
    };

    public void refresh(){
        favoriteItemList.clear();
        favoriteItemList.addAll(DAOSingleton.getDAOInstance().getFavoriteNews(true));
        mFavoriteNewsAdapter.notifyDataSetChanged();
    }
}
