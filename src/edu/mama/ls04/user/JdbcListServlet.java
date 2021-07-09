package edu.mama.ls04.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.mama.ls04.bean.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC实现用户信息查询
 */
@WebServlet(urlPatterns = "/jdbc/list")
public class JdbcListServlet extends HttpServlet {

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
//
//    /**
//     * 从数据库中查询用户信息
//     * @param request
//     * @param response
//     * @throws IOException
//     */
//    @Override
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        List<User> list = new ArrayList<>();
//
//        //加载数据库驱动
//        try {
//            Class.forName(jdbcDriver);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //建立数据库连接，代码段运行结束后自动关闭连接
//        try(Connection conn = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword)) {
//            //预编译SQL
//            String select = "SELECT * FROM USER";
//            try (PreparedStatement stmt = conn.prepareStatement(select)) {
//
//                //执行SQL
//                ResultSet rs = stmt.executeQuery();
//                while (rs.next()) {
//
//                    User user = new User(
//                            rs.getString("ID"),
//                            rs.getString("MOBILE"),
//                            rs.getDate("DATE")
//                    );
//
//                    list.add(user);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//
//        /*
//        ３.输出响应结果
//         */
//        //设置响应数据的格式
//        response.setContentType("text/json");
//        //响应结果为JSON
//        mapper.writeValue(response.getWriter(), list);
//    }


    /**
     * 转发
     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        //转发请求
//        request.getRequestDispatcher("/user").forward(request, response);
//    }


    /**
     * 重定向
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得请求上下文
        String context = request.getServletContext().getContextPath();

        //重定向
        response.sendRedirect(context + "/user");
    }
}
