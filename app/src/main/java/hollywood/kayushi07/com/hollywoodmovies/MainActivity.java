package hollywood.kayushi07.com.hollywoodmovies;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    /* ... */
    private NetworkStateReceiver networkStateReceiver;
    GridView simpleGrid;
    int logos[] = {R.drawable.f_2017, R.drawable.f_2011, R.drawable.f_2006, R.drawable.f_2001, R.drawable.f_1996, R.drawable.f_1990, R.drawable.f_2017};
    private AdView mAdView;
    InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        mAdView = (AdView) findViewById(R.id.adView);

        simpleGrid = (GridView) findViewById(R.id.main_grid); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomGridAdapter customAdapter = new CustomGridAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);


        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                final int save_position = position;

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();

                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
                            intent.putExtra("id", save_position);
                            startActivity(intent);

                            // Load the next interstitial.
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }

                    });
                }
                else {

                    Intent intent = new Intent(MainActivity.this, MovieListActivity.class);
                    intent.putExtra("id", position);
                    startActivity(intent);
                }
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

        createNewIntAd();
    }

    private void createNewIntAd() {
        interstitialAd = new InterstitialAd(this);
        // set the adUnitId (defined in values/strings.xml)
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    public void networkUnavailable() {
        mAdView.setVisibility(View.GONE);
    }
}

