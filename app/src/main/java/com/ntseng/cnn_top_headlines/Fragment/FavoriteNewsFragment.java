package com.ntseng.cnn_top_headlines.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.ntseng.cnn_top_headlines.Adapter.FavoriteNewsAdapter;
import com.ntseng.cnn_top_headlines.Adapter.TopNewsAdapter;
import com.ntseng.cnn_top_headlines.Model.NewsItem;
import com.ntseng.cnn_top_headlines.NewsDetailActivity;
import com.ntseng.cnn_top_headlines.R;
import com.ntseng.cnn_top_headlines.Singleton.SelectSingleton;

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
        favoriteItemList = getFavoriteNews(true);
        mFavoriteNewsAdapter = new FavoriteNewsAdapter(getActivity(), favoriteItemList);
        favoriteNewsListview.setAdapter(mFavoriteNewsAdapter);

        favoriteNewsListview.setOnItemClickListener(onItemClickListener);
        favoriteNewsListview.setOnItemLongClickListener(onItemLongClickListener);
    }

    private List<NewsItem> getFavoriteNews (boolean favorite) {
        return SelectSingleton.getSelectInstance().from(NewsItem.class).where ("favorite = ?", favorite).execute();
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
                            newsItem.save();
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

    public void onRefresh(){
        favoriteItemList.clear();
        favoriteItemList = getFavoriteNews(true);
        mFavoriteNewsAdapter = new FavoriteNewsAdapter(getActivity(), favoriteItemList);
        favoriteNewsListview.setAdapter(mFavoriteNewsAdapter);

        Log.e("favoriteItem","" + favoriteItemList.size());
//        FavoriteNewsFragment favoriteNewsFragment = null;
//        favoriteNewsFragment = (FavoriteNewsFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.pager);
//        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//        ft.detach(favoriteNewsFragment);
//        ft.attach(favoriteNewsFragment);
//        ft.commit();
    }
}
