package in.ac.dtu.coupon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import in.tosc.coupon.R;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private CustomCardAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        new ReadFromJson().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                if (Utils.isNetworkConnected(getApplicationContext())) {
                    new UpdateCoupons(MainActivity.this, false) {
                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            new ReadFromJson().execute();
                        }
                    }.execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReadFromJson extends AsyncTask<Void, Void, ArrayList<JSONObject>> {

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... v) {

            String jsonString = "";

            try {
                File cacheFile = new File(getApplicationContext().getFilesDir(), "data.json");

                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                jsonString = br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
                if (Utils.isNetworkConnected(getApplicationContext())) {
                    new UpdateCoupons(MainActivity.this, true) {
                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);
                            new ReadFromJson().execute();
                        }
                    }.execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                }

            }

            ArrayList<JSONObject> couponList = new ArrayList<JSONObject>();

            try {
                JSONArray jsonArray = new JSONArray(jsonString);

                for (int i = 1; i < jsonArray.length(); ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    couponList.add(obj);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return couponList;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> couponList) {

            mRecyclerView = (RecyclerView) findViewById(R.id.list_main);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mLayoutManager.scrollToPosition(0); // TODO: Remove this when preview SDK has abstract method.

            mRecyclerView.setLayoutManager(mLayoutManager);

            mAdapter = new CustomCardAdapter(getApplicationContext(), couponList);
            Log.d(TAG, "List size = " + couponList.size());
            // This sets the color displayed for card titles and header actions by default

            //if (couponList.size() != 0) {
                Log.d(TAG, "Inside onPostExecute()");
                mRecyclerView.setAdapter(mAdapter);
                ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            //}

        }
    }

}
