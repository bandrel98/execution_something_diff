package edu.mama.ls04.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
 * 将 JdbcListServlet 和 JdbcRegisterServlet 的功能合并到一个类中
 */
@WebServlet(urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

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
        config.addDataSourceProperty("maximumPoolSize", "10");          //最大连接数：10
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
     * 从数据库中查询用户信息
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数
         */

        /*
        ２.处理请求
         */
        List<User> list = new ArrayList<>();

        //建立数据库连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {
            //预编译SQL
            String select = "SELECT * FROM USER";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {

                //执行SQL
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    //将查询结果集转为List
                    User user = new User(
                            rs.getString("ID"),
                            rs.getString("MOBILE"),
                            rs.getDate("DATE")
                    );

                    list.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /*
        ３.输出响应结果
         */
        //设置响应数据的格式
        response.setContentType("text/json");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), list);
    }

    /**
     * 用户注册
     * @param request
     * @param response
     * @throws IOException
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
        //数据库操作成功与否
        Boolean result = Boolean.FALSE;

        //建立连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {

            String insert = "INSERT INTO USER (MOBILE, PASSWORD, DATE) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, mobile);
                stmt.setString(2, password);
                stmt.setDate(3, new Date(System.currentTimeMillis()));

                stmt.executeUpdate();
            }

            //数据库操作成功
            result = Boolean.TRUE;

        } catch (SQLException e) {
            e.printStackTrace();
        }

         /*
        ３.输出响应结果
         */
        //设置响应数据的格式
        response.setContentType("text/json");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), result);
    }


    /**
     * 修改用户信息
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数
         */
        String id = request.getParameter("id");
        String mobile = request.getParameter("mobile");

        /*
        ２.处理请求
         */
        //数据库操作成功与否
        Boolean result = Boolean.FALSE;

        //建立连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {

            String insert = "UPDATE USER SET MOBILE = ? WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, mobile);
                stmt.setString(2, id);

                stmt.executeUpdate();
            }

            //数据库操作成功
            result = Boolean.TRUE;

        } catch (SQLException e) {
            e.printStackTrace();
        }

         /*
        ３.输出响应结果
         */
        //设置响应数据的格式
        response.setContentType("text/json");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), result);
    }


    /**
     * 删除用户
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数
         */
        String id = request.getParameter("id");

        /*
        ２.处理请求
         */
        //数据库操作成功与否
        Boolean result = Boolean.FALSE;

        //建立连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {

            String insert = "DELETE FROM USER WHERE ID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, id);

                stmt.executeUpdate();
            }

            //数据库操作成功
            result = Boolean.TRUE;

        } catch (SQLException e) {
            e.printStackTrace();
        }

         /*
        ３.输出响应结果
         */
        //设置响应数据的格式
        response.setContentType("text/json");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), result);
    }
}
