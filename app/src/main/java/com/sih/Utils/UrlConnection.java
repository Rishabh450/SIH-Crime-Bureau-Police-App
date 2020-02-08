package com.sih.Utils;

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


public class UrlConnection {


    HttpURLConnection urlConnect = null;
    BufferedReader bufferedReader = null;
    private final String apiKey="ukARpEXeA9PI9rz6XIlHIUwzmPY0lPEx";

    public String Url(String url) {
        // HttpURLConnection urlConnect;
        {
            try {
                URL connect = new URL(url);
                try {
                    urlConnect = (HttpURLConnection) connect.openConnection();
                    urlConnect.connect();

                    InputStream is = urlConnect.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String line = "";
                    StringBuffer buffer = new StringBuffer();

                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();
                    //System.out.println(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } finally {
                if (urlConnect != null) {
                    urlConnect.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return url;
    }

    public String Weather(String key) {
        String forecast = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/" + key + "?apikey="+apiKey+"&details=true&metric=true";
        StringBuffer bufferForecast = null;
        try {
            URL connect = new URL(forecast);
            try {
                urlConnect = (HttpURLConnection) connect.openConnection();
                urlConnect.connect();

                InputStream is = urlConnect.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                bufferForecast = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    bufferForecast.append(line);
                }

                //System.out.println(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bufferForecast.toString();
    }

    public String HourlyData(String key)
    {
        String hourlyData="https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/" + key + "?apikey="+apiKey+"&details=true&metric=true";
        StringBuffer bufferForecast=null;
        try {
            URL connect = new URL(hourlyData);
            try {
                urlConnect = (HttpURLConnection) connect.openConnection();
                urlConnect.connect();

                InputStream is = urlConnect.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                bufferForecast = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    bufferForecast.append(line);
                }

                //System.out.println(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bufferForecast.toString();
    }
    public String CurrentConditions(String key) {
        String currentconditions = "https://dataservice.accuweather.com/currentconditions/v1/" + key + "?apikey="+apiKey+"&details=true";
        StringBuffer bufferForecast = null;
        try {
            URL connect = new URL(currentconditions);
            try {
                urlConnect = (HttpURLConnection) connect.openConnection();
                urlConnect.connect();

                InputStream is = urlConnect.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                bufferForecast = new StringBuffer();

                while ((line = bufferedReader.readLine()) != null) {
                    bufferForecast.append(line);
                }

                //System.out.println(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bufferForecast.toString();
    }


    public static void main(String args[])
    {
      /*  String urll="http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=Z0kvPGPf9xFFn8XYQw1TBpGVE83HA7fz&q=28.7041%2C77.1025";
        UrlConnection urlconnection=new UrlConnection();
        String result=urlconnection.Url(urll);
        System.out.println("result is");
        System.out.println(result);
*/
    }
}