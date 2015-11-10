package com.yanglonglong.servlet;

import com.google.gson.Gson;
import com.yanglonglong.function.PublishLayers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import com.yanglonglong.function.AddFileInDB;
import com.yanglonglong.config;

@WebServlet(name = "PublishLayerServlet")
public class AddFileInDBServlet extends HttpServlet {

    //初始化
    public void init() throws ServletException {
        System.out.println("我是init()方法！用来进行初始化工作");
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        response.setContentType("charset=UTF-8");
        HashMap<String,String> dataBasePara = config.getOraclePara();

        HashMap<String,String> filePara = new HashMap<String,String>(); // 数据库记录配置
        filePara.put("parentID",request.getParameter("fileid"));// 上层文件夹id
        filePara.put("fileName",request.getParameter("filename"));// 文件名称
        System.out.println(request.getParameter("filename"));
        try {
            AddFileInDB.addSHPFileToOracle(dataBasePara, filePara);
            System.out.println("添加文件到数据库成功");
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
