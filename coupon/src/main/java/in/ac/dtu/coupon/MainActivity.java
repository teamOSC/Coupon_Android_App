package in.ac.dtu.coupon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardHeader;
import com.afollestad.cardsui.CardListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<Card> cardList = new ArrayList<Card>();
    private ArrayList<JSONObject> couponList = new ArrayList<JSONObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ReadFromJson() {
            @Override
            protected void onPostExecute(ArrayList<JSONObject> couponList) {
                super.onPostExecute(couponList);
                new SetIcons().execute();
            }
        }.execute();
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
                if(Utils.isNetworkConnected(getApplicationContext())){
                    new UpdateCoupons(MainActivity.this, false).execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                }

                return  true;
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
                if(Utils.isNetworkConnected(getApplicationContext())){
                    new UpdateCoupons(MainActivity.this, true).execute();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                }

            }

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

            CardListView newsListView = (CardListView) findViewById(android.R.id.list);

            CustomCardAdapter adapter = new CustomCardAdapter(getApplicationContext(), couponList);
            Log.d(TAG, "List size = " + couponList.size());
                    // This sets the color displayed for card titles and header actions by default
            adapter.add(new CardHeader("All Coupons List"));
            try{
                for(JSONObject couponItem : couponList) {
                    cardList.add(new Card(couponItem.getString("code"), couponItem.getString("description")));
                    adapter.add(cardList.get(cardList.size() - 1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            newsListView.setAdapter(adapter);

        }
    }

    private class SetIcons extends AsyncTask<Void, Void, Boolean> {

        File filesDir = getApplicationContext().getFilesDir();
        File photoPath = null;
        boolean refreshIcons = false;

        @Override
        protected Boolean doInBackground(Void... v) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = null;
            try {
                for(int i = 0; i < cardList.size(); ++i) {
                    photoPath = new File(filesDir, couponList.get(i).getString("name") + ".jpg");
                    if(!photoPath.exists() && !couponList.get(i).getString("favicon").equals("NULL")){
                        refreshIcons = true;
                    } else if(photoPath.exists()){
                        bitmap = BitmapFactory.decodeFile(photoPath.getAbsolutePath(), options);
                        cardList.get(i).setThumbnail(getApplicationContext(), bitmap);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return refreshIcons;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if(b) {
                new DownloadIcons(getApplicationContext(), couponList).execute();
            }
        }
    }

}
