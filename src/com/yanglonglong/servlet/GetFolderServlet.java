package com.yanglonglong.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.yanglonglong.config;
import com.yanglonglong.function.GetfolderFromOracle;

@WebServlet(name = "GetFolderServlet")
public class GetFolderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=GB2312");
        HashMap<String,String> dataBasePara = config.getOraclePara();

        System.out.println("获取目录成功");
        PrintWriter out = null;
        out = response.getWriter();// 获得输出流out
//        out.println("中国人");
        out.println(GetfolderFromOracle.fromFoldertoJson(dataBasePara));
        out.flush(); // 冲刷缓冲区
        out.close(); // 关闭输出
    }
}
