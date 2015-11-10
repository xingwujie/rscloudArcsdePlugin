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

    //��ʼ��
    public void init() throws ServletException {
        System.out.println("����init()�������������г�ʼ������");
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
        out = response.getWriter();// ��������out
        out.println(imageURL);
        out.flush(); // ��ˢ������
        out.close(); // �ر����
    }

    //����ʵ��
    public void destroy() {
        super.destroy();
        System.out.println("����destroy()������������������ʵ���Ĺ���");
    }
}
