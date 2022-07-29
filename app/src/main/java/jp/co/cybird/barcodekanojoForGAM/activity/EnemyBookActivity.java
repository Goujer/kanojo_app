package jp.co.cybird.barcodekanojoForGAM.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.activity.base.BaseActivity;

public class EnemyBookActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_book);
        TextView txtTitle = (TextView) findViewById(R.id.row_selection_title);
        if (txtTitle != null) {
            txtTitle.setText(getResources().getString(R.string.enemy_your_enemies));
        }
    }

    public View getClientView() {
        View leyout = getLayoutInflater().inflate(R.layout.activity_enemy_book, (ViewGroup) null);
        LinearLayout appLayoutRoot = new LinearLayout(this);
        appLayoutRoot.addView(leyout);
        return appLayoutRoot;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    }
}
