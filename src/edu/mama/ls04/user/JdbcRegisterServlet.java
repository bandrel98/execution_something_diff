package edu.mama.ls04.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * 通过JDBC实现用户注册
 */
@WebServlet(urlPatterns = "/jdbc/register")
public class JdbcRegisterServlet extends HttpServlet {

    //Jackson
    private ObjectMapper mapper ;

    //JDBC
    private String jdbcURL = "jdbc:mysql://localhost:3306/JAVA_WEB";
    private String jdbcUser = "root";
    private String jdbcPassword = "root";
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";

    /**
     * 初始化Jackson
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
        １.接收请求参数：从POST请求中接收参数
         */
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        /*
        ２.做出响应处理：将数据保存到数据库
         */
        //数据库保存成功与否
        Boolean result = Boolean.FALSE;

        //加载数据库驱动
        try {
            Class.forName(jdbcDriver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //建立数据库连接，代码段运行结束后自动关闭连接
        try(Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword)) {

            String insert = "INSERT INTO USER (MOBILE, PASSWORD, DATE) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, mobile);
                stmt.setString(2, password);
                stmt.setDate(3, new Date(System.currentTimeMillis()));

                stmt.executeUpdate();
            }

            //保存成功
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
