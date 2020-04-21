package com.facebook;

import android.content.Context;
import com.facebook.internal.CacheableRequestBatch;
import com.facebook.internal.FileLruCache;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Response {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    private static final int INVALID_SESSION_FACEBOOK_ERROR_CODE = 190;
    public static final String NON_JSON_RESPONSE_PROPERTY = "FACEBOOK_NON_JSON_RESULT";
    private static final String RESPONSE_CACHE_TAG = "ResponseCache";
    private static final String RESPONSE_LOG_TAG = "Response";
    private static FileLruCache responseCache;
    private final HttpURLConnection connection;
    private final FacebookRequestError error;
    private final GraphObject graphObject;
    private final GraphObjectList<GraphObject> graphObjectList;
    private final boolean isFromCache;
    private final Request request;

    interface PagedResults extends GraphObject {
        GraphObjectList<GraphObject> getData();

        PagingInfo getPaging();
    }

    public enum PagingDirection {
        NEXT,
        PREVIOUS
    }

    interface PagingInfo extends GraphObject {
        String getNext();

        String getPrevious();
    }

    static {
        boolean z;
        if (!Response.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    Response(Request request2, HttpURLConnection connection2, GraphObject graphObject2, boolean isFromCache2) {
        this.request = request2;
        this.connection = connection2;
        this.graphObject = graphObject2;
        this.graphObjectList = null;
        this.isFromCache = isFromCache2;
        this.error = null;
    }

    Response(Request request2, HttpURLConnection connection2, GraphObjectList<GraphObject> graphObjects, boolean isFromCache2) {
        this.request = request2;
        this.connection = connection2;
        this.graphObject = null;
        this.graphObjectList = graphObjects;
        this.isFromCache = isFromCache2;
        this.error = null;
    }

    Response(Request request2, HttpURLConnection connection2, FacebookRequestError error2) {
        this.request = request2;
        this.connection = connection2;
        this.graphObject = null;
        this.graphObjectList = null;
        this.isFromCache = false;
        this.error = error2;
    }

    public final FacebookRequestError getError() {
        return this.error;
    }

    public final GraphObject getGraphObject() {
        return this.graphObject;
    }

    public final <T extends GraphObject> T getGraphObjectAs(Class<T> graphObjectClass) {
        if (this.graphObject == null) {
            return null;
        }
        if (graphObjectClass != null) {
            return this.graphObject.cast(graphObjectClass);
        }
        throw new NullPointerException("Must pass in a valid interface that extends GraphObject");
    }

    public final GraphObjectList<GraphObject> getGraphObjectList() {
        return this.graphObjectList;
    }

    public final <T extends GraphObject> GraphObjectList<T> getGraphObjectListAs(Class<T> graphObjectClass) {
        if (this.graphObjectList == null) {
            return null;
        }
        return this.graphObjectList.castToListOf(graphObjectClass);
    }

    public final HttpURLConnection getConnection() {
        return this.connection;
    }

    public Request getRequest() {
        return this.request;
    }

    public Request getRequestForPagedResults(PagingDirection direction) {
        PagingInfo pagingInfo;
        String link = null;
        if (!(this.graphObject == null || (pagingInfo = ((PagedResults) this.graphObject.cast(PagedResults.class)).getPaging()) == null)) {
            link = direction == PagingDirection.NEXT ? pagingInfo.getNext() : pagingInfo.getPrevious();
        }
        if (Utility.isNullOrEmpty(link)) {
            return null;
        }
        if (link != null && link.equals(this.request.getUrlForSingleRequest())) {
            return null;
        }
        try {
            return new Request(this.request.getSession(), new URL(link));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String toString() {
        String responseCode;
        try {
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(this.connection != null ? this.connection.getResponseCode() : 200);
            responseCode = String.format("%d", objArr);
        } catch (IOException e) {
            responseCode = "unknown";
        }
        return "{Response: " + " responseCode: " + responseCode + ", graphObject: " + this.graphObject + ", error: " + this.error + ", isFromCache:" + this.isFromCache + "}";
    }

    public final boolean getIsFromCache() {
        return this.isFromCache;
    }

    static FileLruCache getResponseCache() {
        Context applicationContext;
        if (responseCache == null && (applicationContext = Session.getStaticContext()) != null) {
            responseCache = new FileLruCache(applicationContext, RESPONSE_CACHE_TAG, new FileLruCache.Limits());
        }
        return responseCache;
    }

    static List<Response> fromHttpConnection(HttpURLConnection connection2, RequestBatch requests) {
        List<Response> constructErrorResponses;
        InputStream stream;
        InputStream interceptStream;
        InputStream stream2 = null;
        FileLruCache cache = null;
        String cacheKey = null;
        if (requests instanceof CacheableRequestBatch) {
            CacheableRequestBatch cacheableRequestBatch = (CacheableRequestBatch) requests;
            cache = getResponseCache();
            cacheKey = cacheableRequestBatch.getCacheKeyOverride();
            if (Utility.isNullOrEmpty(cacheKey)) {
                if (requests.size() == 1) {
                    cacheKey = requests.get(0).getUrlForSingleRequest();
                } else {
                    Logger.log(LoggingBehavior.REQUESTS, RESPONSE_CACHE_TAG, "Not using cache for cacheable request because no key was specified");
                }
            }
            if (!cacheableRequestBatch.getForceRoundTrip() && cache != null && !Utility.isNullOrEmpty(cacheKey)) {
                try {
                    stream2 = cache.get(cacheKey);
                    if (stream2 != null) {
                        constructErrorResponses = createResponsesFromStream(stream2, (HttpURLConnection) null, requests, true);
                        return constructErrorResponses;
                    }
                    Utility.closeQuietly(stream2);
                } catch (FacebookException e) {
                } catch (JSONException e2) {
                } catch (IOException e3) {
                } finally {
                    Utility.closeQuietly(stream2);
                }
            }
        }
        try {
            if (connection2.getResponseCode() >= 400) {
                stream = connection2.getErrorStream();
            } else {
                stream = connection2.getInputStream();
                if (!(cache == null || cacheKey == null || stream == null || (interceptStream = cache.interceptAndPut(cacheKey, stream)) == null)) {
                    stream = interceptStream;
                }
            }
            constructErrorResponses = createResponsesFromStream(stream2, connection2, requests, false);
        } catch (FacebookException facebookException) {
            Logger.log(LoggingBehavior.REQUESTS, "Response", "Response <Error>: %s", facebookException);
            constructErrorResponses = constructErrorResponses(requests, connection2, facebookException);
        } catch (JSONException exception) {
            Logger.log(LoggingBehavior.REQUESTS, "Response", "Response <Error>: %s", exception);
            constructErrorResponses = constructErrorResponses(requests, connection2, new FacebookException((Throwable) exception));
        } catch (IOException exception2) {
            Logger.log(LoggingBehavior.REQUESTS, "Response", "Response <Error>: %s", exception2);
            constructErrorResponses = constructErrorResponses(requests, connection2, new FacebookException((Throwable) exception2));
        } finally {
            Utility.closeQuietly(stream2);
        }
        return constructErrorResponses;
    }

    static List<Response> createResponsesFromStream(InputStream stream, HttpURLConnection connection2, RequestBatch requests, boolean isFromCache2) throws FacebookException, JSONException, IOException {
        String responseString = Utility.readStreamToString(stream);
        Logger.log(LoggingBehavior.INCLUDE_RAW_RESPONSES, "Response", "Response (raw)\n  Size: %d\n  Response:\n%s\n", Integer.valueOf(responseString.length()), responseString);
        List<Response> responses = createResponsesFromObject(connection2, requests, new JSONTokener(responseString).nextValue(), isFromCache2);
        Logger.log(LoggingBehavior.REQUESTS, "Response", "Response\n  Id: %s\n  Size: %d\n  Responses:\n%s\n", requests.getId(), Integer.valueOf(responseString.length()), responses);
        return responses;
    }

    private static List<Response> createResponsesFromObject(HttpURLConnection connection2, List<Request> requests, Object object, boolean isFromCache2) throws FacebookException, JSONException {
        if ($assertionsDisabled || connection2 != null || isFromCache2) {
            int numRequests = requests.size();
            List<Response> responses = new ArrayList<>(numRequests);
            Object originalResult = object;
            if (numRequests == 1) {
                Request request2 = requests.get(0);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(BODY_KEY, object);
                    jsonObject.put(CODE_KEY, connection2 != null ? connection2.getResponseCode() : 200);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(jsonObject);
                    object = jsonArray;
                } catch (JSONException e) {
                    responses.add(new Response(request2, connection2, new FacebookRequestError(connection2, e)));
                } catch (IOException e2) {
                    responses.add(new Response(request2, connection2, new FacebookRequestError(connection2, e2)));
                }
            }
            if (!(object instanceof JSONArray) || ((JSONArray) object).length() != numRequests) {
                throw new FacebookException("Unexpected number of results");
            }
            JSONArray jsonArray2 = (JSONArray) object;
            for (int i = 0; i < jsonArray2.length(); i++) {
                Request request3 = requests.get(i);
                try {
                    responses.add(createResponseFromObject(request3, connection2, jsonArray2.get(i), isFromCache2, originalResult));
                } catch (JSONException e3) {
                    responses.add(new Response(request3, connection2, new FacebookRequestError(connection2, e3)));
                } catch (FacebookException e4) {
                    responses.add(new Response(request3, connection2, new FacebookRequestError(connection2, e4)));
                }
            }
            return responses;
        }
        throw new AssertionError();
    }

    private static Response createResponseFromObject(Request request2, HttpURLConnection connection2, Object object, boolean isFromCache2, Object originalResult) throws JSONException {
        Session session;
        if (object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            FacebookRequestError error2 = FacebookRequestError.checkResponseAndCreateError(jsonObject, originalResult, connection2);
            if (error2 != null) {
                if (error2.getErrorCode() == INVALID_SESSION_FACEBOOK_ERROR_CODE && (session = request2.getSession()) != null) {
                    session.closeAndClearTokenInformation();
                }
                return new Response(request2, connection2, error2);
            }
            Object body = Utility.getStringPropertyAsJSON(jsonObject, BODY_KEY, NON_JSON_RESPONSE_PROPERTY);
            if (body instanceof JSONObject) {
                return new Response(request2, connection2, GraphObject.Factory.create((JSONObject) body), isFromCache2);
            }
            if (body instanceof JSONArray) {
                return new Response(request2, connection2, GraphObject.Factory.createList((JSONArray) body, GraphObject.class), isFromCache2);
            }
            object = JSONObject.NULL;
        }
        if (object == JSONObject.NULL) {
            return new Response(request2, connection2, (GraphObject) null, isFromCache2);
        }
        throw new FacebookException("Got unexpected object type in response, class: " + object.getClass().getSimpleName());
    }

    static List<Response> constructErrorResponses(List<Request> requests, HttpURLConnection connection2, FacebookException error2) {
        int count = requests.size();
        List<Response> responses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            responses.add(new Response(requests.get(i), connection2, new FacebookRequestError(connection2, error2)));
        }
        return responses;
    }
}
