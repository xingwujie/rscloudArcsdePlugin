package com.yanglonglong;


import java.util.HashMap;

public class config {
    public static HashMap getDataStoryPara(){ // ArcSDE�Ĳ���
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

    public static HashMap getAddLayerPara(){ // ����Geoserver����

        HashMap<String,String> addLayerPara =  new HashMap<String,String>();
        addLayerPara.put("name","SDE.ZERENQU");// Ĭ��ͼ��
        addLayerPara.put("ip","localhost");
        addLayerPara.put("port","18080");
        addLayerPara.put("user","admin");
        addLayerPara.put("passwd","geoserver");
        addLayerPara.put("workspace","abcd");
        addLayerPara.put("datastory","fromSDE");

        return addLayerPara;
    }

    public static HashMap getOraclePara(){  // ң����oracle���ݿ�Ĳ���
        HashMap<String, String> dataBasePara = new HashMap<String, String>();
        dataBasePara.put("ip", "192.168.2.113");
        dataBasePara.put("port", "1521");
        dataBasePara.put("user", "rscloud");
        dataBasePara.put("passwd", "yll");

        return dataBasePara;
    }


}
