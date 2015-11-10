package com.yanglonglong.servlet;

import com.google.gson.Gson;
import com.yanglonglong.config;
import com.yanglonglong.function.GetImageFromGeoserver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class GetLayerURLServlet extends HttpServlet {

    //初始化
    public void init() throws ServletException {
        System.out.println("我是init()方法！用来进行初始化工作");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("GB2312");
        response.setContentType("charset=GB2312");
        HashMap addLayerPara =  config.getAddLayerPara();
        String fileName = request.getParameter("layername");
        addLayerPara.put("name",fileName);
        String imageURL = "";
        try {
            String imageRange = GetImageFromGeoserver.getImageRange(addLayerPara,fileName);
            imageURL = "http://"+ addLayerPara.get("ip") + ":" + addLayerPara.get("port") + "/" +
                    "geoserver/" + addLayerPara.get("workspace") + "/wms?service=WMS&version=1.1.0&request=GetMap&layers=" +
                    addLayerPara.get("workspace") + ":" + fileName + "&styles=&bbox=" + imageRange +
                    "&width=768&height=604&srs=EPSG:4326&format=application/openlayers";

        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = null;
        out = response.getWriter();// 获得输出流out
        out.println(imageURL);
        out.flush(); // 冲刷缓冲区
        out.close(); // 关闭输出
    }

    //销毁实例
    public void destroy() {
        super.destroy();
        System.out.println("我是destroy()方法！用来进行销毁实例的工作");
    }
}
