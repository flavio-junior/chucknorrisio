package br.com.js.chucknorrisio.datasource;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRemoteDatasource {

    public interface ListCategoriesCallback {
        void onSuccess(List<String> response);

        void onError(String message);

        void onComplete();
    }

    public void findAll(ListCategoriesCallback callback) {
        HTTPClient.retrofit().create(ChuckNorrisAPI.class)
                .findAll()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful())
                            callback.onSuccess(response.body());

                        callback.onComplete();
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        callback.onError(t.getMessage());
                        callback.onComplete();
                    }
                });

        // new CategoryTask(callback).execute();
    }

    private static class CategoryTask extends AsyncTask<Void, Void, List<String>> {

        private final ListCategoriesCallback callback;
        private String errorMessage;

        public CategoryTask(ListCategoriesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> response = new ArrayList<>();

            HttpsURLConnection urlConnection;

            try {
                URL url = new URL(Endpoint.GET_CATEGORIES);
                urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setConnectTimeout(2000);
                urlConnection.setReadTimeout(2000);

                int responseCode = urlConnection.getResponseCode();
                if (responseCode > 400) {
                    throw new EOFException("Erro na comunicação do servidor");
                }

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
                jsonReader.beginArray();

                while (jsonReader.hasNext()) {
                    response.add(jsonReader.nextString());
                }
                jsonReader.endArray();

            } catch (MalformedURLException e) {
                errorMessage = e.getMessage();
            } catch (IOException e) {
                errorMessage = e.getMessage();
            }

            return response;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (errorMessage != null) {
                Log.i("TESTE", errorMessage);
                callback.onError(errorMessage);
            } else {
                Log.i("TESTE", strings.toString());
                callback.onSuccess(strings);
            }
            callback.onComplete();
        }
    }
}
