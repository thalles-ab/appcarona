package br.uvv.carona.httprequest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import br.uvv.carona.R;
import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.exception.AuthenticationException;
import br.uvv.carona.exception.CommonException;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.ImageUtils;
import br.uvv.carona.model.Student;

public class BaseHttpRequest{
    private static final String TAG = "BaseHttpRequest";
    private static final Gson gson = AppPartiUVV.sGson;

    public static String createRequest(HttpMethodUtil method, String url, Object object) throws Exception {
        return getRequest(method,url, AppPartiUVV.getToken(), object);
    }

    private static String getRequest(HttpMethodUtil method, String url, String authorization, Object object) throws Exception{
        if (!hasInternetConnection()) {
            throw new ConnectException(AppPartiUVV.mApplication.getString(R.string.error_not_connect_networking));
        }
        String response = "";
        String params = "";
        final String boundary = Long.toString(System.currentTimeMillis());
        final String twoHyphens = "--";
        final String end = "\r\n";
        int maxBufferSize = 1*1024*1024;
        HttpURLConnection connection = null;
        try {
            URL urlink = new URL(url);
            connection = (HttpURLConnection)urlink.openConnection();
            if(object instanceof Student && ((Student) object).file != null && ((Student) object).file.exists()){
                connection.addRequestProperty("Content-Type", "multipart/form-data;boundary="+ boundary);
            }else {
                connection.addRequestProperty("Content-Type",
                        "application/json");
            }

            if(!TextUtils.isEmpty(authorization)){
                connection.addRequestProperty("Authorization", authorization);
            }
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(0);

            boolean doInput = true;
            boolean doOutput;
            if(method == HttpMethodUtil.GET){
                connection.setRequestMethod("GET");
                params = url + convertMapParams(gson.fromJson(gson.toJson(object), Map.class));
                doOutput = false;
            }else if(method == HttpMethodUtil.POST){
                connection.setRequestMethod("POST");
                params = gson.toJson(object);
                doOutput = true;
            }else if(method == HttpMethodUtil.PUT){
                connection.setRequestMethod("PUT");
                params = gson.toJson(object);
                doOutput = true;
            }else{
                connection.setRequestMethod("DELETE");
                params = url + convertMapParams(gson.fromJson(gson.toJson(object), Map.class));
                doOutput = false;
            }

            connection.setDoInput(doInput);
            connection.setDoOutput(doOutput);

            if(doOutput){
                if(object instanceof Student && ((Student) object).file != null && ((Student) object).file.exists()){
                    Student student = (Student) object;
                    File file = student.file;
                    DataOutputStream dataOS = new DataOutputStream(connection.getOutputStream());
                    dataOS.writeBytes(twoHyphens + boundary + end);
                    dataOS.writeBytes("Content-Disposition: form-data; name=\"fileUpload\"; filename=\"" + file.getName() + "\"" + end);
                    dataOS.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + end);
                    dataOS.writeBytes("Content-Transfer-Encoding: binary" + end);
                    dataOS.writeBytes(end);

                    byte[] compressedImage = ImageUtils.compressImage(file.getPath());
                    InputStream inputStream = new ByteArrayInputStream(compressedImage);
                    int bytesAvailable = inputStream.available();
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[] buffer = new byte[bufferSize];
                    int bytesRead = inputStream.read(buffer, 0, bufferSize);
                    while(bytesRead > 0) {
                        dataOS.write(buffer, 0, bufferSize);
                        bytesAvailable = inputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = inputStream.read(buffer, 0, bufferSize);
                    }
                    dataOS.writeBytes(end);
                    dataOS.writeBytes(twoHyphens + boundary + end);
                    dataOS.writeBytes("Content-Disposition: form-data; name=\"id\"" + end);
                    dataOS.writeBytes("Content-Type: text/plain" + end);
                    dataOS.writeBytes(end);
                    dataOS.writeBytes(String.valueOf(student.id));
                    dataOS.writeBytes(end);
                    dataOS.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    inputStream.close();
                    dataOS.flush();
                    dataOS.close();
                }else {
                    connection.setFixedLengthStreamingMode(
                            params.getBytes().length);
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.print(params);
                    out.close();
                }
            }

            connection.connect();
            int code = connection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                switch (code) {
                    case HttpURLConnection.HTTP_UNAUTHORIZED:
                        Log.i(TAG, ": Login ou senha incorretos");
                        throw new CommonException(AppPartiUVV.mApplication.getString(R.string.error_unauthorized));
                    case HttpURLConnection.HTTP_BAD_REQUEST:
                        Log.e(TAG, "SC_BAD_REQUEST");
                        break;
                    case HttpURLConnection.HTTP_FORBIDDEN:
                        Log.e(TAG, "HttpResponse Token inválido :");
                        throw new AuthenticationException();
                    default:
                        throw new CommonException(AppPartiUVV.mApplication.getString(R.string.error_ws_generic));
                }
            }

            if(code == HttpURLConnection.HTTP_OK) {
                if (doInput) {
                    InputStream is = connection.getInputStream();
                    response = readIt(is);
                    is.close();
                }
            }else{
                InputStream is = connection.getErrorStream();
                response = readIt(is);
                is.close();
            }
        } catch (Exception e) {
            throw new CommonException(AppPartiUVV.mApplication.getString(R.string.error_ws_generic));
        } finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return response.equals("") ? null : response;
    }


    private static String readIt(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public static boolean hasInternetConnection(){
        ConnectivityManager cManager = (ConnectivityManager) AppPartiUVV.mApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    protected static String convertMapParams(Map<String, Object> params) {
        if (params == null)
            return "";

        StringBuilder str = new StringBuilder("?");
        for (Map.Entry<String, Object> item : params.entrySet()) {
            str.append(item.getKey()).append("=").append(item.getValue() instanceof Double && item.getValue() != null ?
                    ((Double) item.getValue()).intValue() : item.getValue()).append("&");
        }
        return str.toString();
    }
}