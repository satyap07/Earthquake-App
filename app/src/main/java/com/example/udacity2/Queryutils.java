package com.example.udacity2;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class Queryutils {

    /**
    * Creating function to return arraylist containing all the details
     */
    public static ArrayList<EarthQuake> fetchData(String requestURL) throws MalformedURLException {

        Log.i("Date","Fetching data called");
        /**
         * create URL
         */

        URL url= createURL(requestURL);
        String JSONresponse=null;
        try {
            JSONresponse= makeHTTPrequest(url);

        }catch (IOException e){

        }
        if(JSONresponse==null){
            return  null;
        }

            ArrayList<EarthQuake>earthQuakes=new ArrayList<>();
       try {
            JSONObject root= new JSONObject(JSONresponse);
            JSONArray descAraay= root.optJSONArray("features");
            for(int i=0;i<descAraay.length();i++){
                JSONObject jsonObject=descAraay.optJSONObject(i);
                JSONObject jsonObject1=jsonObject.optJSONObject("properties");
                double magnitude=jsonObject1.optDouble("mag");
                String place=jsonObject1.optString("place");
                long date=jsonObject1.optLong("time");
                String url1=jsonObject1.optString("url");

                earthQuakes.add(new EarthQuake(magnitude,place,date,url1));

            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
        return earthQuakes;


    }
    /**
    *make HTTP request and return string response
    */
    private static String makeHTTPrequest(URL url) throws IOException {
        String jsonResponse="";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /**
             *  If the request was successful (response code 200),
             *  then read the input stream and parse the response.
             */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.i("error", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.i("error", "Problem retrieving the earthquake JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
     /**
     * This function creates url from the given string
     */
    private static URL createURL(String requestURL) throws MalformedURLException {
        URL url= null;
        if(requestURL==null){
            return null;
        }else {
            url=new URL(requestURL);
            return url;
        }
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


}
