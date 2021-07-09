package edu.mama.ls04.homework;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过JDBC实现汽车信息的登记入库和查询
 */
@WebServlet(urlPatterns = {"/car/list", "/car/save"})
public class CarAnswerServlet extends HttpServlet {

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
     * 从数据库中查询车辆信息
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数：从GET请求中接收参数，本例不需要处理
         */

        /*
        ２.做出响应处理：从数据库中查询
         */
        List<Car> list = new ArrayList<>();

        //建立数据库连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {
            //预编译SQL
            String select = "SELECT * FROM CAR";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {

                //执行SQL
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    Car car = new Car(
                            rs.getString("BRAND"),
                            rs.getString("MODEL"),
                            rs.getInt("SEAT"),
                            rs.getDate("DATE"),
                            rs.getBigDecimal("PRICE")
                    );

                    list.add(car);
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
     * 保存页面提交的车辆信息到数据库
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        １.接收请求参数：从POST请求中接收参数
         */
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        int seat = Integer.parseInt(request.getParameter("seat"));
        BigDecimal price = new BigDecimal(request.getParameter("price"));

        /*
        ２.做出响应处理：将数据保存到数据库
         */
        //数据库保存成功与否
        Boolean result = Boolean.FALSE;
        //建立连接，代码段运行结束后自动关闭连接
        try(Connection conn = dataSource.getConnection()) {

            String insert = "INSERT INTO CAR (BRAND, MODEL, SEAT, DATE, PRICE) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, brand);
                stmt.setString(2, model);
                stmt.setInt(3, seat);
                stmt.setDate(4, new Date(System.currentTimeMillis()));
                stmt.setBigDecimal(5, price);

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
