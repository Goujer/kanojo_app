package com.facebook;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Pair;
import com.facebook.RequestBatch;
import com.facebook.internal.Logger;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import jp.co.cybird.app.android.lib.applauncher.AppLauncherConsts;
import org.apache.james.mime4j.util.CharsetUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    private static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ATTACHED_FILES_PARAM = "attached_files";
    private static final String ATTACHMENT_FILENAME_PREFIX = "file";
    private static final String BATCH_APP_ID_PARAM = "batch_app_id";
    private static final String BATCH_BODY_PARAM = "body";
    private static final String BATCH_ENTRY_DEPENDS_ON_PARAM = "depends_on";
    private static final String BATCH_ENTRY_NAME_PARAM = "name";
    private static final String BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM = "omit_response_on_success";
    private static final String BATCH_METHOD_PARAM = "method";
    private static final String BATCH_PARAM = "batch";
    private static final String BATCH_RELATIVE_URL_PARAM = "relative_url";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String FORMAT_JSON = "json";
    private static final String FORMAT_PARAM = "format";
    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final int MAXIMUM_BATCH_SIZE = 50;
    private static final String ME = "me";
    private static final String MIGRATION_BUNDLE_PARAM = "migration_bundle";
    private static final String MIME_BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    private static final String MY_FEED = "me/feed";
    private static final String MY_FRIENDS = "me/friends";
    private static final String MY_PHOTOS = "me/photos";
    private static final String MY_VIDEOS = "me/videos";
    private static final String PICTURE_PARAM = "picture";
    private static final String SDK_ANDROID = "android";
    private static final String SDK_PARAM = "sdk";
    private static final String SEARCH = "search";
    private static final String USER_AGENT_BASE = "FBAndroidSDK";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static String defaultBatchApplicationId;
    private static volatile String userAgent;
    private String batchEntryDependsOn;
    private String batchEntryName;
    private boolean batchEntryOmitResultOnSuccess;
    private Callback callback;
    private GraphObject graphObject;
    private String graphPath;
    private HttpMethod httpMethod;
    private String overriddenURL;
    private Bundle parameters;
    private String restMethod;
    private Session session;

    public interface Callback {
        void onCompleted(Response response);
    }

    public interface GraphPlaceListCallback {
        void onCompleted(List<GraphPlace> list, Response response);
    }

    public interface GraphUserCallback {
        void onCompleted(GraphUser graphUser, Response response);
    }

    public interface GraphUserListCallback {
        void onCompleted(List<GraphUser> list, Response response);
    }

    private interface KeyValueSerializer {
        void writeString(String str, String str2) throws IOException;
    }

    public Request() {
        this((Session) null, (String) null, (Bundle) null, (HttpMethod) null, (Callback) null);
    }

    public Request(Session session2, String graphPath2) {
        this(session2, graphPath2, (Bundle) null, (HttpMethod) null, (Callback) null);
    }

    public Request(Session session2, String graphPath2, Bundle parameters2, HttpMethod httpMethod2) {
        this(session2, graphPath2, parameters2, httpMethod2, (Callback) null);
    }

    public Request(Session session2, String graphPath2, Bundle parameters2, HttpMethod httpMethod2, Callback callback2) {
        this.batchEntryOmitResultOnSuccess = true;
        this.session = session2;
        this.graphPath = graphPath2;
        this.callback = callback2;
        setHttpMethod(httpMethod2);
        if (parameters2 != null) {
            this.parameters = new Bundle(parameters2);
        } else {
            this.parameters = new Bundle();
        }
        if (!this.parameters.containsKey(MIGRATION_BUNDLE_PARAM)) {
            this.parameters.putString(MIGRATION_BUNDLE_PARAM, FacebookSdkVersion.MIGRATION_BUNDLE);
        }
    }

    Request(Session session2, URL overriddenURL2) {
        this.batchEntryOmitResultOnSuccess = true;
        this.session = session2;
        this.overriddenURL = overriddenURL2.toString();
        setHttpMethod(HttpMethod.GET);
        this.parameters = new Bundle();
    }

    public static Request newPostRequest(Session session2, String graphPath2, GraphObject graphObject2, Callback callback2) {
        Request request = new Request(session2, graphPath2, (Bundle) null, HttpMethod.POST, callback2);
        request.setGraphObject(graphObject2);
        return request;
    }

    public static Request newRestRequest(Session session2, String restMethod2, Bundle parameters2, HttpMethod httpMethod2) {
        Request request = new Request(session2, (String) null, parameters2, httpMethod2);
        request.setRestMethod(restMethod2);
        return request;
    }

    public static Request newMeRequest(Session session2, final GraphUserCallback callback2) {
        return new Request(session2, ME, (Bundle) null, (HttpMethod) null, new Callback() {
            public void onCompleted(Response response) {
                if (GraphUserCallback.this != null) {
                    GraphUserCallback.this.onCompleted((GraphUser) response.getGraphObjectAs(GraphUser.class), response);
                }
            }
        });
    }

    public static Request newMyFriendsRequest(Session session2, final GraphUserListCallback callback2) {
        return new Request(session2, MY_FRIENDS, (Bundle) null, (HttpMethod) null, new Callback() {
            public void onCompleted(Response response) {
                if (GraphUserListCallback.this != null) {
                    GraphUserListCallback.this.onCompleted(Request.typedListFromResponse(response, GraphUser.class), response);
                }
            }
        });
    }

    public static Request newUploadPhotoRequest(Session session2, Bitmap image, Callback callback2) {
        Bundle parameters2 = new Bundle(1);
        parameters2.putParcelable(PICTURE_PARAM, image);
        return new Request(session2, MY_PHOTOS, parameters2, HttpMethod.POST, callback2);
    }

    public static Request newUploadPhotoRequest(Session session2, File file, Callback callback2) throws FileNotFoundException {
        ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, 268435456);
        Bundle parameters2 = new Bundle(1);
        parameters2.putParcelable(PICTURE_PARAM, descriptor);
        return new Request(session2, MY_PHOTOS, parameters2, HttpMethod.POST, callback2);
    }

    public static Request newUploadVideoRequest(Session session2, File file, Callback callback2) throws FileNotFoundException {
        ParcelFileDescriptor descriptor = ParcelFileDescriptor.open(file, 268435456);
        Bundle parameters2 = new Bundle(1);
        parameters2.putParcelable(file.getName(), descriptor);
        return new Request(session2, MY_VIDEOS, parameters2, HttpMethod.POST, callback2);
    }

    public static Request newGraphPathRequest(Session session2, String graphPath2, Callback callback2) {
        return new Request(session2, graphPath2, (Bundle) null, (HttpMethod) null, callback2);
    }

    public static Request newPlacesSearchRequest(Session session2, Location location, int radiusInMeters, int resultsLimit, String searchText, final GraphPlaceListCallback callback2) {
        if (location != null || !Utility.isNullOrEmpty(searchText)) {
            Bundle parameters2 = new Bundle(5);
            parameters2.putString("type", "place");
            parameters2.putInt("limit", resultsLimit);
            if (location != null) {
                parameters2.putString("center", String.format(Locale.US, "%f,%f", new Object[]{Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())}));
                parameters2.putInt("distance", radiusInMeters);
            }
            if (!Utility.isNullOrEmpty(searchText)) {
                parameters2.putString(AppLauncherConsts.REQUEST_PARAM_GENERAL, searchText);
            }
            return new Request(session2, SEARCH, parameters2, HttpMethod.GET, new Callback() {
                public void onCompleted(Response response) {
                    if (GraphPlaceListCallback.this != null) {
                        GraphPlaceListCallback.this.onCompleted(Request.typedListFromResponse(response, GraphPlace.class), response);
                    }
                }
            });
        }
        throw new FacebookException("Either location or searchText must be specified.");
    }

    public static Request newStatusUpdateRequest(Session session2, String message, Callback callback2) {
        Bundle parameters2 = new Bundle();
        parameters2.putString("message", message);
        return new Request(session2, MY_FEED, parameters2, HttpMethod.POST, callback2);
    }

    public final GraphObject getGraphObject() {
        return this.graphObject;
    }

    public final void setGraphObject(GraphObject graphObject2) {
        this.graphObject = graphObject2;
    }

    public final String getGraphPath() {
        return this.graphPath;
    }

    public final void setGraphPath(String graphPath2) {
        this.graphPath = graphPath2;
    }

    public final HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public final void setHttpMethod(HttpMethod httpMethod2) {
        if (this.overriddenURL == null || httpMethod2 == HttpMethod.GET) {
            if (httpMethod2 == null) {
                httpMethod2 = HttpMethod.GET;
            }
            this.httpMethod = httpMethod2;
            return;
        }
        throw new FacebookException("Can't change HTTP method on request with overridden URL.");
    }

    public final Bundle getParameters() {
        return this.parameters;
    }

    public final void setParameters(Bundle parameters2) {
        this.parameters = parameters2;
    }

    public final String getRestMethod() {
        return this.restMethod;
    }

    public final void setRestMethod(String restMethod2) {
        this.restMethod = restMethod2;
    }

    public final Session getSession() {
        return this.session;
    }

    public final void setSession(Session session2) {
        this.session = session2;
    }

    public final String getBatchEntryName() {
        return this.batchEntryName;
    }

    public final void setBatchEntryName(String batchEntryName2) {
        this.batchEntryName = batchEntryName2;
    }

    public final String getBatchEntryDependsOn() {
        return this.batchEntryDependsOn;
    }

    public final void setBatchEntryDependsOn(String batchEntryDependsOn2) {
        this.batchEntryDependsOn = batchEntryDependsOn2;
    }

    public final boolean getBatchEntryOmitResultOnSuccess() {
        return this.batchEntryOmitResultOnSuccess;
    }

    public final void setBatchEntryOmitResultOnSuccess(boolean batchEntryOmitResultOnSuccess2) {
        this.batchEntryOmitResultOnSuccess = batchEntryOmitResultOnSuccess2;
    }

    public static final String getDefaultBatchApplicationId() {
        return defaultBatchApplicationId;
    }

    public static final void setDefaultBatchApplicationId(String applicationId) {
        defaultBatchApplicationId = applicationId;
    }

    public final Callback getCallback() {
        return this.callback;
    }

    public final void setCallback(Callback callback2) {
        this.callback = callback2;
    }

    public static RequestAsyncTask executePostRequestAsync(Session session2, String graphPath2, GraphObject graphObject2, Callback callback2) {
        return newPostRequest(session2, graphPath2, graphObject2, callback2).executeAsync();
    }

    public static RequestAsyncTask executeRestRequestAsync(Session session2, String restMethod2, Bundle parameters2, HttpMethod httpMethod2) {
        return newRestRequest(session2, restMethod2, parameters2, httpMethod2).executeAsync();
    }

    public static RequestAsyncTask executeMeRequestAsync(Session session2, GraphUserCallback callback2) {
        return newMeRequest(session2, callback2).executeAsync();
    }

    public static RequestAsyncTask executeMyFriendsRequestAsync(Session session2, GraphUserListCallback callback2) {
        return newMyFriendsRequest(session2, callback2).executeAsync();
    }

    public static RequestAsyncTask executeUploadPhotoRequestAsync(Session session2, Bitmap image, Callback callback2) {
        return newUploadPhotoRequest(session2, image, callback2).executeAsync();
    }

    public static RequestAsyncTask executeUploadPhotoRequestAsync(Session session2, File file, Callback callback2) throws FileNotFoundException {
        return newUploadPhotoRequest(session2, file, callback2).executeAsync();
    }

    public static RequestAsyncTask executeGraphPathRequestAsync(Session session2, String graphPath2, Callback callback2) {
        return newGraphPathRequest(session2, graphPath2, callback2).executeAsync();
    }

    public static RequestAsyncTask executePlacesSearchRequestAsync(Session session2, Location location, int radiusInMeters, int resultsLimit, String searchText, GraphPlaceListCallback callback2) {
        return newPlacesSearchRequest(session2, location, radiusInMeters, resultsLimit, searchText, callback2).executeAsync();
    }

    public static RequestAsyncTask executeStatusUpdateRequestAsync(Session session2, String message, Callback callback2) {
        return newStatusUpdateRequest(session2, message, callback2).executeAsync();
    }

    public final Response executeAndWait() {
        return executeAndWait(this);
    }

    public final RequestAsyncTask executeAsync() {
        return executeBatchAsync(this);
    }

    public static HttpURLConnection toHttpConnection(Request... requests) {
        return toHttpConnection((Collection<Request>) Arrays.asList(requests));
    }

    public static HttpURLConnection toHttpConnection(Collection<Request> requests) {
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        return toHttpConnection(new RequestBatch(requests));
    }

    public static HttpURLConnection toHttpConnection(RequestBatch requests) {
        URL url;
        Iterator it = requests.iterator();
        while (it.hasNext()) {
            ((Request) it.next()).validate();
        }
        try {
            if (requests.size() == 1) {
                url = new URL(requests.get(0).getUrlForSingleRequest());
            } else {
                url = new URL(ServerProtocol.GRAPH_URL);
            }
            try {
                HttpURLConnection connection = createConnection(url);
                serializeToUrlConnection(requests, connection);
                return connection;
            } catch (IOException e) {
                throw new FacebookException("could not construct request body", e);
            } catch (JSONException e2) {
                throw new FacebookException("could not construct request body", e2);
            }
        } catch (MalformedURLException e3) {
            throw new FacebookException("could not construct URL for request", e3);
        }
    }

    public static Response executeAndWait(Request request) {
        List<Response> responses = executeBatchAndWait(request);
        if (responses != null && responses.size() == 1) {
            return responses.get(0);
        }
        throw new FacebookException("invalid state: expected a single response");
    }

    public static List<Response> executeBatchAndWait(Request... requests) {
        Validate.notNull(requests, "requests");
        return executeBatchAndWait((Collection<Request>) Arrays.asList(requests));
    }

    public static List<Response> executeBatchAndWait(Collection<Request> requests) {
        return executeBatchAndWait(new RequestBatch(requests));
    }

    public static List<Response> executeBatchAndWait(RequestBatch requests) {
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        try {
            return executeConnectionAndWait(toHttpConnection(requests), requests);
        } catch (Exception ex) {
            List<Response> responses = Response.constructErrorResponses(requests.getRequests(), (HttpURLConnection) null, new FacebookException((Throwable) ex));
            runCallbacks(requests, responses);
            return responses;
        }
    }

    public static RequestAsyncTask executeBatchAsync(Request... requests) {
        Validate.notNull(requests, "requests");
        return executeBatchAsync((Collection<Request>) Arrays.asList(requests));
    }

    public static RequestAsyncTask executeBatchAsync(Collection<Request> requests) {
        return executeBatchAsync(new RequestBatch(requests));
    }

    public static RequestAsyncTask executeBatchAsync(RequestBatch requests) {
        Validate.notEmptyAndContainsNoNulls(requests, "requests");
        RequestAsyncTask asyncTask = new RequestAsyncTask(requests);
        asyncTask.executeOnSettingsExecutor();
        return asyncTask;
    }

    public static List<Response> executeConnectionAndWait(HttpURLConnection connection, Collection<Request> requests) {
        return executeConnectionAndWait(connection, new RequestBatch(requests));
    }

    public static List<Response> executeConnectionAndWait(HttpURLConnection connection, RequestBatch requests) {
        List<Response> responses = Response.fromHttpConnection(connection, requests);
        Utility.disconnectQuietly(connection);
        int numRequests = requests.size();
        if (numRequests != responses.size()) {
            throw new FacebookException(String.format("Received %d responses while expecting %d", new Object[]{Integer.valueOf(responses.size()), Integer.valueOf(numRequests)}));
        }
        runCallbacks(requests, responses);
        HashSet<Session> sessions = new HashSet<>();
        Iterator it = requests.iterator();
        while (it.hasNext()) {
            Request request = (Request) it.next();
            if (request.session != null) {
                sessions.add(request.session);
            }
        }
        Iterator<Session> it2 = sessions.iterator();
        while (it2.hasNext()) {
            it2.next().extendAccessTokenIfNeeded();
        }
        return responses;
    }

    public static RequestAsyncTask executeConnectionAsync(HttpURLConnection connection, RequestBatch requests) {
        return executeConnectionAsync((Handler) null, connection, requests);
    }

    public static RequestAsyncTask executeConnectionAsync(Handler callbackHandler, HttpURLConnection connection, RequestBatch requests) {
        Validate.notNull(connection, "connection");
        RequestAsyncTask asyncTask = new RequestAsyncTask(connection, requests);
        requests.setCallbackHandler(callbackHandler);
        asyncTask.executeOnSettingsExecutor();
        return asyncTask;
    }

    public String toString() {
        return "{Request: " + " session: " + this.session + ", graphPath: " + this.graphPath + ", graphObject: " + this.graphObject + ", restMethod: " + this.restMethod + ", httpMethod: " + this.httpMethod + ", parameters: " + this.parameters + "}";
    }

    static void runCallbacks(final RequestBatch requests, List<Response> responses) {
        int numRequests = requests.size();
        final ArrayList<Pair<Callback, Response>> callbacks = new ArrayList<>();
        for (int i = 0; i < numRequests; i++) {
            Request request = requests.get(i);
            if (request.callback != null) {
                callbacks.add(new Pair(request.callback, responses.get(i)));
            }
        }
        if (callbacks.size() > 0) {
            Runnable runnable = new Runnable() {
                public void run() {
                    Iterator it = callbacks.iterator();
                    while (it.hasNext()) {
                        Pair<Callback, Response> pair = (Pair) it.next();
                        ((Callback) pair.first).onCompleted((Response) pair.second);
                    }
                    for (RequestBatch.Callback batchCallback : requests.getCallbacks()) {
                        batchCallback.onBatchCompleted(requests);
                    }
                }
            };
            Handler callbackHandler = requests.getCallbackHandler();
            if (callbackHandler == null) {
                runnable.run();
            } else {
                callbackHandler.post(runnable);
            }
        }
    }

    static HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", getUserAgent());
        connection.setRequestProperty("Content-Type", getMimeContentType());
        connection.setChunkedStreamingMode(0);
        return connection;
    }

    private void addCommonParameters() {
        if (this.session != null) {
            if (!this.session.isOpened()) {
                throw new FacebookException("Session provided to a Request in un-opened state.");
            } else if (!this.parameters.containsKey("access_token")) {
                String accessToken = this.session.getAccessToken();
                Logger.registerAccessToken(accessToken);
                this.parameters.putString("access_token", accessToken);
            }
        }
        this.parameters.putString(SDK_PARAM, SDK_ANDROID);
        this.parameters.putString(FORMAT_PARAM, FORMAT_JSON);
    }

    private String appendParametersToBaseUrl(String baseUrl) {
        Uri.Builder uriBuilder = new Uri.Builder().encodedPath(baseUrl);
        for (String key : this.parameters.keySet()) {
            Object value = this.parameters.get(key);
            if (value == null) {
                value = "";
            }
            if (isSupportedParameterType(value)) {
                uriBuilder.appendQueryParameter(key, parameterToString(value).toString());
            } else if (this.httpMethod == HttpMethod.GET) {
                throw new IllegalArgumentException(String.format("Unsupported parameter type for GET request: %s", new Object[]{value.getClass().getSimpleName()}));
            }
        }
        return uriBuilder.toString();
    }

    /* access modifiers changed from: package-private */
    public final String getUrlForBatchedRequest() {
        String baseUrl;
        if (this.overriddenURL != null) {
            throw new FacebookException("Can't override URL for a batch request");
        }
        if (this.restMethod != null) {
            baseUrl = ServerProtocol.BATCHED_REST_METHOD_URL_BASE + this.restMethod;
        } else {
            baseUrl = this.graphPath;
        }
        addCommonParameters();
        return appendParametersToBaseUrl(baseUrl);
    }

    /* access modifiers changed from: package-private */
    public final String getUrlForSingleRequest() {
        String baseUrl;
        if (this.overriddenURL != null) {
            return this.overriddenURL.toString();
        }
        if (this.restMethod != null) {
            baseUrl = ServerProtocol.REST_URL_BASE + this.restMethod;
        } else {
            baseUrl = ServerProtocol.GRAPH_URL_BASE + this.graphPath;
        }
        addCommonParameters();
        return appendParametersToBaseUrl(baseUrl);
    }

    private void serializeToBatch(JSONArray batch, Bundle attachments) throws JSONException, IOException {
        JSONObject batchEntry = new JSONObject();
        if (this.batchEntryName != null) {
            batchEntry.put(BATCH_ENTRY_NAME_PARAM, this.batchEntryName);
            batchEntry.put(BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM, this.batchEntryOmitResultOnSuccess);
        }
        if (this.batchEntryDependsOn != null) {
            batchEntry.put(BATCH_ENTRY_DEPENDS_ON_PARAM, this.batchEntryDependsOn);
        }
        String relativeURL = getUrlForBatchedRequest();
        batchEntry.put(BATCH_RELATIVE_URL_PARAM, relativeURL);
        batchEntry.put(BATCH_METHOD_PARAM, this.httpMethod);
        if (this.session != null) {
            Logger.registerAccessToken(this.session.getAccessToken());
        }
        ArrayList<String> attachmentNames = new ArrayList<>();
        for (String key : this.parameters.keySet()) {
            Object value = this.parameters.get(key);
            if (isSupportedAttachmentType(value)) {
                String name = String.format("%s%d", new Object[]{ATTACHMENT_FILENAME_PREFIX, Integer.valueOf(attachments.size())});
                attachmentNames.add(name);
                Utility.putObjectInBundle(attachments, name, value);
            }
        }
        if (!attachmentNames.isEmpty()) {
            batchEntry.put(ATTACHED_FILES_PARAM, TextUtils.join(",", attachmentNames));
        }
        if (this.graphObject != null) {
            final ArrayList<String> keysAndValues = new ArrayList<>();
            processGraphObject(this.graphObject, relativeURL, new KeyValueSerializer() {
                public void writeString(String key, String value) throws IOException {
                    keysAndValues.add(String.format("%s=%s", new Object[]{key, URLEncoder.encode(value, "UTF-8")}));
                }
            });
            batchEntry.put(BATCH_BODY_PARAM, TextUtils.join("&", keysAndValues));
        }
        batch.put(batchEntry);
    }

    private void validate() {
        if (this.graphPath != null && this.restMethod != null) {
            throw new IllegalArgumentException("Only one of a graph path or REST method may be specified per request.");
        }
    }

    /* JADX INFO: finally extract failed */
    static final void serializeToUrlConnection(RequestBatch requests, HttpURLConnection connection) throws IOException, JSONException {
        boolean isPost = false;
        Logger logger = new Logger(LoggingBehavior.REQUESTS, "Request");
        int numRequests = requests.size();
        HttpMethod connectionHttpMethod = numRequests == 1 ? requests.get(0).httpMethod : HttpMethod.POST;
        connection.setRequestMethod(connectionHttpMethod.name());
        URL url = connection.getURL();
        logger.append("Request:\n");
        logger.appendKeyValue("Id", requests.getId());
        logger.appendKeyValue("URL", url);
        logger.appendKeyValue("Method", connection.getRequestMethod());
        logger.appendKeyValue("User-Agent", connection.getRequestProperty("User-Agent"));
        logger.appendKeyValue("Content-Type", connection.getRequestProperty("Content-Type"));
        connection.setConnectTimeout(requests.getTimeout());
        connection.setReadTimeout(requests.getTimeout());
        if (connectionHttpMethod == HttpMethod.POST) {
            isPost = true;
        }
        if (!isPost) {
            logger.log();
            return;
        }
        connection.setDoOutput(true);
        BufferedOutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
        try {
            Serializer serializer = new Serializer(outputStream, logger);
            if (numRequests == 1) {
                Request request = requests.get(0);
                logger.append("  Parameters:\n");
                serializeParameters(request.parameters, serializer);
                logger.append("  Attachments:\n");
                serializeAttachments(request.parameters, serializer);
                if (request.graphObject != null) {
                    processGraphObject(request.graphObject, url.getPath(), serializer);
                }
            } else {
                String batchAppID = getBatchAppId(requests);
                if (Utility.isNullOrEmpty(batchAppID)) {
                    throw new FacebookException("At least one request in a batch must have an open Session, or a default app ID must be specified.");
                }
                serializer.writeString(BATCH_APP_ID_PARAM, batchAppID);
                Bundle attachments = new Bundle();
                serializeRequestsAsJSON(serializer, requests, attachments);
                logger.append("  Attachments:\n");
                serializeAttachments(attachments, serializer);
            }
            outputStream.close();
            logger.log();
        } catch (Throwable th) {
            outputStream.close();
            throw th;
        }
    }

    private static void processGraphObject(GraphObject graphObject2, String path, KeyValueSerializer serializer) throws IOException {
        boolean passByValue;
        boolean isOGAction = false;
        if (path.startsWith("me/") || path.startsWith("/me/")) {
            int colonLocation = path.indexOf(":");
            int questionMarkLocation = path.indexOf("?");
            if (colonLocation <= 3 || (questionMarkLocation != -1 && colonLocation >= questionMarkLocation)) {
                isOGAction = false;
            } else {
                isOGAction = true;
            }
        }
        for (Map.Entry<String, Object> entry : graphObject2.asMap().entrySet()) {
            if (!isOGAction || !entry.getKey().equalsIgnoreCase("image")) {
                passByValue = false;
            } else {
                passByValue = true;
            }
            processGraphObjectProperty(entry.getKey(), entry.getValue(), serializer, passByValue);
        }
    }

    private static void processGraphObjectProperty(String key, Object value, KeyValueSerializer serializer, boolean passByValue) throws IOException {
        Class<?> valueClass = value.getClass();
        if (GraphObject.class.isAssignableFrom(valueClass)) {
            value = ((GraphObject) value).getInnerJSONObject();
            valueClass = value.getClass();
        } else if (GraphObjectList.class.isAssignableFrom(valueClass)) {
            value = ((GraphObjectList) value).getInnerJSONArray();
            valueClass = value.getClass();
        }
        if (JSONObject.class.isAssignableFrom(valueClass)) {
            JSONObject jsonObject = (JSONObject) value;
            if (passByValue) {
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String propertyName = keys.next();
                    processGraphObjectProperty(String.format("%s[%s]", new Object[]{key, propertyName}), jsonObject.opt(propertyName), serializer, passByValue);
                }
            } else if (jsonObject.has("id")) {
                processGraphObjectProperty(key, jsonObject.optString("id"), serializer, passByValue);
            } else if (jsonObject.has("url")) {
                processGraphObjectProperty(key, jsonObject.optString("url"), serializer, passByValue);
            }
        } else if (JSONArray.class.isAssignableFrom(valueClass)) {
            JSONArray jsonArray = (JSONArray) value;
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                processGraphObjectProperty(String.format("%s[%d]", new Object[]{key, Integer.valueOf(i)}), jsonArray.opt(i), serializer, passByValue);
            }
        } else if (String.class.isAssignableFrom(valueClass) || Number.class.isAssignableFrom(valueClass) || Boolean.class.isAssignableFrom(valueClass)) {
            serializer.writeString(key, value.toString());
        } else if (Date.class.isAssignableFrom(valueClass)) {
            KeyValueSerializer keyValueSerializer = serializer;
            String str = key;
            keyValueSerializer.writeString(str, new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US).format((Date) value));
        }
    }

    private static void serializeParameters(Bundle bundle, Serializer serializer) throws IOException {
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (isSupportedParameterType(value)) {
                serializer.writeObject(key, value);
            }
        }
    }

    private static void serializeAttachments(Bundle bundle, Serializer serializer) throws IOException {
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (isSupportedAttachmentType(value)) {
                serializer.writeObject(key, value);
            }
        }
    }

    private static void serializeRequestsAsJSON(Serializer serializer, Collection<Request> requests, Bundle attachments) throws JSONException, IOException {
        JSONArray batch = new JSONArray();
        for (Request request : requests) {
            request.serializeToBatch(batch, attachments);
        }
        serializer.writeString(BATCH_PARAM, batch.toString());
    }

    private static String getMimeContentType() {
        return String.format("multipart/form-data; boundary=%s", new Object[]{MIME_BOUNDARY});
    }

    private static String getUserAgent() {
        if (userAgent == null) {
            userAgent = String.format("%s.%s", new Object[]{USER_AGENT_BASE, FacebookSdkVersion.BUILD});
        }
        return userAgent;
    }

    private static String getBatchAppId(RequestBatch batch) {
        if (!Utility.isNullOrEmpty(batch.getBatchApplicationId())) {
            return batch.getBatchApplicationId();
        }
        Iterator it = batch.iterator();
        while (it.hasNext()) {
            Session session2 = ((Request) it.next()).session;
            if (session2 != null) {
                return session2.getApplicationId();
            }
        }
        return defaultBatchApplicationId;
    }

    /* access modifiers changed from: private */
    public static <T extends GraphObject> List<T> typedListFromResponse(Response response, Class<T> clazz) {
        GraphObjectList<GraphObject> data;
        GraphMultiResult multiResult = (GraphMultiResult) response.getGraphObjectAs(GraphMultiResult.class);
        if (multiResult == null || (data = multiResult.getData()) == null) {
            return null;
        }
        return data.castToListOf(clazz);
    }

    private static boolean isSupportedAttachmentType(Object value) {
        return (value instanceof Bitmap) || (value instanceof byte[]) || (value instanceof ParcelFileDescriptor);
    }

    /* access modifiers changed from: private */
    public static boolean isSupportedParameterType(Object value) {
        return (value instanceof String) || (value instanceof Boolean) || (value instanceof Number) || (value instanceof Date);
    }

    /* access modifiers changed from: private */
    public static String parameterToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        }
        if ((value instanceof Boolean) || (value instanceof Number)) {
            return value.toString();
        }
        if (value instanceof Date) {
            return new SimpleDateFormat(ISO_8601_FORMAT_STRING, Locale.US).format(value);
        }
        throw new IllegalArgumentException("Unsupported parameter type.");
    }

    private static class Serializer implements KeyValueSerializer {
        private boolean firstWrite = true;
        private final Logger logger;
        private final BufferedOutputStream outputStream;

        public Serializer(BufferedOutputStream outputStream2, Logger logger2) {
            this.outputStream = outputStream2;
            this.logger = logger2;
        }

        public void writeObject(String key, Object value) throws IOException {
            if (Request.isSupportedParameterType(value)) {
                writeString(key, Request.parameterToString(value));
            } else if (value instanceof Bitmap) {
                writeBitmap(key, (Bitmap) value);
            } else if (value instanceof byte[]) {
                writeBytes(key, (byte[]) value);
            } else if (value instanceof ParcelFileDescriptor) {
                writeFile(key, (ParcelFileDescriptor) value);
            } else {
                throw new IllegalArgumentException("value is not a supported type: String, Bitmap, byte[]");
            }
        }

        public void writeString(String key, String value) throws IOException {
            writeContentDisposition(key, (String) null, (String) null);
            writeLine("%s", value);
            writeRecordBoundary();
            if (this.logger != null) {
                this.logger.appendKeyValue("    " + key, value);
            }
        }

        public void writeBitmap(String key, Bitmap bitmap) throws IOException {
            writeContentDisposition(key, key, "image/png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, this.outputStream);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            this.logger.appendKeyValue("    " + key, "<Image>");
        }

        public void writeBytes(String key, byte[] bytes) throws IOException {
            writeContentDisposition(key, key, "content/unknown");
            this.outputStream.write(bytes);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            this.logger.appendKeyValue("    " + key, String.format("<Data: %d>", new Object[]{Integer.valueOf(bytes.length)}));
        }

        /* JADX WARNING: Removed duplicated region for block: B:22:0x0061  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0066  */
        public void writeFile(String key, ParcelFileDescriptor descriptor) throws IOException {
            BufferedInputStream bufferedInputStream;
            writeContentDisposition(key, key, "content/unknown");
            ParcelFileDescriptor.AutoCloseInputStream inputStream = null;
            BufferedInputStream bufferedInputStream2 = null;
            int totalBytes = 0;
            try {
                ParcelFileDescriptor.AutoCloseInputStream inputStream2 = new ParcelFileDescriptor.AutoCloseInputStream(descriptor);
                try {
                    bufferedInputStream = new BufferedInputStream(inputStream2);
                } catch (Throwable th) {
                    th = th;
                    inputStream = inputStream2;
                    if (bufferedInputStream2 != null) {
                        bufferedInputStream2.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
                try {
                    byte[] buffer = new byte[8192];
                    while (true) {
                        int bytesRead = bufferedInputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        this.outputStream.write(buffer, 0, bytesRead);
                        totalBytes += bytesRead;
                    }
                    if (bufferedInputStream != null) {
                        bufferedInputStream.close();
                    }
                    if (inputStream2 != null) {
                        inputStream2.close();
                    }
                    writeLine("", new Object[0]);
                    writeRecordBoundary();
                    this.logger.appendKeyValue("    " + key, String.format("<Data: %d>", new Object[]{Integer.valueOf(totalBytes)}));
                } catch (Throwable th2) {
                    th = th2;
                    bufferedInputStream2 = bufferedInputStream;
                    inputStream = inputStream2;
                    if (bufferedInputStream2 != null) {
                    }
                    if (inputStream != null) {
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (bufferedInputStream2 != null) {
                }
                if (inputStream != null) {
                }
                throw th;
            }
        }

        public void writeRecordBoundary() throws IOException {
            writeLine("--%s", Request.MIME_BOUNDARY);
        }

        public void writeContentDisposition(String name, String filename, String contentType) throws IOException {
            write("Content-Disposition: form-data; name=\"%s\"", name);
            if (filename != null) {
                write("; filename=\"%s\"", filename);
            }
            writeLine("", new Object[0]);
            if (contentType != null) {
                writeLine("%s: %s", "Content-Type", contentType);
            }
            writeLine("", new Object[0]);
        }

        public void write(String format, Object... args) throws IOException {
            if (this.firstWrite) {
                this.outputStream.write("--".getBytes());
                this.outputStream.write(Request.MIME_BOUNDARY.getBytes());
                this.outputStream.write(CharsetUtil.CRLF.getBytes());
                this.firstWrite = false;
            }
            this.outputStream.write(String.format(format, args).getBytes());
        }

        public void writeLine(String format, Object... args) throws IOException {
            write(format, args);
            write(CharsetUtil.CRLF, new Object[0]);
        }
    }
}
