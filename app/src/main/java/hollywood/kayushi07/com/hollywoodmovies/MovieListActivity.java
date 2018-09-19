package hollywood.kayushi07.com.hollywoodmovies;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MovieDataPump.ExListData1990_1995;
import MovieDataPump.ExListData1996_2000;
import MovieDataPump.ExListData2001_2005;
import MovieDataPump.ExListData2006_2010;
import MovieDataPump.ExListData2011_2015;
import MovieDataPump.ExListData2016_Future;

/**
 * Created by Ayushi on 19-12-2017.
 */

public class MovieListActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener{

    private NetworkStateReceiver networkStateReceiver;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        mAdView = (AdView) findViewById(R.id.adView2);

        // Get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

        switch (position){
            case 5:
                expandableListDetail = ExListData1990_1995.getData();
                setTitle("Hollywood Movies (1990 - 1995)");
                break;
            case 4:
                expandableListDetail = ExListData1996_2000.getData();
                setTitle("Hollywood Movies (1996 - 2000)");
                break;
            case 3:
                expandableListDetail = ExListData2001_2005.getData();
                setTitle("Hollywood Movies (2001 - 2005)");
                break;
            case 2:
                expandableListDetail = ExListData2006_2010.getData();
                setTitle("Hollywood Movies (2006 - 2010)");
                break;
            case 1:
                expandableListDetail = ExListData2011_2015.getData();
                setTitle("Hollywood Movies (2011 - 2015)");
                break;
            case 0:
                expandableListDetail = ExListData2016_Future.getData();
                setTitle("Hollywood Movies (2016 - Future)");
                break;

        }
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition),
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }




    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {

        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);

        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void networkAvailable() {
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void networkUnavailable() {
        mAdView.setVisibility(View.GONE);
    }
}
