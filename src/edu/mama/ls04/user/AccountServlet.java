package edu.mama.ls04.user;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import edu.mama.ls04.bean.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 个人中心
 */
@WebServlet(urlPatterns = {"/user/account"})
public class AccountServlet extends HttpServlet {

    //Jackson
    private ObjectMapper mapper ;
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
     * 从数据库中查询用户信息
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //通过Session拿到当前登录用户。注意：此时getAttribute的key必须和登录时setAttribute的key相同
        User user = (User)request.getSession().getAttribute("user");

        if (user != null) {
            //用户已登录
            System.out.println(user.getMobile() + "发来的HTTP请求");
        }

        //设置响应数据的格式
        response.setContentType("text/json");
        //响应结果为JSON
        mapper.writeValue(response.getWriter(), user);
    }
}
