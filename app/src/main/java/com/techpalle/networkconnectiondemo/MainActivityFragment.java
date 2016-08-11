package com.techpalle.networkconnectiondemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    Button getBtn;
    TextView responseTextView;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        getBtn = (Button) view.findViewById(R.id.getBtn);
        responseTextView = (TextView) view.findViewById(R.id.responseTextView);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connectivityManager = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();

                ConnectivityManager connectivityManager1 = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info1 = connectivityManager1.getActiveNetworkInfo();

                if(info != null && info.isConnected()){
                    MyTask myTask = new MyTask();
                    myTask.execute("http://techpalle.com");
                    MyTask1 myTask1 = new MyTask1();
                    myTask1.execute("http://api.androidhive.info/contacts/");
                }
                else
                    Toast.makeText(getActivity(),"Internet not connected", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private class MyTask1 extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                //Prepare Url
                URL url = new URL(params[0]);

                //Open connection with the server
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                //Open input stream
                InputStream inputStream = connection.getInputStream();

                //Read from input stream
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                //Read more efficiently with Buffer Reader
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = bufferedReader.readLine();

                stringBuilder = new StringBuilder();

                if(line!= null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            responseTextView.setText(s);
            Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();
        }
    }

    private class MyTask extends AsyncTask<String, Void, String>{

        URL url;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder= new StringBuilder();


        @Override
        protected String doInBackground(String... params) {

            try {
                url= new URL(params[0]);

                /*
                While connecting to internet, if there is no internet, or poor internet,
                        or server is down, it throws IO exception*/

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                do{
                    line = bufferedReader.readLine();
                    stringBuilder.append(line);
                } while(line != null);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpURLConnection.disconnect();

            }


            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals(null)){
                Toast.makeText(getActivity(),"Something went wrong", Toast.LENGTH_LONG).show();
            } else{
                responseTextView.setText(s);
            }
            super.onPostExecute(s);
        }
    }
}
