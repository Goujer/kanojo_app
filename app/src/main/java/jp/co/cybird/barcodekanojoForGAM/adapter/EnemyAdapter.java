package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import com.goujer.barcodekanojo.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;
import jp.co.cybird.barcodekanojoForGAM.listitem.EnemyListItem;
import jp.co.cybird.barcodekanojoForGAM.listitem.ListItemInterface;
import jp.co.cybird.barcodekanojoForGAM.view.MoreView;

public class EnemyAdapter extends ArrayAdapter<EnemyListItem> implements ListItemInterface {
    private LayoutInflater inflater;
    private List<EnemyListItem> items;
    private MoreView.OnMoreClickListener moreListener;

    public EnemyAdapter(Context context, List<EnemyListItem> objects, MoreView.OnMoreClickListener ml) {
        super(context, R.layout.row_enemies, objects);
        this.items = objects;
        this.moreListener = ml;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ActivityModel a;
        View view = convertView;
        EnemyListItem item = this.items.get(position);
        if (item == null) {
            return view;
        }
        switch (item.getStatus()) {
            case 0:
                View view2 = this.inflater.inflate(R.layout.row_selection, (ViewGroup) null);
                TextView txtTitle = (TextView) view2.findViewById(R.id.row_selection_title);
                if (txtTitle != null) {
                    txtTitle.setText(item.getTxt());
                }
                TextView txtNumber = (TextView) view2.findViewById(R.id.row_selection_number);
                int num = item.getNumber();
                if (txtNumber == null || num == -1) {
                    return view2;
                }
                txtNumber.setText(new StringBuilder().append(num).toString());
                return view2;
            case 3:
                return this.inflater.inflate(R.layout.row_kanojos, (ViewGroup) null);
            case 4:
                View view3 = this.inflater.inflate(R.layout.row_more, (ViewGroup) null);
                MoreView moreView = (MoreView) view3.findViewById(R.id.row_more);
                if (moreView == null) {
                    return view3;
                }
                moreView.setId(item.getId(), this.moreListener);
                return view3;
            default:
                View view4 = this.inflater.inflate(R.layout.row_activities, (ViewGroup) null);
                TextView txtActivity = (TextView) view4.findViewById(R.id.row_activities_txt);
                if (txtActivity == null || (a = item.getActivityModel()) == null) {
                    return view4;
                }
                txtActivity.setText(a.getActivity());
                return view4;
        }
    }
}
