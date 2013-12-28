package in.ac.dtu.coupon;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by omerjerk on 27/12/13.
 */
public class DownloadIcons extends AsyncTask <Void, Void, Void> {

    private static final String TAG = "DownloadIcons";

    private Context context;
    private ArrayList<JSONObject> couponList;

    public DownloadIcons(Context context, ArrayList<JSONObject> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    @Override
    protected Void doInBackground(Void... v) {

        FileOutputStream mFileOutputStream = null;
        Bitmap mBitmap;
        int i =0;
        try {
            for(JSONObject coupon : couponList) {
                ++i;
                if(!coupon.getString("favicon").equals("NULL")) {
                    if(i != 15 && i != 20 && i != 28 && i != 33){
                    URL url = new URL(coupon.getString("favicon"));
                    File file = new File(context.getFilesDir() + File.separator + coupon.getString("name") + ".jpg");
                    if(!file.exists()){
                        file.createNewFile();
                         mBitmap = getBitmapFromURL(url);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        if(mBitmap != null) {

                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            mFileOutputStream = new FileOutputStream(file);
                            mFileOutputStream.write(bytes.toByteArray());
                        } else {
                            Log.d(TAG, "Downloaded bitmap is NULL");
                        }
                        mFileOutputStream.close(); }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSONException and Index = " + i);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d(TAG, "MalformedURLException and Index = " + i);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "IOException and Index = " + i);
        }

        return null;
    }

    public static Bitmap getBitmapFromURL(URL url) {
    /** This method downloads an Image from the given URL,
     *  then decodes and returns a Bitmap object
     */
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
