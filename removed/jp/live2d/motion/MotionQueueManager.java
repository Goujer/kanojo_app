package jp.live2d.motion;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import jp.live2d.ALive2DModel;
import jp.live2d.util.UtSystem;

public class MotionQueueManager {
    ArrayList<MotionQueueEnt> motions = new ArrayList<>();

    public int startMotion(AMotion motion, boolean autoDelete) {
        AMotion sameMotion = null;
        MotionQueueEnt ent = null;
        int len = this.motions.size();
        for (int i = 0; i < len; i++) {
            ent = this.motions.get(i);
            if (ent != null) {
                if (ent.motion == motion) {
                    sameMotion = motion;
                } else {
                    ent.startFadeout((long) ent.motion.getFadeOut());
                }
            }
        }
        if (sameMotion != null && ent != null) {
            sameMotion.reinit();
            return ent.motionQueueEntNo;
        } else if (motion == null) {
            return -1;
        } else {
            motion.reinit();
            MotionQueueEnt ent2 = new MotionQueueEnt();
            ent2.motion = motion;
            this.motions.add(ent2);
            return ent2.motionQueueEntNo;
        }
    }

    public boolean updateParam(ALive2DModel model) {
        AMotion motion;
        boolean updated = false;
        try {
            Iterator<MotionQueueEnt> ite = this.motions.iterator();
            while (ite.hasNext()) {
                MotionQueueEnt ent = ite.next();
                if (!(ent == null || (motion = ent.motion) == null)) {
                    motion.updateParam(model, ent);
                    updated = true;
                    if (ent.isFinished()) {
                        ite.remove();
                    }
                }
            }
            return updated;
        } catch (ConcurrentModificationException e) {
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return true;
        }
    }

    public boolean isFinished() {
        Iterator<MotionQueueEnt> ite = this.motions.iterator();
        while (ite.hasNext()) {
            MotionQueueEnt ent = ite.next();
            if (ent != null && ent.motion != null && !ent.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFinished(int _motionQueueEntNo) {
        Iterator<MotionQueueEnt> ite = this.motions.iterator();
        while (ite.hasNext()) {
            MotionQueueEnt ent = ite.next();
            if (ent != null && ent.motionQueueEntNo == _motionQueueEntNo && !ent.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public void DUMP() {
        System.out.printf("-- Dump --\n", new Object[0]);
        for (int i = 0; i < this.motions.size(); i++) {
            AMotion am = this.motions.get(i).motion;
            System.out.printf("MotionQueueEnt[%d] :: %s\n", new Object[]{Integer.valueOf(this.motions.size()), am.toString()});
        }
    }

    public static class MotionQueueEnt {
        static int static_motionQueueEntNo = 0;
        boolean available = true;
        long endTimeMSec = -1;
        long fadeInStartTimeMSec = -1;
        boolean finished = false;
        AMotion motion = null;
        int motionQueueEntNo;
        long startTimeMSec = -1;

        MotionQueueEnt() {
            int i = static_motionQueueEntNo;
            static_motionQueueEntNo = i + 1;
            this.motionQueueEntNo = i;
        }

        /* access modifiers changed from: package-private */
        public boolean isFinished() {
            return this.finished;
        }

        public void startFadeout(long fadeOutMsec) {
            if (this.endTimeMSec <= 0) {
                this.endTimeMSec = UtSystem.getUserTimeMSec() + fadeOutMsec;
            }
        }
    }
}
