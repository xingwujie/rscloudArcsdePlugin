package com.yanglonglong.function;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yanglonglong.config;
import com.yanglonglong.myclass.Fold;

public class GetfolderFromOracle {
    public static void main(String[] args) {
        HashMap<String, String> dataBasePara = config.getOraclePara();

        System.out.println(fromFoldertoJson(dataBasePara));
    }

    public static ArrayList getFolder(HashMap dataBasePara) {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        ArrayList<HashMap<String, String>> resultArray = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            System.out.println("开始尝试连接数据库！");
            String url = "jdbc:oracle:thin:@" + dataBasePara.get("ip") + ":" + dataBasePara.get("port") + ":ORCL";// 127.0.0.1是本机地址，ORCL为数据库的SID，不是服务名
            String user = dataBasePara.get("user").toString();// 用户名,系统默认的账户名
            String password = dataBasePara.get("passwd").toString();// 你安装时选设置的密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
            System.out.println("连接成功！");
            String sql = "select * from TB_FOLDER t";// 预编译语句
            pre = con.prepareStatement(sql);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next()) {  // 当结果集不为空时
                HashMap<String, String> onresult = new HashMap<>();
                onresult.put("text", result.getString("NAME"));
                onresult.put("uid", result.getString("UUID"));
                onresult.put("pid", result.getString("PARENTID"));
                onresult.put("type", result.getString("TYPE"));
                onresult.put("level", "0");
                if(result.getString("USERID").contains("rscloud")){
                    resultArray.add(onresult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return setLevel(resultArray);
    }

    // 设置每个文件夹的深度
    public static ArrayList setLevel(ArrayList<HashMap<String, String>> folderString) {
        for (int i = 0; i < folderString.size(); i++) {// 子目录
            for (int k = 0; k < folderString.size(); k++) { // 父目录
                if (folderString.get(i).get("pid").equals(folderString.get(k).get("uid"))) {
                    int level = Integer.parseInt(folderString.get(k).get("level")) + 1;
                    folderString.get(i).put("level", String.valueOf(level));
                }
            }
        }
        return folderString;
    }


    public static String fromFoldertoJson(HashMap dataBasePara) {
        ArrayList<HashMap<String, String>> folderString = getFolder(dataBasePara);
        ArrayList<Fold> folds = new ArrayList();
        int i;
        // 实例化所有磁盘
        for (i = 0; i < folderString.size(); i++) {
            if (folderString.get(i).get("level").equals("0")) {
                folds.add(getFoldClass(folderString.get(i)));
            }
        }

        // 实例化第一层文件
        for (i = 0; i < folderString.size(); i++) {
            if (folderString.get(i).get("level").equals("1")) {
                Fold fold = getFoldClass(folderString.get(i));
                folds = addFold(folds, fold);
            }
        }
        // 实例化第二层文件
        for (i = 0; i < folderString.size(); i++) {
            if (folderString.get(i).get("level").equals("2")) {
                Fold fold = getFoldClass(folderString.get(i));
                folds = addTwoFold(folds, fold);
            }
        }
        // 实例化第三层文件
        for (i = 0; i < folderString.size(); i++) {
            if (folderString.get(i).get("level").equals("3")) {
                Fold fold = getFoldClass(folderString.get(i));
                folds = addThreeFold(folds, fold);
            }
        }
        // 实例化第四层文件
        for (i = 0; i < folderString.size(); i++) {
            if (folderString.get(i).get("level").equals("4")) {
                Fold fold = getFoldClass(folderString.get(i));
                folds = addFourFold(folds, fold);
            }
        }

        return new Gson().toJson(folds);

    }

    // 实例化文件
    public static Fold getFoldClass(HashMap<String, String> folderone) {
        Fold fold = new Fold();
        fold.setText(folderone.get("text"));
        fold.setPid(folderone.get("pid"));
        fold.setUid(folderone.get("uid"));
        fold.setType(folderone.get("type"));
        fold.setLevel(folderone.get("level"));
        fold.setChildren(new ArrayList());
        return fold;
    }

    // 添加第一层文件
    public static ArrayList<Fold> addFold(ArrayList<Fold> flodArray, Fold folderone) {
        for (int k = 0; k < flodArray.size(); k++) {
            if (folderone.getPid().equals(flodArray.get(k).getUid())) {
                flodArray.get(k).getChildren().add(folderone);
                return flodArray;
            }
        }
        return flodArray;
    }

    // 添加第二层文件
    public static ArrayList<Fold> addTwoFold(ArrayList<Fold> flodArray, Fold folderone) {
        for (int k = 0; k < flodArray.size(); k++) {
            if (flodArray.get(k).getChildren().size() > 0) {
                addFold(flodArray.get(k).getChildren(), folderone);
                return flodArray;
            }
        }
        return flodArray;
    }


    // 添加第三层文件
    public static ArrayList<Fold> addThreeFold(ArrayList<Fold> flodArray, Fold folderone) {
        for (int k = 0; k < flodArray.size(); k++) {
            if (flodArray.get(k).getChildren().size() > 0) {
                addTwoFold(flodArray.get(k).getChildren(), folderone);
                return flodArray;
            }
        }
        return flodArray;
    }

    // 添加第死层文件
    public static ArrayList<Fold> addFourFold(ArrayList<Fold> flodArray, Fold folderone) {
        for (int k = 0; k < flodArray.size(); k++) {
            if (flodArray.get(k).getChildren().size() > 0) {
                addThreeFold(flodArray.get(k).getChildren(), folderone);
                return flodArray;
            }
        }
        return flodArray;
    }

}