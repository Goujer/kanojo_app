package jp.co.cybird.barcodekanojoForGAM.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jp.co.cybird.barcodekanojoForGAM.R;
import jp.co.cybird.barcodekanojoForGAM.core.model.User;
import jp.co.cybird.barcodekanojoForGAM.core.util.ImageCache;
import jp.co.cybird.barcodekanojoForGAM.core.util.RemoteResourceManager;

public class UserProfileView extends RelativeLayout {
    private ImageView imgPhoto = findViewById(R.id.common_profile_img);
    private Context mContext;
    private TextView txtBcoin;
    private TextView txtKanojos;
    private TextView txtLevel = findViewById(R.id.common_profile_level);
    private TextView txtName = findViewById(R.id.common_profile_name);
    private TextView txtStamina;
    private TextView txtTickets;

    public UserProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_user_profile, this, true);
        this.mContext = context;
        this.txtLevel.setVisibility(View.GONE);
        this.txtStamina = findViewById(R.id.common_profile_stamina);
        this.txtKanojos = findViewById(R.id.common_profile_kanojos);
        this.txtBcoin = findViewById(R.id.common_profile_b_coin);
        this.txtTickets = findViewById(R.id.common_profile_ticket);
        this.txtName.setSingleLine();
        this.txtName.setEllipsize(TextUtils.TruncateAt.END);
    }

    public void clear() {
        this.imgPhoto.setImageDrawable(null);
        this.mContext = null;
    }

    public void setUser(User user, RemoteResourceManager rrm) {
        if (user != null) {
            if (this.imgPhoto != null) {
                ImageCache.setImageAndRequest(this.mContext, this.imgPhoto, user.getProfile_image_url(), rrm, R.drawable.common_noimage);
            }
            if (this.txtLevel != null) {
                this.txtLevel.setText(":Lv." + user.getLevel());
            }
            if (this.txtName != null && user.getName() != "null" && user.getName() != null) {
                this.txtName.setText(user.getName() + this.txtLevel.getText().toString());
            } else if (this.txtName != null) {
                this.txtName.setText(String.valueOf(getResources().getString(R.string.blank_name)) + this.txtLevel.getText().toString());
            }
            if (this.txtStamina != null) {
                this.txtStamina.setText(String.valueOf(user.getStamina()));
            }
            if (this.txtKanojos != null) {
                this.txtKanojos.setText(String.valueOf(user.getKanojo_count()));
            }
            if (this.txtBcoin != null) {
                this.txtBcoin.setText(String.valueOf(user.getMoney()));
            }
            if (this.txtTickets != null) {
                this.txtTickets.setText(String.valueOf(user.getTickets()));
            }
        }
    }
}
