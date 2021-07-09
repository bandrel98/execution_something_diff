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
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/json/list")
public class JsonListServlet extends HttpServlet {

    //Jackson
    private ObjectMapper mapper ;

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

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        User user1 = new User("1", "13600000001", new Date());
        User user2 = new User("2", "13600000002", new Date());
        User user3 = new User("3", "13600000003", new Date());

        List<User> list = List.of(user1, user2, user3);

        //设置响应数据的格式
        response.setContentType("text/json");

        mapper.writeValue(response.getWriter(), list);
    }
}
