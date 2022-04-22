package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import jp.co.cybird.barcodekanojoForGAM.R;
import com.goujer.barcodekanojo.core.model.User;

public class DashboardHeaderView extends LinearLayout {
    private final StatsView statsView;

    public DashboardHeaderView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.view_dashboard_header, this, true);
		statsView = findViewById(R.id.dashboard_header_stats);
    }

    public void clear() {
        this.statsView.clear();
        setBackgroundDrawable(null);
    }

    public void setUser(User user) {
        this.statsView.setUser(user);
    }
}
