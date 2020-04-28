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
    private TextView txtKanojos;
    private TextView txtScanned;

    public StatsView(Context context, AttributeSet attrs) {
        super(context, attrs);

		txtKanojos = findViewById(R.id.row_stats_kanjos);
		txtScanned = findViewById(R.id.row_stats_scanned);

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundResource(R.drawable.row_stats_bg);
        setPadding(4, 8, 4, 8);
        LayoutInflater.from(context).inflate(R.layout.view_stats, this, true);
    }

    public void clear() {
        this.txtKanojos.setBackgroundDrawable(null);
        this.txtScanned.setBackgroundDrawable(null);
        setBackgroundDrawable(null);
    }

    public void setUser(User user) {
        if (user != null) {
            if (this.txtKanojos != null) {
                this.txtKanojos.setText(String.valueOf(user.getKanojo_count()));
            }
            if (this.txtScanned != null) {
                this.txtScanned.setText(String.valueOf(user.getScan_count()));
            }
        }
    }
}
