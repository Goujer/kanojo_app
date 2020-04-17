package android.support.v4.os;

import android.os.Parcel;
import android.os.Parcelable;

/* compiled from: ParcelableCompatHoneycombMR2 */
class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable.ClassLoaderCreator<T> {
    private final ParcelableCompatCreatorCallbacks<T> mCallbacks;

    public ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks<T> callbacks) {
        this.mCallbacks = callbacks;
    }

    public T createFromParcel(Parcel in) {
        return this.mCallbacks.createFromParcel(in, (ClassLoader) null);
    }

    public T createFromParcel(Parcel in, ClassLoader loader) {
        return this.mCallbacks.createFromParcel(in, loader);
    }

    public T[] newArray(int size) {
        return this.mCallbacks.newArray(size);
    }
}
