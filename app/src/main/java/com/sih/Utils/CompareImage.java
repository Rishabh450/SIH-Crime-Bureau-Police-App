package com.sih.Utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;


public class CompareImage extends AsyncTask<Void, Void, Void>
{
    Context context;
    File file,file2;
    long fileno,curr;

    public CompareImage(Context context, File file, File file2,long fileno) {
        this.context = context;
        this.file = file;
        this.file2 = file2;
        pdLoading = new ProgressDialog(context);
        this.fileno=fileno;

    }

    double confidence=0.0;
    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    final File sdCard = Environment.getExternalStorageDirectory();

    private  String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private  String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }

    public  byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }

    protected  byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        String boundaryString = getBoundary();
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }
    ProgressDialog pdLoading;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("confidencekitna"," loading" );
        //this method will be running on UI thread
        pdLoading.setMessage("\tSearching...");
        pdLoading.show();
    }
    @Override
    protected Void doInBackground(Void... params) {

        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
        URL url = null;
        try {
            url = new URL("https://firebasestorage.googleapis.com/v0/b/policeapp-397c4.appspot.com/o/-M-HJEv6_No92bBpXlU5%2Fprofile_pic.jpg?alt=media&token=7e9898f4-e141-4c8a-9fce-baa01585be6c");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // File file = Paths.get(url.toURI()).toFile();
            //File file = null;
            //FileUtils.copyURLToFile(url, file);

            //file = new File(sdCard.getAbsolutePath() + "/HashContact"+"/Pictures/"+"13.jpg");
            byte[] buff1 = getBytesFromFile(file);
            Log.d("confidencekitna","1");
            //file2 = new File(sdCard.getAbsolutePath() + "/HashContact"+"/Pictures/"+"11.jpg");

            // Create a new file object for the second file and get bytes from file
            //File file2 = new File("C:\\Users\\ihene\\Desktop\\esan-caleb.jpg");
            byte[] buff2 = getBytesFromFile(file2);
            Log.d("confidencekitna","2");
            // Data needed to use the Face++ Compare API
            String url1 = "https://api-us.faceplusplus.com/facepp/v3/compare";
            HashMap<String, String> map = new HashMap<>();
            HashMap<String, byte[]> byteMap = new HashMap<>();
            map.put("api_key", "zADIkrvwyv23nZ2UX8_wVFZc2cHRfM-L");
            map.put("api_secret", "G1GVBBJLSsyoo_c5ZvxcZ1U7HIXlRigH");

            byteMap.put("image_file1", buff1);
            byteMap.put("image_file2", buff2);

            try {
                // Connecting and retrieving the JSON results
                byte[] bacd = post(url1, map, byteMap);
                Log.d("confidencekitna","4");
                String jsonStr = new String(bacd);
                Log.d("confidencekitna","5");
                // Parse the JSON and get the confidence value
                JSONObject obj = new JSONObject(jsonStr);
                Log.d("confidencekitna","6");
                confidence = obj.getDouble("confidence");
                System.out.println("kitnaconf" +confidence);
                //Toast.makeText(MainActivity.this,"Match:"+confidence,Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.d("confidencekitna", String.valueOf(e));
                e.printStackTrace();
            }
        }



        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.d("confidencekitna"," "+confidence);
        if(confidence>0.0&&confidence<70)
            Toast.makeText(context,"Match:"+confidence+"%",Toast.LENGTH_SHORT).show();
        else if(confidence<75)
            Toast.makeText(context,"Probable close relative found",Toast.LENGTH_SHORT).show();
        else if(confidence>=75)
            Toast.makeText(context,"You are found",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Timeout",Toast.LENGTH_SHORT).show();
        //            //this method will be running on UI thread

        pdLoading.dismiss();
    }

}
