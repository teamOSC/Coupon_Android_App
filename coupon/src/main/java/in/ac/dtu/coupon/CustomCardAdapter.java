package in.ac.dtu.coupon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.tosc.coupon.R;

/**
 * Created by omerjerk on 26/12/13.
 */
public class CustomCardAdapter extends RecyclerView.Adapter<CustomCardAdapter.ViewHolder> {

    private ArrayList<JSONObject> couponList;
    private Context context;
    private LayoutInflater mInflater;

    public CustomCardAdapter( Context context, ArrayList<JSONObject> couponList) {
        this.couponList = couponList;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public Drawable getVendorIcon(String vendorName) {
        vendorName = vendorName.toLowerCase();
        int vendorIconId = context.getResources().getIdentifier(vendorName, "drawable", "in.tosc.coupon");
        Drawable vendorIcon = context.getResources().getDrawable(vendorIconId);
        return vendorIcon;
    }

    public boolean haveVendorIcon(String vendorName) {
        String[] vendorArray = context.getResources().getStringArray(R.array.vendors);
        for (int i = 0; i < vendorArray.length; i++) {
            if (vendorArray[i].equalsIgnoreCase(vendorName)) {
                return true;
            }
        }
        return false;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        CardView view = (CardView) LayoutInflater.from(context)
                .inflate(R.layout.view_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            String vendorName = couponList.get(position).getString("name");
            holder.couponName.setText("WEBSITE : " + vendorName);
            holder.couponName.setTextColor(Color.parseColor("#444444"));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(context,
                            "Long press this card to go to website to redeem coupon",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            holder.root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Uri uri = null;
                    try {
                        uri = Uri.parse(couponList.get(position).getString("url"));
                    } catch (JSONException e) {
                        uri = Uri.parse("http://google.com");
                        e.printStackTrace();
                    }
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
                    webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(webIntent);
                    return false;
                }
            });


            if (haveVendorIcon(vendorName)) {
                holder.vendorIcon.setImageDrawable(getVendorIcon(vendorName));
            } else {
                holder.vendorIcon.setImageDrawable(getVendorIcon("none"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView couponName;
        public TextView couponDescription;
        public ImageView vendorIcon;
        public CardView root;

        public ViewHolder(CardView view) {
            super(view);
            root = view;
            couponName = (TextView) view.findViewById(R.id.coupon_name);
            couponDescription = (TextView) view.findViewById(R.id.coupon_description);
            vendorIcon = (ImageView) view.findViewById(R.id.vendor_icon);
        }
    }

}
