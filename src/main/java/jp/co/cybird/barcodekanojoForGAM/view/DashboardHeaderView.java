package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;

public class DashboardHeaderView extends LinearLayout {
    private StatsView statsView = ((StatsView) findViewById(R.id.dashboard_header_stats));

    public DashboardHeaderView(Context context) {
        super(context);
        setOrientation(1);
        LayoutInflater.from(context).inflate(R.layout.view_dashboard_header, this, true);
    }

    public void clear() {
        this.statsView.clear();
        setBackgroundDrawable((Drawable) null);
    }

    public void setUser(User user) {
        this.statsView.setUser(user);
    }
}
