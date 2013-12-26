package in.ac.dtu.coupon;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        switch(item.getItemId()) {
            case R.id.action_settings :
                return true;
            case R.id.action_refresh :
                new UpdateCoupons(MainActivity.this, false).execute();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReadFromJson extends AsyncTask<Void, Void, ArrayList<JSONObject>> {

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... v) {

            String jsonString = "";

            try {
                //Log.d(TAG, getActivity().getFilesDir() + "data.json");
                File cacheFile = new File(getApplicationContext().getFilesDir(), "data.json");

                BufferedReader br = new BufferedReader(new FileReader(cacheFile));
                jsonString = br.readLine();

            } catch (Exception e) {
                e.printStackTrace();
                new UpdateCoupons(MainActivity.this, true).execute();
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
    }

}
