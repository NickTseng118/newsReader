package com.ntseng.cnn_top_headlines.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ntseng.cnn_top_headlines.Singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.adapter.TopNewsAdapter;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.NewsDetailActivity;
import com.ntseng.cnn_top_headlines.R;

import java.util.List;


public class TopNewsFragment extends Fragment {

    private List<NewsItem> topItemList;
    TopNewsAdapter mTopNewsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_news, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        topItemList = DAOSingleton.getDAOInstance().selectAll();

        ListView topNewsListview = (ListView)getActivity().findViewById(R.id.topNewslistView);
        mTopNewsAdapter = new TopNewsAdapter(getActivity(), topItemList);
        topNewsListview.setAdapter(mTopNewsAdapter);
        topNewsListview.setOnItemClickListener(onNewsItemclicklistener);

    }

    private AdapterView.OnItemClickListener onNewsItemclicklistener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            bundle.putSerializable("newsItem", topItemList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    public void refresh(){
        mTopNewsAdapter.notifyDataSetChanged();
    }

}
