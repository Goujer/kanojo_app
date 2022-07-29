package jp.co.cybird.barcodekanojoForGAM.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import com.goujer.barcodekanojo.R;
import com.goujer.barcodekanojo.core.model.User;

public class EnemyBookAdapter extends ArrayAdapter<User> {
    private LayoutInflater inflater;
    private List<User> items;

    public EnemyBookAdapter(Context context, List<User> objects) {
        super(context, R.layout.row_enemies, objects);
        this.items = objects;
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtName;
        View view = convertView;
        int location = position;
        if (view == null) {
            view = this.inflater.inflate(R.layout.row_enemies, (ViewGroup) null);
        }
        User item = this.items.get(location);
        if (!(item == null || (txtName = (TextView) view.findViewById(R.id.row_enemies_txt)) == null)) {
            txtName.setText(String.valueOf(item.getName()) + " Lv:" + item.getLevel());
        }
        return view;
    }
}
