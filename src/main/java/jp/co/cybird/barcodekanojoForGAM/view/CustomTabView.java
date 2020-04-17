package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;

public class CustomTabView extends FrameLayout {
    LayoutInflater inflater;

    public CustomTabView(Context context) {
        super(context);
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public CustomTabView(Context context, String title, int icon) {
        this(context);
        View childview = this.inflater.inflate(R.layout.tabwidget, (ViewGroup) null);
        ((TextView) childview.findViewById(R.id.tab_text)).setText(title);
        ((ImageView) childview.findViewById(R.id.tab_icon)).setImageResource(icon);
        addView(childview);
    }
}
