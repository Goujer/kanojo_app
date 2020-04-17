package com.facebook.widget;

import android.app.Activity;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.facebook.FacebookException;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.android.R;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.model.GraphPlace;
import com.facebook.widget.GraphObjectAdapter;
import com.facebook.widget.PickerFragment;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PlacePickerFragment extends PickerFragment<GraphPlace> {
    private static final String CATEGORY = "category";
    public static final int DEFAULT_RADIUS_IN_METERS = 1000;
    public static final int DEFAULT_RESULTS_LIMIT = 100;
    private static final String ID = "id";
    private static final String LOCATION = "location";
    public static final String LOCATION_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.Location";
    private static final String NAME = "name";
    public static final String RADIUS_IN_METERS_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.RadiusInMeters";
    public static final String RESULTS_LIMIT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ResultsLimit";
    public static final String SEARCH_TEXT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.SearchText";
    public static final String SHOW_SEARCH_BOX_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ShowSearchBox";
    private static final String TAG = "PlacePickerFragment";
    private static final String WERE_HERE_COUNT = "were_here_count";
    private static final int searchTextTimerDelayInMilliseconds = 2000;
    private boolean hasSearchTextChangedSinceLastQuery;
    private Location location;
    private int radiusInMeters;
    private int resultsLimit;
    private EditText searchBox;
    private String searchText;
    private Timer searchTextTimer;
    private boolean showSearchBox;

    public PlacePickerFragment() {
        this((Bundle) null);
    }

    public PlacePickerFragment(Bundle args) {
        super(GraphPlace.class, R.layout.com_facebook_placepickerfragment, args);
        this.radiusInMeters = 1000;
        this.resultsLimit = 100;
        this.showSearchBox = true;
        setPlacePickerSettingsFromBundle(args);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location2) {
        this.location = location2;
    }

    public int getRadiusInMeters() {
        return this.radiusInMeters;
    }

    public void setRadiusInMeters(int radiusInMeters2) {
        this.radiusInMeters = radiusInMeters2;
    }

    public int getResultsLimit() {
        return this.resultsLimit;
    }

    public void setResultsLimit(int resultsLimit2) {
        this.resultsLimit = resultsLimit2;
    }

    public String getSearchText() {
        return this.searchText;
    }

    public void setSearchText(String searchText2) {
        if (TextUtils.isEmpty(searchText2)) {
            searchText2 = null;
        }
        this.searchText = searchText2;
        if (this.searchBox != null) {
            this.searchBox.setText(searchText2);
        }
    }

    public void onSearchBoxTextChanged(String searchText2, boolean forceReloadEventIfSameText) {
        if (forceReloadEventIfSameText || !Utility.stringsEqualOrEmpty(this.searchText, searchText2)) {
            if (TextUtils.isEmpty(searchText2)) {
                searchText2 = null;
            }
            this.searchText = searchText2;
            this.hasSearchTextChangedSinceLastQuery = true;
            if (this.searchTextTimer == null) {
                this.searchTextTimer = createSearchTextTimer();
            }
        }
    }

    public GraphPlace getSelection() {
        Collection<GraphPlace> selection = getSelectedGraphObjects();
        if (selection == null || selection.size() <= 0) {
            return null;
        }
        return selection.iterator().next();
    }

    public void setSettingsFromBundle(Bundle inState) {
        super.setSettingsFromBundle(inState);
        setPlacePickerSettingsFromBundle(inState);
    }

    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);
        TypedArray a = activity.obtainStyledAttributes(attrs, R.styleable.com_facebook_place_picker_fragment);
        setRadiusInMeters(a.getInt(0, this.radiusInMeters));
        setResultsLimit(a.getInt(1, this.resultsLimit));
        if (a.hasValue(1)) {
            setSearchText(a.getString(2));
        }
        this.showSearchBox = a.getBoolean(3, this.showSearchBox);
        a.recycle();
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        ViewStub stub;
        super.onActivityCreated(savedInstanceState);
        ViewGroup view = (ViewGroup) getView();
        if (this.showSearchBox && (stub = (ViewStub) view.findViewById(R.id.com_facebook_placepickerfragment_search_box_stub)) != null) {
            this.searchBox = (EditText) stub.inflate();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(3, R.id.search_box);
            ((ListView) view.findViewById(R.id.com_facebook_picker_list_view)).setLayoutParams(layoutParams);
            if (view.findViewById(R.id.com_facebook_picker_title_bar) != null) {
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams2.addRule(3, R.id.com_facebook_picker_title_bar);
                this.searchBox.setLayoutParams(layoutParams2);
            }
            this.searchBox.addTextChangedListener(new SearchTextWatcher(this, (SearchTextWatcher) null));
            if (!TextUtils.isEmpty(this.searchText)) {
                this.searchBox.setText(this.searchText);
            }
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.searchBox != null) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).showSoftInput(this.searchBox, 1);
        }
    }

    public void onDetach() {
        super.onDetach();
        if (this.searchBox != null) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.searchBox.getWindowToken(), 0);
        }
    }

    /* access modifiers changed from: package-private */
    public void saveSettingsToBundle(Bundle outState) {
        super.saveSettingsToBundle(outState);
        outState.putInt(RADIUS_IN_METERS_BUNDLE_KEY, this.radiusInMeters);
        outState.putInt(RESULTS_LIMIT_BUNDLE_KEY, this.resultsLimit);
        outState.putString(SEARCH_TEXT_BUNDLE_KEY, this.searchText);
        outState.putParcelable(LOCATION_BUNDLE_KEY, this.location);
        outState.putBoolean(SHOW_SEARCH_BOX_BUNDLE_KEY, this.showSearchBox);
    }

    /* access modifiers changed from: package-private */
    public void onLoadingData() {
        this.hasSearchTextChangedSinceLastQuery = false;
    }

    /* access modifiers changed from: package-private */
    public Request getRequestForLoadData(Session session) {
        return createRequest(this.location, this.radiusInMeters, this.resultsLimit, this.searchText, this.extraFields, session);
    }

    /* access modifiers changed from: package-private */
    public String getDefaultTitleText() {
        return getString(R.string.com_facebook_nearby);
    }

    /* access modifiers changed from: package-private */
    public PickerFragment<GraphPlace>.PickerFragmentAdapter<GraphPlace> createAdapter() {
        PickerFragment<GraphPlace>.PickerFragmentAdapter<GraphPlace> adapter = new PickerFragment<GraphPlace>.PickerFragmentAdapter<GraphPlace>(this, getActivity()) {
            /* access modifiers changed from: protected */
            public CharSequence getSubTitleOfGraphObject(GraphPlace graphObject) {
                String category = graphObject.getCategory();
                Integer wereHereCount = (Integer) graphObject.getProperty(PlacePickerFragment.WERE_HERE_COUNT);
                if (category != null && wereHereCount != null) {
                    return PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_format, category, wereHereCount);
                } else if (category == null && wereHereCount != null) {
                    return PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_were_here_only_format, wereHereCount);
                } else if (category == null || wereHereCount != null) {
                    return null;
                } else {
                    return PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_catetory_only_format, category);
                }
            }

            /* access modifiers changed from: protected */
            public int getGraphObjectRowLayoutId(GraphPlace graphObject) {
                return R.layout.com_facebook_placepickerfragment_list_row;
            }

            /* access modifiers changed from: protected */
            public int getDefaultPicture() {
                return R.drawable.com_facebook_place_default_icon;
            }
        };
        adapter.setShowCheckbox(false);
        adapter.setShowPicture(getShowPictures());
        return adapter;
    }

    /* access modifiers changed from: package-private */
    public PickerFragment<GraphPlace>.LoadingStrategy createLoadingStrategy() {
        return new AsNeededLoadingStrategy(this, (AsNeededLoadingStrategy) null);
    }

    /* access modifiers changed from: package-private */
    public PickerFragment<GraphPlace>.SelectionStrategy createSelectionStrategy() {
        return new PickerFragment.SingleSelectionStrategy();
    }

    private Request createRequest(Location location2, int radiusInMeters2, int resultsLimit2, String searchText2, Set<String> extraFields, Session session) {
        Request request = Request.newPlacesSearchRequest(session, location2, radiusInMeters2, resultsLimit2, searchText2, (Request.GraphPlaceListCallback) null);
        Set<String> fields = new HashSet<>(extraFields);
        fields.addAll(Arrays.asList(new String[]{ID, NAME, LOCATION, CATEGORY, WERE_HERE_COUNT}));
        String pictureField = this.adapter.getPictureFieldSpecifier();
        if (pictureField != null) {
            fields.add(pictureField);
        }
        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);
        return request;
    }

    private void setPlacePickerSettingsFromBundle(Bundle inState) {
        if (inState != null) {
            setRadiusInMeters(inState.getInt(RADIUS_IN_METERS_BUNDLE_KEY, this.radiusInMeters));
            setResultsLimit(inState.getInt(RESULTS_LIMIT_BUNDLE_KEY, this.resultsLimit));
            if (inState.containsKey(SEARCH_TEXT_BUNDLE_KEY)) {
                setSearchText(inState.getString(SEARCH_TEXT_BUNDLE_KEY));
            }
            if (inState.containsKey(LOCATION_BUNDLE_KEY)) {
                setLocation((Location) inState.getParcelable(LOCATION_BUNDLE_KEY));
            }
            this.showSearchBox = inState.getBoolean(SHOW_SEARCH_BOX_BUNDLE_KEY, this.showSearchBox);
        }
    }

    private Timer createSearchTextTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                PlacePickerFragment.this.onSearchTextTimerTriggered();
            }
        }, 0, 2000);
        return timer;
    }

    /* access modifiers changed from: private */
    public void onSearchTextTimerTriggered() {
        if (this.hasSearchTextChangedSinceLastQuery) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    try {
                        PlacePickerFragment.this.loadData(true);
                        if (0 != 0) {
                            PickerFragment.OnErrorListener onErrorListener = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener != null) {
                                onErrorListener.onError(PlacePickerFragment.this, (FacebookException) null);
                                return;
                            }
                            Logger.log(LoggingBehavior.REQUESTS, PlacePickerFragment.TAG, "Error loading data : %s", null);
                        }
                    } catch (FacebookException fe) {
                        FacebookException error = fe;
                        if (error != null) {
                            PickerFragment.OnErrorListener onErrorListener2 = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener2 != null) {
                                onErrorListener2.onError(PlacePickerFragment.this, error);
                                return;
                            }
                            Logger.log(LoggingBehavior.REQUESTS, PlacePickerFragment.TAG, "Error loading data : %s", error);
                        }
                    } catch (Exception e) {
                        FacebookException error2 = new FacebookException((Throwable) e);
                        if (error2 != null) {
                            PickerFragment.OnErrorListener onErrorListener3 = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener3 != null) {
                                onErrorListener3.onError(PlacePickerFragment.this, error2);
                                FacebookException facebookException = error2;
                                return;
                            }
                            Logger.log(LoggingBehavior.REQUESTS, PlacePickerFragment.TAG, "Error loading data : %s", error2);
                            FacebookException facebookException2 = error2;
                            return;
                        }
                    } catch (Throwable th) {
                        if (0 != 0) {
                            PickerFragment.OnErrorListener onErrorListener4 = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener4 != null) {
                                onErrorListener4.onError(PlacePickerFragment.this, (FacebookException) null);
                            } else {
                                Logger.log(LoggingBehavior.REQUESTS, PlacePickerFragment.TAG, "Error loading data : %s", null);
                            }
                        }
                        throw th;
                    }
                }
            });
            return;
        }
        this.searchTextTimer.cancel();
        this.searchTextTimer = null;
    }

    private class AsNeededLoadingStrategy extends PickerFragment<GraphPlace>.LoadingStrategy {
        private AsNeededLoadingStrategy() {
            super();
        }

        /* synthetic */ AsNeededLoadingStrategy(PlacePickerFragment placePickerFragment, AsNeededLoadingStrategy asNeededLoadingStrategy) {
            this();
        }

        public void attach(GraphObjectAdapter<GraphPlace> adapter) {
            super.attach(adapter);
            this.adapter.setDataNeededListener(new GraphObjectAdapter.DataNeededListener() {
                public void onDataNeeded() {
                    if (!AsNeededLoadingStrategy.this.loader.isLoading()) {
                        AsNeededLoadingStrategy.this.loader.followNextLink();
                    }
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onLoadFinished(GraphObjectPagingLoader<GraphPlace> loader, SimpleGraphObjectCursor<GraphPlace> data) {
            super.onLoadFinished(loader, data);
            if (data != null && !loader.isLoading()) {
                PlacePickerFragment.this.hideActivityCircle();
                if (data.isFromCache()) {
                    loader.refreshOriginalRequest((long) (data.areMoreObjectsAvailable() ? PlacePickerFragment.searchTextTimerDelayInMilliseconds : 0));
                }
            }
        }
    }

    private class SearchTextWatcher implements TextWatcher {
        private SearchTextWatcher() {
        }

        /* synthetic */ SearchTextWatcher(PlacePickerFragment placePickerFragment, SearchTextWatcher searchTextWatcher) {
            this();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            PlacePickerFragment.this.onSearchBoxTextChanged(s.toString(), false);
        }

        public void afterTextChanged(Editable s) {
        }
    }
}
