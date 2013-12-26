package in.ac.dtu.coupon;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.cardsui.Card;
import com.afollestad.cardsui.CardAdapter;
import com.afollestad.silk.images.SilkImageManager;
import com.afollestad.silk.views.image.SilkImageView;

/**
 * Created by omerjerk on 26/12/13.
 */
public class CustomCardAdapter extends CardAdapter<Card> {

    public CustomCardAdapter(Context context) {
        super(context, R.layout.card_layout); // the custom card layout is passed to the super constructor instead of every individual card
        setAccentColorRes(17170455);
    }

    @Override
    protected boolean onProcessThumbnail(ImageView icon, Card card) {
        // Optional, you can modify properties of the icon ImageView here.
        return super.onProcessThumbnail(icon, card);
    }

    @Override
    protected boolean onProcessTitle(TextView title, Card card, int accentColor) {
        // Optional, you can modify properties of the title textview here.
        return super.onProcessTitle(title, card, accentColor);
    }

    @Override
    protected boolean onProcessContent(TextView content, Card card) {
        // Optional, you can modify properties of the content textview here.
        return super.onProcessContent(content, card);
    }

    @Override
    protected boolean onProcessMenu(View view, final Card card) {
        // Sets up a card's menu (custom_card.xml makes this a star)
        return super.onProcessMenu(view, card);
    }

    @Override
    public View onViewCreated(int index, View recycled, Card item) {
        // Optional, you can modify properties of other views that you add to the card layout that aren't the icon, title, content...
        return super.onViewCreated(index, recycled, item);
    }

}
