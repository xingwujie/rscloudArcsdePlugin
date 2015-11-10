package com.yanglonglong.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.yanglonglong.function.PublishLayers;
import com.yanglonglong.config;

@WebServlet(name = "GetAllLayersServlet")
public class GetAllLayersServlet extends HttpServlet {

    //初始化
    public void init() throws ServletException {
        System.out.println("我是init()方法！用来进行初始化工作");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json;charset=GB2312");
        HashMap<String,String> dataStoryPara = config.getDataStoryPara();

        String publishes = null;
        try {
            publishes = new Gson().toJson(PublishLayers.getAllLayers(dataStoryPara));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(publishes);
        PrintWriter out = null;
        out = response.getWriter();// 获得输出痒out
        out.println(publishes);
        out.flush(); // 冲刷缓冲区
        out.close(); // 关闭输出
    }

     //销毁实例
    public void destroy() {
        super.destroy();
        System.out.println("我是destroy()方法！用来销毁实例的工作");
    }
}
