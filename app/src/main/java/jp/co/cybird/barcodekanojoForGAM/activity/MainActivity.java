package jp.co.cybird.barcodekanojoForGAM.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.view.CustomTabView;

public class MainActivity extends TabActivity implements BaseInterface {
    private Resources r;
    TabHost tabHost;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        this.r = getResources();
        this.tabHost = getTabHost();
        this.tabHost.addTab(this.tabHost.newTabSpec("tab_dashboard").setIndicator(new CustomTabView(this, this.r.getString(R.string.tab_dashboard), R.drawable.tab_icon_account)).setContent(new Intent().setClass(this, DashboardActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec("tab_kanojo").setIndicator(new CustomTabView(this, this.r.getString(R.string.tab_kanojo), R.drawable.tab_icon_kanojos)).setContent(new Intent().setClass(this, KanojosActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec("tab_scan").setIndicator(new CustomTabView(this, this.r.getString(R.string.tab_scan), R.drawable.tab_icon_scan)).setContent(new Intent().setClass(this, DashboardActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec("tab_enemy").setIndicator(new CustomTabView(this, this.r.getString(R.string.tab_enemy), R.drawable.tab_icon_enemybook)).setContent(new Intent().setClass(this, EnemyBookActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec("tab_map").setIndicator(new CustomTabView(this, this.r.getString(R.string.tab_map), R.drawable.tab_icon_map)).setContent(new Intent().setClass(this, MapKanojosActivity.class)));
        this.tabHost.setCurrentTab(0);
        this.tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {
                if (tabId.equals("tab_scan")) {
                    MainActivity.this.startActivityForResult(new Intent().setClass(MainActivity.this, ScanActivity.class), BaseInterface.REQUEST_SCAN);
                    MainActivity.this.tabHost.setCurrentTab(0);
                }
            }
        });
    }
}
