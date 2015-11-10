package com.yanglonglong.servlet;

import com.google.gson.Gson;
import com.yanglonglong.config;
import com.yanglonglong.function.PublishLayers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet(name = "PublishLayerServlet")
public class PublishLayerServlet extends HttpServlet {

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
        HashMap<String,String> dataStoryPara = com.yanglonglong.config.getDataStoryPara();
        HashMap<String,String> addLayerPara =  config.getAddLayerPara();
        addLayerPara.put("name",request.getParameter("layername"));
        try {
            System.out.println("开始发布");
            PublishLayers.addlayer(dataStoryPara, addLayerPara);
            System.out.println("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter out = null;
        out = response.getWriter();// 获得输出流out
        out.println("ok");
        out.flush(); // 冲刷缓冲区
        out.close(); // 关闭输出
    }

    //销毁实例
    public void destroy() {
        super.destroy();
        System.out.println("我是destroy()方法！用来进行销毁实例的工作");
    }
}
