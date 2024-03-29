package jp.co.cybird.barcodekanojoForGAM.activity.enemy;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.goujer.barcodekanojo.core.cache.DynamicImageCache;

import java.util.ArrayList;
import java.util.List;
import com.goujer.barcodekanojo.BarcodeKanojoApp;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseInterface;
import jp.co.cybird.barcodekanojoForGAM.adapter.EnemyAdapter;
import com.goujer.barcodekanojo.core.model.Kanojo;
import com.goujer.barcodekanojo.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.listitem.EnemyListItem;
import jp.co.cybird.barcodekanojoForGAM.view.MoreView;
import com.goujer.barcodekanojo.view.UserProfileView;

public class EnemyActivity extends BaseActivity implements MoreView.OnMoreClickListener {
    private final int MORE_KANOJOS = 10;
    private EnemyAdapter adapter;
    private User mEnemy;
    private List<Kanojo> mEnemyKanojos;
    private ListView mListView;
    private UserProfileView mProfileView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy);
        DynamicImageCache dic = ((BarcodeKanojoApp) getApplication()).getImageCache();
        ((Button) findViewById(R.id.enemy_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnemyActivity.this.finish();
            }
        });
        this.mEnemy = (User) getIntent().getExtras().get(BaseInterface.EXTRA_USER);
        if (this.mEnemy != null) {
            this.mProfileView = (UserProfileView) findViewById(R.id.common_enemy_profile);
            this.mProfileView.setUser(this.mEnemy, dic);
            TextView txtTitle = (TextView) findViewById(R.id.row_selection_title);
            if (txtTitle != null) {
                txtTitle.setText(getResources().getString(R.string.enemy_your_enemies));
            }
            this.mListView = (ListView) findViewById(R.id.enemy_list);
            Resources r = getResources();
            List<EnemyListItem> mList = new ArrayList<>();
            int size = this.mEnemyKanojos.size();
            mList.add(new EnemyListItem(r.getString(R.string.enemy_his_enemies), size));
            for (int i = 0; i < size; i += 2) {
            }
            mList.add(new EnemyListItem(10));
            mList.add(new EnemyListItem(r.getString(R.string.enemy_activities)));
            this.mListView.setAdapter(this.adapter);
        }
    }

    @Override
	protected void onDestroy() {
    	super.onDestroy();
		mProfileView.destroy();
	}

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_enemy, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    public void onMoreClick(int id) {
    }
}
