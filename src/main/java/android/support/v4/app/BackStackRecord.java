package android.support.v4.app;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, Runnable {
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SHOW = 5;
    static final String TAG = "BackStackEntry";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack = true;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    Op mHead;
    int mIndex;
    final FragmentManagerImpl mManager;
    String mName;
    int mNumOp;
    int mPopEnterAnim;
    int mPopExitAnim;
    Op mTail;
    int mTransition;
    int mTransitionStyle;

    static final class Op {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        Op next;
        int popEnterAnim;
        int popExitAnim;
        Op prev;
        ArrayList<Fragment> removed;

        Op() {
        }
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        writer.print(prefix);
        writer.print("mName=");
        writer.print(this.mName);
        writer.print(" mIndex=");
        writer.print(this.mIndex);
        writer.print(" mCommitted=");
        writer.println(this.mCommitted);
        if (this.mTransition != 0) {
            writer.print(prefix);
            writer.print("mTransition=#");
            writer.print(Integer.toHexString(this.mTransition));
            writer.print(" mTransitionStyle=#");
            writer.println(Integer.toHexString(this.mTransitionStyle));
        }
        if (!(this.mEnterAnim == 0 && this.mExitAnim == 0)) {
            writer.print(prefix);
            writer.print("mEnterAnim=#");
            writer.print(Integer.toHexString(this.mEnterAnim));
            writer.print(" mExitAnim=#");
            writer.println(Integer.toHexString(this.mExitAnim));
        }
        if (!(this.mPopEnterAnim == 0 && this.mPopExitAnim == 0)) {
            writer.print(prefix);
            writer.print("mPopEnterAnim=#");
            writer.print(Integer.toHexString(this.mPopEnterAnim));
            writer.print(" mPopExitAnim=#");
            writer.println(Integer.toHexString(this.mPopExitAnim));
        }
        if (!(this.mBreadCrumbTitleRes == 0 && this.mBreadCrumbTitleText == null)) {
            writer.print(prefix);
            writer.print("mBreadCrumbTitleRes=#");
            writer.print(Integer.toHexString(this.mBreadCrumbTitleRes));
            writer.print(" mBreadCrumbTitleText=");
            writer.println(this.mBreadCrumbTitleText);
        }
        if (!(this.mBreadCrumbShortTitleRes == 0 && this.mBreadCrumbShortTitleText == null)) {
            writer.print(prefix);
            writer.print("mBreadCrumbShortTitleRes=#");
            writer.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
            writer.print(" mBreadCrumbShortTitleText=");
            writer.println(this.mBreadCrumbShortTitleText);
        }
        if (this.mHead != null) {
            writer.print(prefix);
            writer.println("Operations:");
            String innerPrefix = prefix + "    ";
            for (Op op = this.mHead; op != null; op = op.next) {
                writer.print(prefix);
                writer.print("  Op #");
                writer.print(0);
                writer.println(":");
                writer.print(innerPrefix);
                writer.print("cmd=");
                writer.print(op.cmd);
                writer.print(" fragment=");
                writer.println(op.fragment);
                if (!(op.enterAnim == 0 && op.exitAnim == 0)) {
                    writer.print(prefix);
                    writer.print("enterAnim=#");
                    writer.print(Integer.toHexString(op.enterAnim));
                    writer.print(" exitAnim=#");
                    writer.println(Integer.toHexString(op.exitAnim));
                }
                if (!(op.popEnterAnim == 0 && op.popExitAnim == 0)) {
                    writer.print(prefix);
                    writer.print("popEnterAnim=#");
                    writer.print(Integer.toHexString(op.popEnterAnim));
                    writer.print(" popExitAnim=#");
                    writer.println(Integer.toHexString(op.popExitAnim));
                }
                if (op.removed != null && op.removed.size() > 0) {
                    for (int i = 0; i < op.removed.size(); i++) {
                        writer.print(innerPrefix);
                        if (op.removed.size() == 1) {
                            writer.print("Removed: ");
                        } else {
                            writer.println("Removed:");
                            writer.print(innerPrefix);
                            writer.print("  #");
                            writer.print(0);
                            writer.print(": ");
                        }
                        writer.println(op.removed.get(i));
                    }
                }
            }
        }
    }

    public BackStackRecord(FragmentManagerImpl manager) {
        this.mManager = manager;
    }

    public int getId() {
        return this.mIndex;
    }

    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }

    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }

    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mActivity.getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }

    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mActivity.getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }

    /* access modifiers changed from: package-private */
    public void addOp(Op op) {
        if (this.mHead == null) {
            this.mTail = op;
            this.mHead = op;
        } else {
            op.prev = this.mTail;
            this.mTail.next = op;
            this.mTail = op;
        }
        op.enterAnim = this.mEnterAnim;
        op.exitAnim = this.mExitAnim;
        op.popEnterAnim = this.mPopEnterAnim;
        op.popExitAnim = this.mPopExitAnim;
        this.mNumOp++;
    }

    public FragmentTransaction add(Fragment fragment, String tag) {
        doAddOp(0, fragment, tag, 1);
        return this;
    }

    public FragmentTransaction add(int containerViewId, Fragment fragment) {
        doAddOp(containerViewId, fragment, (String) null, 1);
        return this;
    }

    public FragmentTransaction add(int containerViewId, Fragment fragment, String tag) {
        doAddOp(containerViewId, fragment, tag, 1);
        return this;
    }

    private void doAddOp(int containerViewId, Fragment fragment, String tag, int opcmd) {
        fragment.mFragmentManager = this.mManager;
        if (tag != null) {
            if (fragment.mTag == null || tag.equals(fragment.mTag)) {
                fragment.mTag = tag;
            } else {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + tag);
            }
        }
        if (containerViewId != 0) {
            if (fragment.mFragmentId == 0 || fragment.mFragmentId == containerViewId) {
                fragment.mFragmentId = containerViewId;
                fragment.mContainerId = containerViewId;
            } else {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + containerViewId);
            }
        }
        Op op = new Op();
        op.cmd = opcmd;
        op.fragment = fragment;
        addOp(op);
    }

    public FragmentTransaction replace(int containerViewId, Fragment fragment) {
        return replace(containerViewId, fragment, (String) null);
    }

    public FragmentTransaction replace(int containerViewId, Fragment fragment, String tag) {
        if (containerViewId == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        doAddOp(containerViewId, fragment, tag, 2);
        return this;
    }

    public FragmentTransaction remove(Fragment fragment) {
        Op op = new Op();
        op.cmd = 3;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction hide(Fragment fragment) {
        Op op = new Op();
        op.cmd = 4;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction show(Fragment fragment) {
        Op op = new Op();
        op.cmd = 5;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction detach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 6;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction attach(Fragment fragment) {
        Op op = new Op();
        op.cmd = 7;
        op.fragment = fragment;
        addOp(op);
        return this;
    }

    public FragmentTransaction setCustomAnimations(int enter, int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    public FragmentTransaction setCustomAnimations(int enter, int exit, int popEnter, int popExit) {
        this.mEnterAnim = enter;
        this.mExitAnim = exit;
        this.mPopEnterAnim = popEnter;
        this.mPopExitAnim = popExit;
        return this;
    }

    public FragmentTransaction setTransition(int transition) {
        this.mTransition = transition;
        return this;
    }

    public FragmentTransaction setTransitionStyle(int styleRes) {
        this.mTransitionStyle = styleRes;
        return this;
    }

    public FragmentTransaction addToBackStack(String name) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        this.mAddToBackStack = true;
        this.mName = name;
        return this;
    }

    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }

    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(int res) {
        this.mBreadCrumbTitleRes = res;
        this.mBreadCrumbTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbTitle(CharSequence text) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = text;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(int res) {
        this.mBreadCrumbShortTitleRes = res;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }

    public FragmentTransaction setBreadCrumbShortTitle(CharSequence text) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = text;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void bumpBackStackNesting(int amt) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v(TAG, "Bump nesting in " + this + " by " + amt);
            }
            for (Op op = this.mHead; op != null; op = op.next) {
                if (op.fragment != null) {
                    op.fragment.mBackStackNesting += amt;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v(TAG, "Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting);
                    }
                }
                if (op.removed != null) {
                    for (int i = op.removed.size() - 1; i >= 0; i--) {
                        Fragment r = op.removed.get(i);
                        r.mBackStackNesting += amt;
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v(TAG, "Bump nesting of " + r + " to " + r.mBackStackNesting);
                        }
                    }
                }
            }
        }
    }

    public int commit() {
        return commitInternal(false);
    }

    public int commitAllowingStateLoss() {
        return commitInternal(true);
    }

    /* access modifiers changed from: package-private */
    public int commitInternal(boolean allowStateLoss) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Commit: " + this);
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        } else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction(this, allowStateLoss);
        return this.mIndex;
    }

    public void run() {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "Run: " + this);
        }
        if (!this.mAddToBackStack || this.mIndex >= 0) {
            bumpBackStackNesting(1);
            for (Op op = this.mHead; op != null; op = op.next) {
                switch (op.cmd) {
                    case 1:
                        Fragment f = op.fragment;
                        f.mNextAnim = op.enterAnim;
                        this.mManager.addFragment(f, false);
                        break;
                    case 2:
                        Fragment f2 = op.fragment;
                        if (this.mManager.mAdded != null) {
                            for (int i = 0; i < this.mManager.mAdded.size(); i++) {
                                Fragment old = this.mManager.mAdded.get(i);
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v(TAG, "OP_REPLACE: adding=" + f2 + " old=" + old);
                                }
                                if (f2 == null || old.mContainerId == f2.mContainerId) {
                                    if (old == f2) {
                                        f2 = null;
                                        op.fragment = null;
                                    } else {
                                        if (op.removed == null) {
                                            op.removed = new ArrayList<>();
                                        }
                                        op.removed.add(old);
                                        old.mNextAnim = op.exitAnim;
                                        if (this.mAddToBackStack) {
                                            old.mBackStackNesting++;
                                            if (FragmentManagerImpl.DEBUG) {
                                                Log.v(TAG, "Bump nesting of " + old + " to " + old.mBackStackNesting);
                                            }
                                        }
                                        this.mManager.removeFragment(old, this.mTransition, this.mTransitionStyle);
                                    }
                                }
                            }
                        }
                        if (f2 == null) {
                            break;
                        } else {
                            f2.mNextAnim = op.enterAnim;
                            this.mManager.addFragment(f2, false);
                            break;
                        }
                    case 3:
                        Fragment f3 = op.fragment;
                        f3.mNextAnim = op.exitAnim;
                        this.mManager.removeFragment(f3, this.mTransition, this.mTransitionStyle);
                        break;
                    case 4:
                        Fragment f4 = op.fragment;
                        f4.mNextAnim = op.exitAnim;
                        this.mManager.hideFragment(f4, this.mTransition, this.mTransitionStyle);
                        break;
                    case 5:
                        Fragment f5 = op.fragment;
                        f5.mNextAnim = op.enterAnim;
                        this.mManager.showFragment(f5, this.mTransition, this.mTransitionStyle);
                        break;
                    case 6:
                        Fragment f6 = op.fragment;
                        f6.mNextAnim = op.exitAnim;
                        this.mManager.detachFragment(f6, this.mTransition, this.mTransitionStyle);
                        break;
                    case 7:
                        Fragment f7 = op.fragment;
                        f7.mNextAnim = op.enterAnim;
                        this.mManager.attachFragment(f7, this.mTransition, this.mTransitionStyle);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
                }
            }
            this.mManager.moveToState(this.mManager.mCurState, this.mTransition, this.mTransitionStyle, true);
            if (this.mAddToBackStack) {
                this.mManager.addBackStackState(this);
                return;
            }
            return;
        }
        throw new IllegalStateException("addToBackStack() called after commit()");
    }

    public void popFromBackStack(boolean doStateMove) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v(TAG, "popFromBackStack: " + this);
        }
        bumpBackStackNesting(-1);
        for (Op op = this.mTail; op != null; op = op.prev) {
            switch (op.cmd) {
                case 1:
                    Fragment f = op.fragment;
                    f.mNextAnim = op.popExitAnim;
                    this.mManager.removeFragment(f, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 2:
                    Fragment f2 = op.fragment;
                    if (f2 != null) {
                        f2.mNextAnim = op.popExitAnim;
                        this.mManager.removeFragment(f2, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    }
                    if (op.removed == null) {
                        break;
                    } else {
                        for (int i = 0; i < op.removed.size(); i++) {
                            Fragment old = op.removed.get(i);
                            old.mNextAnim = op.popEnterAnim;
                            this.mManager.addFragment(old, false);
                        }
                        break;
                    }
                case 3:
                    Fragment f3 = op.fragment;
                    f3.mNextAnim = op.popEnterAnim;
                    this.mManager.addFragment(f3, false);
                    break;
                case 4:
                    Fragment f4 = op.fragment;
                    f4.mNextAnim = op.popEnterAnim;
                    this.mManager.showFragment(f4, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 5:
                    Fragment f5 = op.fragment;
                    f5.mNextAnim = op.popExitAnim;
                    this.mManager.hideFragment(f5, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 6:
                    Fragment f6 = op.fragment;
                    f6.mNextAnim = op.popEnterAnim;
                    this.mManager.attachFragment(f6, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                case 7:
                    Fragment f7 = op.fragment;
                    f7.mNextAnim = op.popEnterAnim;
                    this.mManager.detachFragment(f7, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
            }
        }
        if (doStateMove) {
            this.mManager.moveToState(this.mManager.mCurState, FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle, true);
        }
        if (this.mIndex >= 0) {
            this.mManager.freeBackStackIndex(this.mIndex);
            this.mIndex = -1;
        }
    }

    public String getName() {
        return this.mName;
    }

    public int getTransition() {
        return this.mTransition;
    }

    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }

    public boolean isEmpty() {
        return this.mNumOp == 0;
    }
}
