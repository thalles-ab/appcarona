package br.uvv.carona.httprequest;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import br.uvv.carona.application.AppPartiUVV;
import br.uvv.carona.httprequest.util.HttpMethodUtil;

public class BaseHttpRequest {

    public static String createRequest(HttpMethodUtil method, String url, String authorization, Object object) throws Exception{
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
                connection.setConnectTimeout(25000);
                connection.setReadTimeout(25000);
            }else {
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
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

    public static String readIt(InputStream stream) throws IOException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        int data = reader.read();
        String string = "";
        while (data != -1) {
            char current = (char) data;
            data = reader.read();
            string = string + current;
        }
        return string;
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