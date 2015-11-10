package com.yanglonglong.function;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeLayer;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.yanglonglong.config;

/*
  注意，arcsde数据存储出现问题的原因：
  （1） arcgis未破解
  （2） arcsde插件未安装
 */
public class PublishLayers {
    public static void main(String[] args) throws Exception {
        HashMap<String,String> dataStoryPara = config.getDataStoryPara();
        HashMap<String,String> addLayerPara =  config.getAddLayerPara();

        addlayer(dataStoryPara,addLayerPara); // publish layer
//        System.out.println(getAllLayers(dataStoryPara)); // get all layers in ArcSDE
//        System.out.println(getAlreadyPuclicService(addLayerPara)); // get already publish layers
//        System.out.println(unpublishlayers(dataStoryPara,addLayerPara)); // get unpublish layers
//        String publishes = new Gson().toJson(getAllLayers(dataStoryPara));
//        System.out.println(publishes);
    }

    public static void addlayer(HashMap dataStoryPara,HashMap addLayerPara) throws Exception{
        addworkspace(addLayerPara); // add work space
        connectionArcSDE(dataStoryPara,addLayerPara); // add datastory
        addLayer(addLayerPara); // add layer
    }

    public static void addworkspace(HashMap addLayerPara) throws Exception {
        String workSpaceName = addLayerPara.get("workspace").toString();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // curl -u
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope((String) addLayerPara.get("ip"), Integer.parseInt(addLayerPara.get("port").toString())),
                    new UsernamePasswordCredentials((String) addLayerPara.get("user"), (String) addLayerPara.get("passwd")));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpPost httpost = new HttpPost("http://localhost:"+addLayerPara.get("port").toString()+"/geoserver/rest/workspaces");
            // curl -H
            httpost.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");

            // curl -d
            String transData = "<workspace><name>" + workSpaceName + "</name></workspace>";
            httpost.setEntity(new StringEntity(transData));

            // curl -v
            System.out.println("executing request" + httpost.getRequestLine());
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("----------------------------------------");
            String pageHTML = EntityUtils.toString(responseEntity);
            System.out.println(pageHTML);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("Response content: " + response.toString());
            }
            EntityUtils.consume(responseEntity);
        } finally {

            httpclient.getConnectionManager().shutdown();
        }
    }
    public static void connectionArcSDE(HashMap dataStoryPara,HashMap addLayerPara)throws Exception{
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // curl -u
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope((String) addLayerPara.get("ip"), Integer.parseInt(addLayerPara.get("port").toString())),
                    new UsernamePasswordCredentials((String) addLayerPara.get("user"), (String) addLayerPara.get("passwd")));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            // abcd为工作区
            HttpPost httppost = new HttpPost("http://localhost:"+addLayerPara.get("port").toString()+"/geoserver/rest/workspaces/"+addLayerPara.get("workspace").toString()+"/datastores");
            // curl -H
            httppost.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");

            // curl -d
