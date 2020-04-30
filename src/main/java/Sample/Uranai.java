package Sample;

import java.util.Date;
import java.util.Random;

public class Uranai {
    public static int getUranaiToday(int key, int uranaiNo, int itemCount) {
        Date tmp = new Date();
        return getUranai(key, uranaiNo, new Date(tmp.getYear(), tmp.getDate(), tmp.getDay()), itemCount);
    }

    public static int getUranai(int key, int uranaiNo, Date today, int itemCount) {
        int v = new Random(new Random(today.hashCode() * 154).nextInt() + new Random(154064 * uranaiNo).nextInt() + new Random((long) (678464065 * key)).nextInt()).nextInt();
        if (v < 0) {
            v = -v;
        }
        System.out.printf("v : %s\t\t\t\t\t@Uranai\n", new Object[]{new StringBuilder().append(v).toString()});
        return v % itemCount;
    }
}
