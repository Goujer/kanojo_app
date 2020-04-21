package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.android.R;
import com.facebook.internal.SessionTracker;
import com.facebook.model.GraphObject;
import com.facebook.widget.GraphObjectAdapter;
import com.facebook.widget.GraphObjectPagingLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PickerFragment<T extends GraphObject> extends Fragment {
    private static final String ACTIVITY_CIRCLE_SHOW_KEY = "com.facebook.android.PickerFragment.ActivityCircleShown";
    public static final String DONE_BUTTON_TEXT_BUNDLE_KEY = "com.facebook.widget.PickerFragment.DoneButtonText";
    public static final String EXTRA_FIELDS_BUNDLE_KEY = "com.facebook.widget.PickerFragment.ExtraFields";
    private static final int PROFILE_PICTURE_PREFETCH_BUFFER = 5;
    private static final String SELECTION_BUNDLE_KEY = "com.facebook.android.PickerFragment.Selection";
    public static final String SHOW_PICTURES_BUNDLE_KEY = "com.facebook.widget.PickerFragment.ShowPictures";
    public static final String SHOW_TITLE_BAR_BUNDLE_KEY = "com.facebook.widget.PickerFragment.ShowTitleBar";
    public static final String TITLE_TEXT_BUNDLE_KEY = "com.facebook.widget.PickerFragment.TitleText";
    private ProgressBar activityCircle;
    GraphObjectAdapter<T> adapter;
    private Button doneButton;
    private Drawable doneButtonBackground;
    private String doneButtonText;
    HashSet<String> extraFields = new HashSet<>();
    private GraphObjectFilter<T> filter;
    /* access modifiers changed from: private */
    public final Class<T> graphObjectClass;
    private final int layout;
    private ListView listView;
    private PickerFragment<T>.LoadingStrategy loadingStrategy;
    private OnDataChangedListener onDataChangedListener;
    /* access modifiers changed from: private */
    public OnDoneButtonClickedListener onDoneButtonClickedListener;
    /* access modifiers changed from: private */
    public OnErrorListener onErrorListener;
    private AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            PickerFragment.this.reprioritizeDownloads();
        }
    };
    private OnSelectionChangedListener onSelectionChangedListener;
    /* access modifiers changed from: private */
    public PickerFragment<T>.SelectionStrategy selectionStrategy;
    private SessionTracker sessionTracker;
    private boolean showPictures = true;
    private boolean showTitleBar = true;
    private Drawable titleBarBackground;
    private String titleText;
    private TextView titleTextView;

    public interface GraphObjectFilter<T> {
        boolean includeItem(T t);
    }

    public interface OnDataChangedListener {
        void onDataChanged(PickerFragment<?> pickerFragment);
    }

    public interface OnDoneButtonClickedListener {
        void onDoneButtonClicked(PickerFragment<?> pickerFragment);
    }

    public interface OnErrorListener {
        void onError(PickerFragment<?> pickerFragment, FacebookException facebookException);
    }

    public interface OnSelectionChangedListener {
        void onSelectionChanged(PickerFragment<?> pickerFragment);
    }

    /* access modifiers changed from: package-private */
    public abstract PickerFragment<T>.PickerFragmentAdapter<T> createAdapter();

    /* access modifiers changed from: package-private */
    public abstract PickerFragment<T>.LoadingStrategy createLoadingStrategy();

    /* access modifiers changed from: package-private */
    public abstract PickerFragment<T>.SelectionStrategy createSelectionStrategy();

    /* access modifiers changed from: package-private */
    public abstract Request getRequestForLoadData(Session session);

    PickerFragment(Class<T> graphObjectClass2, int layout2, Bundle args) {
        this.graphObjectClass = graphObjectClass2;
        this.layout = layout2;
        setPickerFragmentSettingsFromBundle(args);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter = createAdapter();
        this.adapter.setFilter(new GraphObjectAdapter.Filter<T>() {
            public boolean includeItem(T graphObject) {
                return PickerFragment.this.filterIncludesItem(graphObject);
            }
        });
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.com_facebook_picker_fragment);
        setShowPictures(a.getBoolean(0, this.showPictures));
        String extraFieldsString = a.getString(1);
        if (extraFieldsString != null) {
            setExtraFields(Arrays.asList(extraFieldsString.split(",")));
        }
        this.showTitleBar = a.getBoolean(2, this.showTitleBar);
        this.titleText = a.getString(3);
        this.doneButtonText = a.getString(4);
        this.titleBarBackground = a.getDrawable(5);
        this.doneButtonBackground = a.getDrawable(6);
        a.recycle();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(this.layout, container, false);
        this.listView = (ListView) view.findViewById(R.id.com_facebook_picker_list_view);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PickerFragment.this.onListItemClick((ListView) parent, v, position);
            }
        });
        this.listView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                return false;
            }
        });
        this.listView.setOnScrollListener(this.onScrollListener);
        this.listView.setAdapter(this.adapter);
        this.activityCircle = (ProgressBar) view.findViewById(R.id.com_facebook_picker_activity_circle);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.sessionTracker = new SessionTracker(getActivity(), new Session.StatusCallback() {
            public void call(Session session, SessionState state, Exception exception) {
                if (!session.isOpened()) {
                    PickerFragment.this.clearResults();
                }
            }
        });
        setSettingsFromBundle(savedInstanceState);
        this.loadingStrategy = createLoadingStrategy();
        this.loadingStrategy.attach(this.adapter);
        this.selectionStrategy = createSelectionStrategy();
        this.selectionStrategy.readSelectionFromBundle(savedInstanceState, SELECTION_BUNDLE_KEY);
        if (this.showTitleBar) {
            inflateTitleBar((ViewGroup) getView());
        }
        if (this.activityCircle != null && savedInstanceState != null) {
            if (savedInstanceState.getBoolean(ACTIVITY_CIRCLE_SHOW_KEY, false)) {
                displayActivityCircle();
            } else {
                hideActivityCircle();
            }
        }
    }

    public void onDetach() {
        super.onDetach();
        this.listView.setOnScrollListener((AbsListView.OnScrollListener) null);
        this.listView.setAdapter((ListAdapter) null);
        this.loadingStrategy.detach();
        this.sessionTracker.stopTracking();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveSettingsToBundle(outState);
        this.selectionStrategy.saveSelectionToBundle(outState, SELECTION_BUNDLE_KEY);
        if (this.activityCircle != null) {
            outState.putBoolean(ACTIVITY_CIRCLE_SHOW_KEY, this.activityCircle.getVisibility() == 0);
        }
    }

    public void setArguments(Bundle args) {
        super.setArguments(args);
        setSettingsFromBundle(args);
    }

    public OnDataChangedListener getOnDataChangedListener() {
        return this.onDataChangedListener;
    }

    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener2) {
        this.onDataChangedListener = onDataChangedListener2;
    }

    public OnSelectionChangedListener getOnSelectionChangedListener() {
        return this.onSelectionChangedListener;
    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener2) {
        this.onSelectionChangedListener = onSelectionChangedListener2;
    }

    public OnDoneButtonClickedListener getOnDoneButtonClickedListener() {
        return this.onDoneButtonClickedListener;
    }

    public void setOnDoneButtonClickedListener(OnDoneButtonClickedListener onDoneButtonClickedListener2) {
        this.onDoneButtonClickedListener = onDoneButtonClickedListener2;
    }

    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener2) {
        this.onErrorListener = onErrorListener2;
    }

    public GraphObjectFilter<T> getFilter() {
        return this.filter;
    }

    public void setFilter(GraphObjectFilter<T> filter2) {
        this.filter = filter2;
    }

    public Session getSession() {
        return this.sessionTracker.getSession();
    }

    public void setSession(Session session) {
        this.sessionTracker.setSession(session);
    }

    public boolean getShowPictures() {
        return this.showPictures;
    }

    public void setShowPictures(boolean showPictures2) {
        this.showPictures = showPictures2;
    }

    public Set<String> getExtraFields() {
        return new HashSet(this.extraFields);
    }

    public void setExtraFields(Collection<String> fields) {
        this.extraFields = new HashSet<>();
        if (fields != null) {
            this.extraFields.addAll(fields);
        }
    }

    public void setShowTitleBar(boolean showTitleBar2) {
        this.showTitleBar = showTitleBar2;
    }

    public boolean getShowTitleBar() {
        return this.showTitleBar;
    }

    public void setTitleText(String titleText2) {
        this.titleText = titleText2;
    }

    public String getTitleText() {
        if (this.titleText == null) {
            this.titleText = getDefaultTitleText();
        }
        return this.titleText;
    }

    public void setDoneButtonText(String doneButtonText2) {
        this.doneButtonText = doneButtonText2;
    }

    public String getDoneButtonText() {
        if (this.doneButtonText == null) {
            this.doneButtonText = getDefaultDoneButtonText();
        }
        return this.doneButtonText;
    }

    public void loadData(boolean forceReload) {
        if (forceReload || !this.loadingStrategy.isDataPresentOrLoading()) {
            loadDataSkippingRoundTripIfCached();
        }
    }

    public void setSettingsFromBundle(Bundle inState) {
        setPickerFragmentSettingsFromBundle(inState);
    }

    /* access modifiers changed from: package-private */
    public boolean filterIncludesItem(T graphObject) {
        if (this.filter != null) {
            return this.filter.includeItem(graphObject);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public List<T> getSelectedGraphObjects() {
        return this.adapter.getGraphObjectsById(this.selectionStrategy.getSelectedIds());
    }

    /* access modifiers changed from: package-private */
    public void saveSettingsToBundle(Bundle outState) {
        outState.putBoolean(SHOW_PICTURES_BUNDLE_KEY, this.showPictures);
        if (!this.extraFields.isEmpty()) {
            outState.putString(EXTRA_FIELDS_BUNDLE_KEY, TextUtils.join(",", this.extraFields));
        }
        outState.putBoolean(SHOW_TITLE_BAR_BUNDLE_KEY, this.showTitleBar);
        outState.putString(TITLE_TEXT_BUNDLE_KEY, this.titleText);
        outState.putString(DONE_BUTTON_TEXT_BUNDLE_KEY, this.doneButtonText);
    }

    /* access modifiers changed from: package-private */
    public void onLoadingData() {
    }

    /* access modifiers changed from: package-private */
    public String getDefaultTitleText() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public String getDefaultDoneButtonText() {
        return getString(R.string.com_facebook_picker_done_button_text);
    }

    /* access modifiers changed from: package-private */
    public void displayActivityCircle() {
        if (this.activityCircle != null) {
            layoutActivityCircle();
            this.activityCircle.setVisibility(0);
        }
    }

    /* access modifiers changed from: package-private */
    public void layoutActivityCircle() {
        setAlpha(this.activityCircle, !this.adapter.isEmpty() ? 0.25f : 1.0f);
    }

    /* access modifiers changed from: package-private */
    public void hideActivityCircle() {
        if (this.activityCircle != null) {
            this.activityCircle.clearAnimation();
            this.activityCircle.setVisibility(4);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSelectionStrategy(PickerFragment<T>.SelectionStrategy selectionStrategy2) {
        if (selectionStrategy2 != this.selectionStrategy) {
            this.selectionStrategy = selectionStrategy2;
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    private static void setAlpha(View view, float alpha) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    private void setPickerFragmentSettingsFromBundle(Bundle inState) {
        if (inState != null) {
            this.showPictures = inState.getBoolean(SHOW_PICTURES_BUNDLE_KEY, this.showPictures);
            String extraFieldsString = inState.getString(EXTRA_FIELDS_BUNDLE_KEY);
            if (extraFieldsString != null) {
                setExtraFields(Arrays.asList(extraFieldsString.split(",")));
            }
            this.showTitleBar = inState.getBoolean(SHOW_TITLE_BAR_BUNDLE_KEY, this.showTitleBar);
            String titleTextString = inState.getString(TITLE_TEXT_BUNDLE_KEY);
            if (titleTextString != null) {
                this.titleText = titleTextString;
                if (this.titleTextView != null) {
                    this.titleTextView.setText(this.titleText);
                }
            }
            String doneButtonTextString = inState.getString(DONE_BUTTON_TEXT_BUNDLE_KEY);
            if (doneButtonTextString != null) {
                this.doneButtonText = doneButtonTextString;
                if (this.doneButton != null) {
                    this.doneButton.setText(this.doneButtonText);
                }
            }
        }
    }

    private void inflateTitleBar(ViewGroup view) {
        ViewStub stub = (ViewStub) view.findViewById(R.id.com_facebook_picker_title_bar_stub);
        if (stub != null) {
            View titleBar = stub.inflate();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(3, R.id.com_facebook_picker_title_bar);
            this.listView.setLayoutParams(layoutParams);
            if (this.titleBarBackground != null) {
                titleBar.setBackgroundDrawable(this.titleBarBackground);
            }
            this.doneButton = (Button) view.findViewById(R.id.com_facebook_picker_done_button);
            if (this.doneButton != null) {
                this.doneButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (PickerFragment.this.onDoneButtonClickedListener != null) {
                            PickerFragment.this.onDoneButtonClickedListener.onDoneButtonClicked(PickerFragment.this);
                        }
                    }
                });
                if (getDoneButtonText() != null) {
                    this.doneButton.setText(getDoneButtonText());
                }
                if (this.doneButtonBackground != null) {
                    this.doneButton.setBackgroundDrawable(this.doneButtonBackground);
                }
            }
            this.titleTextView = (TextView) view.findViewById(R.id.com_facebook_picker_title);
            if (this.titleTextView != null && getTitleText() != null) {
                this.titleTextView.setText(getTitleText());
            }
        }
    }

    /* access modifiers changed from: private */
    public void onListItemClick(ListView listView2, View v, int position) {
        this.selectionStrategy.toggleSelection(this.adapter.getIdOfGraphObject((GraphObject) listView2.getItemAtPosition(position)));
        this.adapter.notifyDataSetChanged();
        if (this.onSelectionChangedListener != null) {
            this.onSelectionChangedListener.onSelectionChanged(this);
        }
    }

    private void loadDataSkippingRoundTripIfCached() {
        clearResults();
        Request request = getRequestForLoadData(getSession());
        if (request != null) {
            onLoadingData();
            this.loadingStrategy.startLoading(request);
        }
    }

    /* access modifiers changed from: private */
    public void clearResults() {
        boolean wasData;
        if (this.adapter != null) {
            boolean wasSelection = !this.selectionStrategy.isEmpty();
            if (this.adapter.isEmpty()) {
                wasData = false;
            } else {
                wasData = true;
            }
            this.loadingStrategy.clearResults();
            this.selectionStrategy.clear();
            this.adapter.notifyDataSetChanged();
            if (wasData && this.onDataChangedListener != null) {
                this.onDataChangedListener.onDataChanged(this);
            }
            if (wasSelection && this.onSelectionChangedListener != null) {
                this.onSelectionChangedListener.onSelectionChanged(this);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateAdapter(SimpleGraphObjectCursor<T> data) {
        int newPositionOfItem;
        if (this.adapter != null) {
            View view = this.listView.getChildAt(1);
            int anchorPosition = this.listView.getFirstVisiblePosition();
            if (anchorPosition > 0) {
                anchorPosition++;
            }
            GraphObjectAdapter.SectionAndItem<T> anchorItem = this.adapter.getSectionAndItem(anchorPosition);
            int top = (view == null || anchorItem.getType() == GraphObjectAdapter.SectionAndItem.Type.ACTIVITY_CIRCLE) ? 0 : view.getTop();
            boolean dataChanged = this.adapter.changeCursor(data);
            if (!(view == null || anchorItem == null || (newPositionOfItem = this.adapter.getPosition(anchorItem.sectionKey, anchorItem.graphObject)) == -1)) {
                this.listView.setSelectionFromTop(newPositionOfItem, top);
            }
            if (dataChanged && this.onDataChangedListener != null) {
                this.onDataChangedListener.onDataChanged(this);
            }
        }
    }

    /* access modifiers changed from: private */
    public void reprioritizeDownloads() {
        int lastVisibleItem = this.listView.getLastVisiblePosition();
        if (lastVisibleItem >= 0) {
            this.adapter.prioritizeViewRange(this.listView.getFirstVisiblePosition(), lastVisibleItem, 5);
        }
    }

    abstract class LoadingStrategy {
        protected static final int CACHED_RESULT_REFRESH_DELAY = 2000;
        protected GraphObjectAdapter<T> adapter;
        protected GraphObjectPagingLoader<T> loader;

        LoadingStrategy() {
        }

        public void attach(GraphObjectAdapter<T> adapter2) {
            this.loader = (GraphObjectPagingLoader) PickerFragment.this.getLoaderManager().initLoader(0, (Bundle) null, new LoaderManager.LoaderCallbacks<SimpleGraphObjectCursor<T>>() {
                public Loader<SimpleGraphObjectCursor<T>> onCreateLoader(int id, Bundle args) {
                    return LoadingStrategy.this.onCreateLoader();
                }

                public void onLoadFinished(Loader<SimpleGraphObjectCursor<T>> loader, SimpleGraphObjectCursor<T> data) {
                    if (loader != LoadingStrategy.this.loader) {
                        throw new FacebookException("Received callback for unknown loader.");
                    }
                    LoadingStrategy.this.onLoadFinished((GraphObjectPagingLoader) loader, data);
                }

                public void onLoaderReset(Loader<SimpleGraphObjectCursor<T>> loader) {
                    if (loader != LoadingStrategy.this.loader) {
                        throw new FacebookException("Received callback for unknown loader.");
                    }
                    LoadingStrategy.this.onLoadReset((GraphObjectPagingLoader) loader);
                }
            });
            this.loader.setOnErrorListener(new GraphObjectPagingLoader.OnErrorListener() {
                public void onError(FacebookException error, GraphObjectPagingLoader<?> graphObjectPagingLoader) {
                    PickerFragment.this.hideActivityCircle();
                    if (PickerFragment.this.onErrorListener != null) {
                        PickerFragment.this.onErrorListener.onError(PickerFragment.this, error);
                    }
                }
            });
            this.adapter = adapter2;
            this.adapter.changeCursor(this.loader.getCursor());
        }

        public void detach() {
            this.adapter.setDataNeededListener((GraphObjectAdapter.DataNeededListener) null);
            this.loader.setOnErrorListener((GraphObjectPagingLoader.OnErrorListener) null);
            this.loader = null;
            this.adapter = null;
        }

        public void clearResults() {
            if (this.loader != null) {
                this.loader.clearResults();
            }
        }

        public void startLoading(Request request) {
            if (this.loader != null) {
                this.loader.startLoading(request, true);
                onStartLoading(this.loader, request);
            }
        }

        public boolean isDataPresentOrLoading() {
            return !this.adapter.isEmpty() || this.loader.isLoading();
        }

        /* access modifiers changed from: protected */
        public GraphObjectPagingLoader<T> onCreateLoader() {
            return new GraphObjectPagingLoader<>(PickerFragment.this.getActivity(), PickerFragment.this.graphObjectClass);
        }

        /* access modifiers changed from: protected */
        public void onStartLoading(GraphObjectPagingLoader<T> graphObjectPagingLoader, Request request) {
            PickerFragment.this.displayActivityCircle();
        }

        /* access modifiers changed from: protected */
        public void onLoadReset(GraphObjectPagingLoader<T> graphObjectPagingLoader) {
            this.adapter.changeCursor((GraphObjectCursor) null);
        }

        /* access modifiers changed from: protected */
        public void onLoadFinished(GraphObjectPagingLoader<T> graphObjectPagingLoader, SimpleGraphObjectCursor<T> data) {
            PickerFragment.this.updateAdapter(data);
        }
    }

    abstract class SelectionStrategy {
        /* access modifiers changed from: package-private */
        public abstract void clear();

        /* access modifiers changed from: package-private */
        public abstract Collection<String> getSelectedIds();

        /* access modifiers changed from: package-private */
        public abstract boolean isEmpty();

        /* access modifiers changed from: package-private */
        public abstract boolean isSelected(String str);

        /* access modifiers changed from: package-private */
        public abstract void readSelectionFromBundle(Bundle bundle, String str);

        /* access modifiers changed from: package-private */
        public abstract void saveSelectionToBundle(Bundle bundle, String str);

        /* access modifiers changed from: package-private */
        public abstract boolean shouldShowCheckBoxIfUnselected();

        /* access modifiers changed from: package-private */
        public abstract void toggleSelection(String str);

        SelectionStrategy() {
        }
    }

    class SingleSelectionStrategy extends PickerFragment<T>.SelectionStrategy {
        private String selectedId;

        SingleSelectionStrategy() {
            super();
        }

        public Collection<String> getSelectedIds() {
            return Arrays.asList(new String[]{this.selectedId});
        }

        /* access modifiers changed from: package-private */
        public boolean isSelected(String id) {
            return (this.selectedId == null || id == null || !this.selectedId.equals(id)) ? false : true;
        }

        /* access modifiers changed from: package-private */
        public void toggleSelection(String id) {
            if (this.selectedId == null || !this.selectedId.equals(id)) {
                this.selectedId = id;
            } else {
                this.selectedId = null;
            }
        }

        /* access modifiers changed from: package-private */
        public void saveSelectionToBundle(Bundle outBundle, String key) {
            if (!TextUtils.isEmpty(this.selectedId)) {
                outBundle.putString(key, this.selectedId);
            }
        }

        /* access modifiers changed from: package-private */
        public void readSelectionFromBundle(Bundle inBundle, String key) {
            if (inBundle != null) {
                this.selectedId = inBundle.getString(key);
            }
        }

        public void clear() {
            this.selectedId = null;
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.selectedId == null;
        }

        /* access modifiers changed from: package-private */
        public boolean shouldShowCheckBoxIfUnselected() {
            return false;
        }
    }

    class MultiSelectionStrategy extends PickerFragment<T>.SelectionStrategy {
        private Set<String> selectedIds = new HashSet();

        MultiSelectionStrategy() {
            super();
        }

        public Collection<String> getSelectedIds() {
            return this.selectedIds;
        }

        /* access modifiers changed from: package-private */
        public boolean isSelected(String id) {
            return id != null && this.selectedIds.contains(id);
        }

        /* access modifiers changed from: package-private */
        public void toggleSelection(String id) {
            if (id == null) {
                return;
            }
            if (this.selectedIds.contains(id)) {
                this.selectedIds.remove(id);
            } else {
                this.selectedIds.add(id);
            }
        }

        /* access modifiers changed from: package-private */
        public void saveSelectionToBundle(Bundle outBundle, String key) {
            if (!this.selectedIds.isEmpty()) {
                outBundle.putString(key, TextUtils.join(",", this.selectedIds));
            }
        }

        /* access modifiers changed from: package-private */
        public void readSelectionFromBundle(Bundle inBundle, String key) {
            String ids;
            if (inBundle != null && (ids = inBundle.getString(key)) != null) {
                String[] splitIds = TextUtils.split(ids, ",");
                this.selectedIds.clear();
                Collections.addAll(this.selectedIds, splitIds);
            }
        }

        public void clear() {
            this.selectedIds.clear();
        }

        /* access modifiers changed from: package-private */
        public boolean isEmpty() {
            return this.selectedIds.isEmpty();
        }

        /* access modifiers changed from: package-private */
        public boolean shouldShowCheckBoxIfUnselected() {
            return true;
        }
    }

    abstract class PickerFragmentAdapter<U extends GraphObject> extends GraphObjectAdapter<T> {
        public PickerFragmentAdapter(Context context) {
            super(context);
        }

        /* access modifiers changed from: package-private */
        public boolean isGraphObjectSelected(String graphObjectId) {
            return PickerFragment.this.selectionStrategy.isSelected(graphObjectId);
        }

        /* access modifiers changed from: package-private */
        public void updateCheckboxState(CheckBox checkBox, boolean graphObjectSelected) {
            checkBox.setChecked(graphObjectSelected);
            checkBox.setVisibility((graphObjectSelected || PickerFragment.this.selectionStrategy.shouldShowCheckBoxIfUnselected()) ? 0 : 8);
        }
    }
}
