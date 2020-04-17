package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;

public class StatsView extends LinearLayout {
    private TextView txtKanojos = ((TextView) findViewById(R.id.row_stats_kanjos));
    private TextView txtScanned = ((TextView) findViewById(R.id.row_stats_scanned));

    public StatsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(0);
        setBackgroundResource(R.drawable.row_stats_bg);
        setPadding(4, 8, 4, 8);
        LayoutInflater.from(context).inflate(R.layout.view_stats, this, true);
    }

    public void clear() {
        this.txtKanojos.setBackgroundDrawable((Drawable) null);
        this.txtScanned.setBackgroundDrawable((Drawable) null);
        setBackgroundDrawable((Drawable) null);
    }

    public void setUser(User user) {
        if (user != null) {
            if (this.txtKanojos != null) {
                this.txtKanojos.setText(new StringBuilder().append(user.getKanojo_count()).toString());
            }
            if (this.txtScanned != null) {
                this.txtScanned.setText(new StringBuilder().append(user.getScan_count()).toString());
            }
        }
    }
}
