package com.example.franco.myapplication2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.franco.myapplication2.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by franco on 9/27/17.
 */

public class ReservasFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/
        View v = inflater.inflate(R.layout.reservas_layout, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            SearchTask searchTask = new SearchTask();
            searchTask.execute(new URL ("https://canchas.ml/clubes"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public class SearchTask extends AsyncTask<URL, Void, String> {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(String githubSearchResults) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                TextView infoView = getView().findViewById(R.id.displayInfo);
                infoView.setText("");
                try {
                    JSONArray canchas = new JSONArray(githubSearchResults);
                    for(int i=0; i<canchas.length();i++){
                        infoView.append(canchas.getJSONObject(i).getString("nombre"));
                        infoView.append(":\n");
                        for(int j=0; j< canchas.getJSONObject(i).getJSONArray("reservas").length();j++ ) {
                            infoView.append(" - desde:");
                            infoView.append(canchas.getJSONObject(i).getJSONArray("reservas").getJSONObject(i).getString("desde"));
                            infoView.append("\n - hasta:");
                            infoView.append(canchas.getJSONObject(i).getJSONArray("reservas").getJSONObject(i).getString("hasta"));
                            infoView.append("\n");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
