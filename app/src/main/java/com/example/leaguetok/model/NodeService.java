package com.example.leaguetok.model;

import android.content.res.Resources;
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
import com.example.leaguetok.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NodeService {
    private final String IMIT_VIDEOS_API = "imitationVideo";
    private final String ORIG_VIDEOS_API = "originalVideo";
    private final String USERS_API = "user";

    public interface RequestListener<JSONObject> {
        void onSuccess(JSONObject response) throws JSONException;
        void onError(VolleyError error);
    }

    private String getServerUrl() {
        try {
            InputStream rawResource = LeagueTokApplication.context.getResources().openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty("serverUrl");
        }
        catch (IOException e) {
            return null;
        }
    }

    public void getAllOrigVideos(Long lastUpdated, Model.AsyncListener<List<OriginalVideo>> listener) {
        final String getVideosURL = getServerUrl() + "/" + ORIG_VIDEOS_API + "/" + lastUpdated;
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
                                listener.onError(null);
                            }
                        }

                        listener.onComplete(originalVideos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(null);
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void getAllImitVideos(Long lastUpdated, Model.AsyncListener<List<ImitationVideo>> listener) {
        final String getVideosURL = getServerUrl() + "/" + IMIT_VIDEOS_API + "/" + lastUpdated;
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
                                listener.onError(null);
                            }
                        }

                        listener.onComplete(imitationVideos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(null);
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void getAllUsers(Long lastUpdated, Model.AsyncListener<List<User>> listener) {
        final String getUsersURL = getServerUrl() + "/" + USERS_API + "/all/" + lastUpdated;
        JsonArrayRequest jsArrRequest = new
                JsonArrayRequest(Request.Method.GET,
                getUsersURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<User> users = new ArrayList<User>();
                        for(int i = 0; i < response.length(); i++) {
                            User user = new User();
                            try {
                                user.fromMap(((JSONObject)response.get(i)));
                                users.add(user);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError(null);
                            }
                        }

                        listener.onComplete(users);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(null);
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void getUserImitVideos(String uid, Long lastUpdated, Model.AsyncListener<List<ImitationVideo>> listener) {
        final String getVideosURL = getServerUrl() + "/" + IMIT_VIDEOS_API + "/" + uid + "/" + lastUpdated;
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
                                listener.onError(null);
                            }
                        }

                        listener.onComplete(imitationVideos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(null);
            }
        });

        jsArrRequest.setShouldCache(false);
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsArrRequest);
    }

    public void uploadVideo(String uri, String uid, String origVideoId, RequestListener<JSONObject> listener) {
        final String postVideoURL = getServerUrl() + "/" + IMIT_VIDEOS_API;
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
                        try {
                            listener.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void uploadOriginalVideo(String uri, String name, String performer, RequestListener<JSONObject> listener) {
        final String postVideoURL = getServerUrl() + "/" + ORIG_VIDEOS_API;
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("uri", uri);
        params.put("name", name);
        params.put("performer", performer);

        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.POST,
                postVideoURL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void addNewUser(String uid, String fullName, String photoUrl, Model.AsyncListener listener) {
        final String addNewUserUrl = getServerUrl() + "/" + USERS_API + "/sign-up";
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("uid", uid);
        params.put("fullName", fullName);
        params.put("photoUrl", photoUrl);

        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.POST,
                addNewUserUrl,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onComplete(null);
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

    public void getUserById(String uid, RequestListener<JSONObject> listener) {
        final String getUserByIdUrl = getServerUrl() + "/" + USERS_API + "/" + uid;

        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.GET,
                getUserByIdUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void sendDeviceToken(String uid, String token, Model.AsyncListener listener) {
        final String sendDeviceTokenUrl = getServerUrl() + "/" + USERS_API + "/device";
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("uid", uid);
        params.put("token", token);

        JsonObjectRequest jsObjRequest = new
                JsonObjectRequest(Request.Method.PUT,
                sendDeviceTokenUrl,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onComplete(null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });

        jsObjRequest.setShouldCache(false);
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(LeagueTokApplication.context).add(jsObjRequest);
    }
}
