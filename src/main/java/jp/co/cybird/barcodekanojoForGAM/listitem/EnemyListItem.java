package jp.co.cybird.barcodekanojoForGAM.listitem;

import jp.co.cybird.barcodekanojoForGAM.core.model.ActivityModel;

public class EnemyListItem implements ListItemInterface {
    private KanojosListItem Kanojos = null;
    private int Number = -1;
    private int Status = 4;
    private String Txt;
    private ActivityModel activityModel;
    private int id;

    public EnemyListItem(String txt, int number) {
        this.Txt = txt;
        this.Number = number;
    }

    public EnemyListItem(String txt) {
        this.Txt = txt;
    }

    public EnemyListItem(KanojosListItem kanojos) {
        this.Kanojos = kanojos;
    }

    public EnemyListItem(int id2) {
        this.id = id2;
    }

    public EnemyListItem(ActivityModel activityModel2) {
        this.activityModel = activityModel2;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id2) {
        this.id = id2;
    }

    public int getNumber() {
        return this.Number;
    }

    public void setNumber(int number) {
        this.Number = number;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public String getTxt() {
        return this.Txt;
    }

    public void setTxt(String txt) {
        this.Txt = txt;
    }

    public KanojosListItem getKanojos() {
        return this.Kanojos;
    }

    public void setKanojos(KanojosListItem kanojos) {
        this.Kanojos = kanojos;
    }

    public ActivityModel getActivityModel() {
        return this.activityModel;
    }

    public void setActivityModel(ActivityModel activityModel2) {
        this.activityModel = activityModel2;
    }

    public class KanojosListItem {
        public KanojosListItem() {
        }
    }
}
