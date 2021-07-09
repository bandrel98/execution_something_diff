package edu.mama.ls04;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.mama.ls04.bean.JsonResult;
import edu.mama.ls04.bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录认证
 */
@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    //Jackson
    private ObjectMapper mapper ;

    //连接池（数据源）
    private DataSource dataSource;

    //JDBC
    private String jdbcURL = "jdbc:mysql://localhost:3306/JAVA_WEB";
    private String jdbcUser = "root";
    private String jdbcPassword = "root";
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

    /**
     * 初始化Jackson和HikariCP连接池
     */
    @Override
    public void init() {
        //初始化Jackson
        mapper = new ObjectMapper();
        //输出JSON时自动换行并缩进
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //把java.util.Date, Calendar输出为数字时间戳
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //在遇到未知属性的时候不抛出异常
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //将空字符串""转换为null
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        //初始化连接池
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcURL);
        config.setUsername(jdbcUser);
        config.setPassword(jdbcPassword);

        //连接池参数配置
        config.addDataSourceProperty("maximumPoolSize", "3");             //最大连接数：10
        config.addDataSourceProperty("idleTimeout", "3600000");           //空闲超时：60分

        try {
            //加载数据库驱动
            Class.forName(jdbcDriver);
            //根据连接池配置创建数据源
            dataSource = new HikariDataSource(config);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver class not found", e);
        }
    }

    /**
     * 用户登录
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数
         */
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        /*
        ２.处理请求
         */
        JsonResult result = null;

        //建立连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {

            String select = "SELECT * FROM USER WHERE MOBILE = ? AND PASSWORD = ?";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {
                stmt.setString(1, mobile);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    //账号密码匹配，登录成功
                    result = new JsonResult(true, "登录成功");

                    /*
                    会话
                     */
                    User user = new User(
                            rs.getString("ID"),
                            rs.getString("MOBILE"),
                            rs.getDate("DATE")
                    );
                    request.getSession().setAttribute("user", user);

                } else {
                    //账号密码不匹配，登录失败
                    result = new JsonResult(false, "账号或密码错误");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            //数据库操作抛出异常，登录失败
            result = new JsonResult(false, "出错了哦，请稍后重试");
        }

         /*
        ３.输出响应结果
         */
        //设置响应数据的格式
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), result);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //将Session无效
        request.getSession().invalidate();


        //设置响应数据的格式
        response.setContentType("text/json");
        response.setCharacterEncoding("UTF-8");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), true);
    }
}
