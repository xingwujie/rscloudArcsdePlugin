package com.yanglonglong.function;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.HashMap;
import com.yanglonglong.config;


public class GetImageFromGeoserver {
    public static void main(String[] args) throws Exception {

        HashMap<String,String> addLayerPara =  config.getAddLayerPara();
//        System.out.println(getImageRange(addLayerPara, "SDE.FLOOR3")); // get image range
//        GetImageFromGeoserver.saveImage(addLayerPara,getImageRange(addLayerPara, "SDE.LUWANG"),"SDE.LUWANG");
    }

    // 得到图片范围
    public static String getImageRange(HashMap addLayerPara,String fileName) throws Exception{
        String workSpaceName = addLayerPara.get("workspace").toString();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String lnglat = "";
        try {
            // curl -u
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope((String) addLayerPara.get("ip"), Integer.parseInt(addLayerPara.get("port").toString())),
                    new UsernamePasswordCredentials((String) addLayerPara.get("user"), (String) addLayerPara.get("passwd")));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            System.out.println("http://localhost:18080/geoserver/rest/workspaces/abcd/datastores/fromSDE/featuretypes/SDE.LUWANG.html");
            HttpGet httpget = new HttpGet("http://localhost:"+addLayerPara.get("port").toString()+"/geoserver/rest/workspaces/"+addLayerPara.get("workspace").toString()+"/datastores/"+addLayerPara.get("datastory").toString()+"/featuretypes/"+fileName+".html");
            // curl -H
            httpget.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");

            // curl -v
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
            String pageHTML = EntityUtils.toString(responseEntity);
            Document doc = Jsoup.parse(pageHTML);
            Element li = doc.select("ul>li:last-child").get(0);
            String liText = li.text(); // 得到经纬度范围 http://localhost:18080/geoserver/rest/workspaces/abcd/datastores/fromSDE/featuretypes/SDE.LUWANG.html
            String lng = liText.substring(liText.indexOf("[") + 1, liText.indexOf(","));
            String lat = liText.substring(liText.indexOf(",") + 1, liText.indexOf("]"));
            lnglat = lng.split(":")[0].trim()+","+lat.split(":")[0].trim()+","+lng.split(":")[1].trim()+","+lat.split(":")[1].trim();
//            System.out.println(lnglat);
            if (responseEntity != null) {
                System.out.println("Response content: " + response.toString());
            }
            EntityUtils.consume(responseEntity);
        } finally {
            httpclient.getConnectionManager().shutdown();
//            System.out.println(lnglat);
            return lnglat;
        }
    }

    public static byte[] saveImage(HashMap addLayerPara,String imageRange,String fileName) throws Exception{
        //new一个URL对象
//        System.out.println(imageRange);
        String http = "http://localhost:"+addLayerPara.get("port")+"/geoserver/"+addLayerPara.get("workspace")+"/wms?service=WMS&version=1.1.0&request=GetMap&layers="+addLayerPara.get("workspace")+":"+fileName+"&styles=&bbox="+imageRange+"&width=255&height=255&srs=EPSG:4326&format=image%2Fjpeg";
        byte[] buffer = null;
        try {
            URL uploadUlr = new URL(http);
            HttpURLConnection connection = (HttpURLConnection)uploadUlr.openConnection();
            connection.setConnectTimeout(10*1000);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is =  connection.getInputStream();
            int resCode = connection.getResponseCode();
            if (resCode==200) {
                buffer = IOUtils.toByteArray(is);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer;
    }

}