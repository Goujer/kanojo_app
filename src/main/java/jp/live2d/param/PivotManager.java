package jp.live2d.param;

import java.util.ArrayList;
import jp.live2d.Live2D;
import jp.live2d.ModelContext;
import jp.live2d.id.ParamID;
import jp.live2d.io.BReader;
import jp.live2d.io.ISerializableV2;

public class PivotManager implements ISerializableV2 {
    ArrayList<ParamPivots> paramPivotTable = null;

    public void initDirect() {
        this.paramPivotTable = new ArrayList<>();
    }

    public void readV2(BReader br) throws Exception {
        this.paramPivotTable = (ArrayList) br.readObject();
    }

    public boolean checkParamUpdated(ModelContext mdc) {
        if (mdc.requireSetup()) {
            return true;
        }
        int initVersion = mdc.getInitVersion();
        for (int i = this.paramPivotTable.size() - 1; i >= 0; i--) {
            int index = this.paramPivotTable.get(i).getParamIndex(initVersion);
            if (index == -2) {
                index = mdc.getParamIndex(this.paramPivotTable.get(i).getParamID());
            }
            if (mdc.isParamUpdated(index)) {
                return true;
            }
        }
        return false;
    }

    public int calcPivotValue(ModelContext mdc, boolean[] ret_outsideParam) throws Exception {
        float v0;
        int paramCount = this.paramPivotTable.size();
        int initVersion = mdc.getInitVersion();
        int interpolateCount = 0;
        for (int pi = 0; pi < paramCount; pi++) {
            ParamPivots pp = this.paramPivotTable.get(pi);
            int tmpParamIndex = pp.getParamIndex(initVersion);
            if (tmpParamIndex == -2) {
                tmpParamIndex = mdc.getParamIndex(pp.getParamID());
                pp.setParamIndex_(tmpParamIndex, initVersion);
            }
            if (tmpParamIndex < 0) {
                throw new Exception("PivotManager#calcPivotValue() :tmpParamIndex < 0 " + pp.getParamID());
            }
            if (tmpParamIndex < 0) {
                v0 = 0.0f;
            } else {
                v0 = mdc.getParamFloat(tmpParamIndex);
            }
            int tmpPivotCount = pp.getPivotCount();
            float[] pivotValue = pp.getPivotValue();
            int tmpPivotIndex = -1;
            float tmpT = 0.0f;
            if (tmpPivotCount >= 1) {
                if (tmpPivotCount == 1) {
                    float lastv = pivotValue[0];
                    if (lastv - 1.0E-4f >= v0 || v0 >= 1.0E-4f + lastv) {
                        tmpPivotIndex = 0;
                        ret_outsideParam[0] = true;
                    } else {
                        tmpPivotIndex = 0;
                        tmpT = 0.0f;
                    }
                } else {
                    float lastv2 = pivotValue[0];
                    if (v0 < lastv2 - 1.0E-4f) {
                        tmpPivotIndex = 0;
                        ret_outsideParam[0] = true;
                    } else {
                        boolean found = false;
                        int piv = 1;
                        while (true) {
                            if (piv >= tmpPivotCount) {
                                break;
                            }
                            float curv = pivotValue[piv];
                            if (v0 < 1.0E-4f + curv) {
                                if (curv - 1.0E-4f < v0) {
                                    tmpPivotIndex = piv;
                                } else {
                                    tmpPivotIndex = piv - 1;
                                    tmpT = (v0 - lastv2) / (curv - lastv2);
                                    interpolateCount++;
                                }
                                found = true;
                            } else {
                                lastv2 = curv;
                                piv++;
                            }
                        }
                        if (!found) {
                            tmpPivotIndex = tmpPivotCount - 1;
                            tmpT = 0.0f;
                            ret_outsideParam[0] = true;
                        }
                    }
                }
            }
            pp.setTmpPivotIndex(tmpPivotIndex);
            pp.setTmpT(tmpT);
        }
        return interpolateCount;
    }

    public void calcPivotIndexies(byte[] array64, float[] tmpT_array, int interpolateCount) throws Exception {
        int tc;
        byte b;
        int pivot_indecies = 1 << interpolateCount;
        if (pivot_indecies + 1 > 65) {
            System.out.printf("=================================================\n", new Object[0]);
            System.out.printf("想定サイズを超える補間回数です %d\n ", new Object[]{Integer.valueOf(pivot_indecies)});
            System.out.printf("Live2D.h / PIVOT_TABLE_SIZE を大きくして下さい\n", new Object[0]);
            System.out.printf("=================================================\n", new Object[0]);
        }
        int paramCount = this.paramPivotTable.size();
        int offset = 1;
        int div = 1;
        for (int pvi = 0; pvi < pivot_indecies; pvi++) {
            array64[pvi] = 0;
        }
        int pi = 0;
        int tc2 = 0;
        while (pi < paramCount) {
            ParamPivots pp = this.paramPivotTable.get(pi);
            if (pp.getTmpT() == 0.0f) {
                int index1 = pp.getTmpPivotIndex() * offset;
                if (index1 >= 0 || !Live2D.L2D_DEBUG) {
                    for (int pvi2 = 0; pvi2 < pivot_indecies; pvi2++) {
                        array64[pvi2] = (byte) (array64[pvi2] + index1);
                    }
                    tc = tc2;
                } else {
                    throw new Exception("PivotManager#calcPivotIndexies()より前に tmpPivotIndexが適切に設定されていません");
                }
            } else {
                int index12 = offset * pp.getTmpPivotIndex();
                int index2 = offset * (pp.getTmpPivotIndex() + 1);
                for (int pvi3 = 0; pvi3 < pivot_indecies; pvi3++) {
                    byte b2 = array64[pvi3];
                    if ((pvi3 / div) % 2 == 0) {
                        b = index12;
                    } else {
                        b = index2;
                    }
                    array64[pvi3] = (byte) (b + b2);
                }
                tc = tc2 + 1;
                tmpT_array[tc2] = pp.getTmpT();
                div *= 2;
            }
            offset *= pp.getPivotCount();
            pi++;
            tc2 = tc;
        }
        array64[pivot_indecies] = -1;
        tmpT_array[tc2] = -1.0f;
    }

    public void addParamPivots(ParamID paramID, int pivotCount, float[] pivots) {
        float[] pv = new float[pivotCount];
        for (int i = 0; i < pivotCount; i++) {
            pv[i] = pivots[i];
        }
        ParamPivots pp = new ParamPivots();
        pp.setParamID(paramID);
        pp.setPivotValue(pivotCount, pv);
        this.paramPivotTable.add(pp);
    }

    public void DUMP_PIVOT_STR(int pivotArrayNo) {
        int no = pivotArrayNo;
        int paramCount = this.paramPivotTable.size();
        for (int i = 0; i < paramCount; i++) {
            ParamPivots pp = this.paramPivotTable.get(i);
            int pivotCount = pp.getPivotCount();
            int index = no % pp.getPivotCount();
            float value = pp.getPivotValue()[index];
            System.out.printf("%s[%d]=%7.2f / ", new Object[]{pp.getParamID(), Integer.valueOf(index), Float.valueOf(value)});
            no /= pivotCount;
        }
        System.out.printf("\n", new Object[0]);
    }
}
