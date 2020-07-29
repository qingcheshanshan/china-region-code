
import croe.RegionCode;
import entity.RegionTable;
import utils.TimeRecorder;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class main {
    //数据库url、用户名和密码
    static final String DB_URL = "jdbc:mysql://localhost:3306/qc?" +
            "zeroDateTimeBehavior=convertToNull" +
            "&serverTimezone=Asia/Shanghai&allowMultiQueries=true&characterEncoding=UTF-8" +
            "&autoReconnect=true&failOverReadOnly=false";
    static final String USER = "root";
    static final String PASS = "123456";

    public static void main(String[] args) {
        try {
            TimeRecorder timeRecorder = new TimeRecorder();
            timeRecorder.start();
            //1、注册JDBC驱动
            Class.forName("com.mysql.jdbc.Driver");
            //2、获取数据库连接
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            connection.setAutoCommit(true);
            //3、操作数据库
            Statement statement = connection.createStatement();//获取操作数据库的对象

            RegionCode regionCode = new RegionCode();
            List<RegionTable> preservation = regionCode.preservation();
            boolean r = true;
            for (RegionTable regionTable : preservation) {
                if (regionTable.getClassification() == null) {
                    regionTable.setClassification("0");
                }
                String sql = "INSERT INTO china_region_code(`code`, `name`, `type`, `classification`, `pcode`) VALUES (" +
                        "'" + regionTable.getCode() + "'" +
                        "," +
                        "'" + regionTable.getName() + "'" +
                        "," +
                        "'" + regionTable.getType() + "'" +
                        "," +
                        "'" + regionTable.getClassification() + "'" +
                        "," +
                        "'" + regionTable.getPcode() + "'" +
                        ");";
                int i = statement.executeUpdate(sql);//执行sql，获取结果集
                if (i != 1) {
                    r = false;
                }

            }
            if (r) {
                long duration = timeRecorder.getDuration();
                System.out.println(" 保存成功用时： " + duration);
            } else {
                System.out.println(" 保存失败 ");
            }

            //4、关闭结果集、数据库操作对象、数据库连接
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}