//                  String xml = "<?xml version="
//                    + "\"1.0\""
//                    + " encoding="
//                    + "\"UTF-8\""
//                    + "?><dataStore>" +
//                    "<name>fromSDE</name>" +
//                    "<connectionParameters>" +
//                    "<server>192.168.2.142</server>" +
//                    "<port>5151</port>" +
//                    "<instance>arcse</instance>" +
//                    "<user>sde</user>" +
//                    "<password>yll</password>" +
//                    "<dbtype>arcsde</dbtype>" +
//                    "</connectionParameters></dataStore>";

            String xml = "<?xml version="
                    + "\"1.0\""
                    + " encoding="
                    + "\"UTF-8\""
                    + "?><dataStore>" +
                    "<name>"+dataStoryPara.get("name")+"</name>" +
                    "<connectionParameters>" +
                    "<server>"+dataStoryPara.get("ip")+"</server>" +
                    "<port>"+dataStoryPara.get("port")+"</port>" +
                    "<instance>"+dataStoryPara.get("instance")+"</instance>" +
                    "<user>"+dataStoryPara.get("user")+"</user>" +
                    "<password>"+dataStoryPara.get("passwd")+"</password>" +
                    "<dbtype>"+dataStoryPara.get("dbtype")+"</dbtype>" +
                    "</connectionParameters></dataStore>";
            String transData = xml;
            httppost.setEntity(new StringEntity(transData));

            // curl -v
            System.out.println("executing request" + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("----------------------------------------");
            String pageHTML = EntityUtils.toString(responseEntity);
            System.out.println(pageHTML);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("Response content: " + response.toString());
            }
            EntityUtils.consume(responseEntity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    public static void addLayer(HashMap addLayerPara) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // curl -u
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope((String) addLayerPara.get("ip"), Integer.parseInt(addLayerPara.get("port").toString())),
                    new UsernamePasswordCredentials((String) addLayerPara.get("user"), (String) addLayerPara.get("passwd")));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            // abcd为工作区，fromSDE为数据存储的名称
            HttpPost httppost = new HttpPost("http://localhost:"+addLayerPara.get("port")+"/geoserver/rest/workspaces/"+addLayerPara.get("workspace")+"/datastores/"+addLayerPara.get("datastory")+"/featuretypes");
            // curl -H
            httppost.setHeader(HttpHeaders.CONTENT_TYPE, "text/xml");

            // curl -d
            String transData = "<featureType><name>"+addLayerPara.get("name")+"</name></featureType>";
            httppost.setEntity(new StringEntity(transData));

            // curl -v
            System.out.println("executing request" + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity responseEntity = response.getEntity();
            System.out.println("----------------------------------------");
            String pageHTML = EntityUtils.toString(responseEntity);
            System.out.println(pageHTML);
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("Response content: " + response.toString());
            }
            EntityUtils.consume(responseEntity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    /*获取所有图层*/
    public static ArrayList<String> getAllLayers(HashMap dataStoryPara) throws Exception {
        ArrayList<String> allLayers = new ArrayList<String>();
        //建立一个 ArcSDE 服务器连接
        SeConnection conn = null;
        String server = dataStoryPara.get("ip").toString();
        int instance = Integer.parseInt(dataStoryPara.get("port").toString());
        String database = dataStoryPara.get("instance").toString();
        String user = dataStoryPara.get("user").toString();
        String password = dataStoryPara.get("passwd").toString();
        try {
            conn = new SeConnection(server, instance, database, user, password);
        } catch (SeException e) {
            e.printStackTrace();
        }

        //取得存储与 ArcSDE 数据库内的的图层列表信息
        Vector layerList = conn.getLayers();
        for (int index = 0; index < layerList.size(); index++) {
            SeLayer layer = (SeLayer) layerList.elementAt(index);
            //图层名称
            allLayers.add(user.toUpperCase()+"."+layer.getName());
        }
//        System.out.println(allLayers);
        conn.close();
        return allLayers;
    }

    /*获取ArcSDE中已经发布的服务*/
    public static ArrayList<String> getAlreadyPuclicService(HashMap addLayerPara){
        ArrayList<String> alreadyLayers = new ArrayList<String>();
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            // curl -u
            httpclient.getCredentialsProvider().setCredentials(
                    new AuthScope((String) addLayerPara.get("ip"), Integer.parseInt(addLayerPara.get("port").toString())),
                    new UsernamePasswordCredentials((String) addLayerPara.get("user"), (String) addLayerPara.get("passwd")));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            //abcd为工作区名称，formSDE为数据存储的名称
            HttpGet httpget = new HttpGet("http://localhost:"+addLayerPara.get("port")+"/geoserver/rest/workspaces/"+addLayerPara.get("workspace")+"/datastores/"+addLayerPara.get("datastory")+".html");
            // curl -H
            httpget.setHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
            // curl -v
//            System.out.println("executing request" + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
//            System.out.println("----------------------------------------");
            String pageHTML = EntityUtils.toString(responseEntity);
            Document doc = Jsoup.parse(pageHTML);
            Elements lis = doc.select("ul>li");
            for(org.jsoup.nodes.Element li : lis){
                String liText = li.text();
                alreadyLayers.add(liText);
            }
            if (responseEntity != null) {
//                Sistem.out.println("Response content: " + response.toString());
            }
            EntityUtils.consume(responseEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            httpclient.getConnectionManager().shutdown();
            return alreadyLayers;
        }
    }

    public static ArrayList<String> unpublishlayers(HashMap lataStoryPara,HashMap addLayerPara) throws Exception{
        ArrayList<String> allLayers = getAllLayers(lataStoryPara);
        ArrayList<String> publishLayers = getAlreadyPuclicService(addLayerPara);
        ArrayList<String> unPublishLayers = new ArrayList<>();
        for(String tempLayers : allLayers){
            if(publishLayers.contains(tempLayers)){}
            else{
                unPublishLayers.add(tempLayers);
            }
        }
        return unPublishLayers;
    }

}