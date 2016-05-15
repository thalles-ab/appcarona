package br.uvv.carona.httprequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.util.HttpMethodUtil;
import br.uvv.carona.httprequest.util.ImageUtils;
import br.uvv.carona.model.Student;

public class BaseHttpRequest{

    public static String createRequestWithAuthorization(HttpMethodUtil method, String url, String authorization, Object object) throws Exception{
        return getRequest(method,url, authorization, object);
    }


    public static String createRequest(HttpMethodUtil method, String url, Object object) throws Exception{
        return getRequest(method,url, null, object);
    }

    private static String getRequest(HttpMethodUtil method, String url, String authorization, Object object) throws Exception{
        String response = "";
        String params = "";
        final String boundary = Long.toString(System.currentTimeMillis());
        final String twoHyphens = "--";
        final String end = "\r\n";
        int maxBufferSize = 1*1024*1024;
        HttpURLConnection connection = null;
        try {
            if (method == HttpMethodUtil.GET && object != null) {
                url += (String) object;
            }
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
            if(method == HttpMethodUtil.POST || method == HttpMethodUtil.PUT){
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(2500);
            }else {
                connection.setConnectTimeout(2500);
                connection.setReadTimeout(1500);
            }

            boolean doInput = true;
            boolean doOutput;
            if(method == HttpMethodUtil.GET){
                connection.setRequestMethod("GET");
                doOutput = false;
            }else if(method == HttpMethodUtil.POST){
                connection.setRequestMethod("POST");
                doOutput = true;
            }else if(method == HttpMethodUtil.PUT){
                connection.setRequestMethod("PUT");
                doOutput = true;
            }else{
                connection.setRequestMethod("DELETE");
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
                    params = AppPartiUVV.sGson.toJson(object);
                    connection.setFixedLengthStreamingMode(
                            params.getBytes().length);
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.print(params);
                    out.close();
                }
            }

            connection.connect();
            int code = connection.getResponseCode();
            if(code == HttpURLConnection.HTTP_OK) {
                if (doInput) {
                    InputStream is = connection.getInputStream();
                    response = readIt(is);
                    is.close();
                }
            }else{
                InputStream is = connection.getErrorStream();
                String errorMessage = readIt(is);
                is.close();
                connection.disconnect();
                //TODO tratar erros
                throw new Exception(errorMessage);
            }
        } catch (MalformedURLException e) {
            Log.e("ERROR SERVICE", (e.getMessage() == null) ? "Null Message" : e.getMessage() );
            throw e;
        } catch (ProtocolException e) {
            Log.e("ERROR SERVICE", (e.getMessage() == null) ? "Null Message" : e.getMessage() );
            throw e;
        } catch (IOException e) {
            Log.e("ERROR SERVICE", (e.getMessage() == null) ? "Null Message" : e.getMessage() );
            throw e;
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
}