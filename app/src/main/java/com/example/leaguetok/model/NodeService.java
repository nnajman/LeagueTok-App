package com.example.leaguetok.model;

import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.leaguetok.LeagueTokApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeService {
    private final String IMIT_VIDEOS_API = "imitationVideo";
    private final String ORIG_VIDEOS_API = "originalVideo";

    public interface RequestListener<JSONObject> {
        void onSuccess(JSONObject response);
        void onError(VolleyError error);
    }

    public void getAllOrigVideos(Long lastUpdated, Model.AsyncListener<List<OriginalVideo>> listener) {
        final String getVideosURL = LeagueTokApplication.serverUrl + "/" + ORIG_VIDEOS_API + "/" + lastUpdated;
        JsonArrayRequest jsArrRequest = new
                JsonArrayRequest(Request.Method.GET,
                getVideosURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<OriginalVideo> originalVideos = new ArrayList<OriginalVideo>();
                        for(int i = 0; i < response.length(); i++) {
                            OriginalVideo ov = new OriginalVideo();
                            try {
                                ov.fromMap(((JSONObject)response.get(i)));
                                originalVideos.add(ov);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        listener.onComplete(originalVideos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Change action on error
                Log.d("TAG", error.getMessage());
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void getAllImitVideos(Long lastUpdated, Model.AsyncListener<List<ImitationVideo>> listener) {
        final String getVideosURL = LeagueTokApplication.serverUrl + "/" + IMIT_VIDEOS_API + "/" + lastUpdated;
        JsonArrayRequest jsArrRequest = new
                JsonArrayRequest(Request.Method.GET,
                getVideosURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<ImitationVideo> imitationVideos = new ArrayList<ImitationVideo>();
                        for(int i = 0; i < response.length(); i++) {
                            ImitationVideo iv = new ImitationVideo();
                            try {
                                iv.fromMap(((JSONObject)response.get(i)));
                                imitationVideos.add(iv);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        listener.onComplete(imitationVideos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Change action on error
                Log.d("TAG", error.getMessage());
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void uploadVideo(String uri, String uid, String origVideoId, RequestListener<JSONObject> listener) {
        final String postVideoURL = LeagueTokApplication.serverUrl + "/" + IMIT_VIDEOS_API;
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("link", uri);
        params.put("uid", uid);
        params.put("sourceId", origVideoId);

        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.POST,
                postVideoURL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });

        jsObjRequest.setShouldCache(false);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsObjRequest);
    }
}
