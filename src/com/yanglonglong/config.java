package com.yanglonglong;


import java.util.HashMap;

public class config {
    public static HashMap getDataStoryPara(){ // ArcSDE的参数
        HashMap<String,String> dataStoryPara = new HashMap<String,String>();
        dataStoryPara.put("name","fromSDE");
        dataStoryPara.put("ip","192.168.2.142");
        dataStoryPara.put("port","5151");
        dataStoryPara.put("instance","arcse");
        dataStoryPara.put("user","sde");
        dataStoryPara.put("passwd","yll");
        dataStoryPara.put("dbtype","arcsde");

        return dataStoryPara;
    }

    public static HashMap getAddLayerPara(){ // 本机Geoserver参数

        HashMap<String,String> addLayerPara =  new HashMap<String,String>();
        addLayerPara.put("name","SDE.ZERENQU");// 默认图层
        addLayerPara.put("ip","localhost");
        addLayerPara.put("port","18080");
        addLayerPara.put("user","admin");
        addLayerPara.put("passwd","geoserver");
        addLayerPara.put("workspace","abcd");
        addLayerPara.put("datastory","fromSDE");

        return addLayerPara;
    }

    public static HashMap getOraclePara(){  // 遥感云oracle数据库的参数
        HashMap<String, String> dataBasePara = new HashMap<String, String>();
        dataBasePara.put("ip", "192.168.2.113");
        dataBasePara.put("port", "1521");
        dataBasePara.put("user", "rscloud");
        dataBasePara.put("passwd", "yll");

        return dataBasePara;
    }


}
