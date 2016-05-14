package br.uvv.carona.httprequest;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.util.HttpMethodUtil;

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
        HttpURLConnection connection = null;
        try {
            URL urlink = new URL(url);
            connection = (HttpURLConnection)urlink.openConnection();
            connection.addRequestProperty("Content-Type",
                    "application/json");
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
                params = AppPartiUVV.sGson.toJson(object);
                connection.setFixedLengthStreamingMode(
                        params.getBytes().length);
                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(params);
                out.close();
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