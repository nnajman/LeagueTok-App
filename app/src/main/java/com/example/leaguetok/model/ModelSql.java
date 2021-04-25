package com.example.leaguetok.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

public class ModelSql {
    public void insertOrigVideo(OriginalVideo originalVideo, Model.AsyncListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.originalVideoDao().insertAll(originalVideo);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) listener.onComplete(originalVideo);
            }
        }

        new MyAsyncTask().execute();
    }

    public void deleteOrigVideo(OriginalVideo originalVideo, Model.AsyncListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.originalVideoDao().delete(originalVideo);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (listener != null) {
                    listener.onComplete(true);
                }

            }
        }

        new MyAsyncTask().execute();
    }

    public LiveData<OriginalVideo> getOrigVideoById(String id) {
        return AppLocalDB.db.originalVideoDao().getOrigVideoByID(id);
    }

    public void insertImitVideo(ImitationVideo imitationVideo, Model.AsyncListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.imitationVideoDao().insertAll(imitationVideo);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null) listener.onComplete(imitationVideo);
            }
        }

        new MyAsyncTask().execute();
    }

    public void deleteImitVideo(ImitationVideo imitationVideo, Model.AsyncListener<Boolean> listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDB.db.imitationVideoDao().delete(imitationVideo);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (listener != null) {
                    listener.onComplete(true);
                }

            }
        }

        new MyAsyncTask().execute();
    }
}
