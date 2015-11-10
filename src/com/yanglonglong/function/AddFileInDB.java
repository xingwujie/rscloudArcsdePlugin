package com.yanglonglong.function;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.io.InputStream;

import com.yanglonglong.function.GetfolderFromOracle;
import com.yanglonglong.function.GetImageFromGeoserver;
import com.yanglonglong.config;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

public class AddFileInDB {
    public static void main(String[] args){
        HashMap<String,String> dataBasePara = config.getOraclePara();

        HashMap<String,String> filePara = new HashMap<String,String>(); // 数据库记录配置
        filePara.put("parentID","402882f150caf6300150caf630c40000");// 上层文件夹id
        filePara.put("fileName","SDE.LUWANG");// 文件名称
        addSHPFileToOracle(dataBasePara,filePara);
    }

    public static void addSHPFileToOracle(HashMap dataBasePara,HashMap filePara){
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            System.out.println("开始尝试连接数据库！");
            String url = "jdbc:oracle:thin:@" + dataBasePara.get("ip") + ":" + dataBasePara.get("port") + ":ORCL";// 127.0.0.1是本机地址，ORCL为数据库的SID，不是服务名
            String user = dataBasePara.get("user").toString();// 用户名,系统默认的账户名
            String password = dataBasePara.get("passwd").toString();// 你安装时选设置的密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
            System.out.println("连接成功！");

            SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
            String nowTime = df.format(new Date()); // 将当前系统时间作为虚拟文件的ID
            String uuID = "402882f150caf6300150cbc5b"+nowTime;
            String parentID = filePara.get("parentID").toString();
            String fileName = filePara.get("fileName").toString();
            byte[] imageByte = GetImageFromGeoserver.saveImage(config.getAddLayerPara(),GetImageFromGeoserver.getImageRange(config.getAddLayerPara(), fileName),fileName);
            String sql = "INSERT INTO TB_FILE(UUID,FILEINFOID,PARENTID,FILENAME,FILETYPE,FILESIZE,FILEDATE,USERID,SHARED,FULLFILENAME,VISUALPARAMETER,PREVIEWCONTENT,IMAGINGMODE,SPECTRALMODEL,ZISANIMAGEPATH,DATAID) VALUES ('"+uuID+"',2,'"+parentID+"','"+fileName+"','SHP',1024,to_date(replace('2015-11-12','-',''),'yyyymmdd'),'rscloud',0,'"+fileName+".SHP','',?,'','','','')";// 预编译语句
            pre = con.prepareStatement(sql);// 实例化预编译语句
            pre.setBytes(1,imageByte);
            pre.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源,最后使用的最先关闭
                if(pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
