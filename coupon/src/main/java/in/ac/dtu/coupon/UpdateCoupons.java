package in.ac.dtu.coupon;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by omerjerk on 26/12/13.
 */
public class UpdateCoupons extends AsyncTask<Void, Void, String> {

    private Context context;
    private Boolean ranAutomatically;

    public UpdateCoupons(Context context, Boolean ranAutomatically){

        this.context = context;
        this.ranAutomatically = ranAutomatically;

        Log.d("UpdateCoupons", "Inside the constructor of UpdateCoupons class");
        if (!ranAutomatically) {
            ((Activity)context).setProgressBarIndeterminateVisibility(true);
        }
    }

    @Override
    protected String doInBackground(Void... v){

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet("http://162.243.238.19/sauravtom/coupon.txt"));
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            if (!ranAutomatically) {
                ((Activity)context).setProgressBarIndeterminateVisibility(false);
            }
            Log.d("[GET REQUEST]", "Network exception", e);
            return null;
        }
    }

    protected void onPostExecute(String r) {

        if (!ranAutomatically) {
            ((Activity)context).setProgressBarIndeterminateVisibility(false);
        }

        Log.d("[GET RESPONSE]", r);
        File cacheFile = new File(context.getFilesDir(), "data.json");


        BufferedWriter bw = null;
        try {
            if (!cacheFile.exists()) {
                cacheFile.createNewFile();
            }

            FileWriter fw = new FileWriter(cacheFile.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(r);

            Toast.makeText(context, "News refreshed!", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
                //Should never happen
            }

        }

    }
}
