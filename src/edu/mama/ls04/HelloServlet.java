package edu.mama.ls04;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 使用web.xml配置Servlet
 *
 * 用户请求时，根据请求URL找到映射的对应的Servlet，再根据请求方法（GET/POST/PUT/DELETE等）匹配对应的实例方法
 */
public class HelloServlet extends HttpServlet {

    /*
    - init()：Servlet初始化时调用，仅被调用一次，常用来做一些初始化的操作，如：创建线程池等。
    - destroy()：Servlet销毁时调用，仅被调用一次，常用来做一些资源回收的操作，如：关闭线程池等。
    - service()：可以处理各种请求方法的 HTTP 请求，每次请求调用一次。HttpServlet.service()默认会根据请求 METHOD，调用doXxx方法。

    - doGet()：仅处理请求方法为 GET 的 HTTP 请求，每次请求调用一次
    - doPost()：仅处理请求方法为 POST 的 HTTP 请求，每次请求调用一次
    - doPut()：仅处理请求方法为 PUT 的 HTTP 请求，每次请求调用一次
    - doDelete()：仅处理请求方法为 DELETE 的 HTTP 请求，每次请求调用一次
     */

    /**
     * 仅处理GET请求
     * @param request Http请求，可通过该对象获得请求数据
     * @param response Http响应，响应数据需要通过该对象发出
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //响应内容有中文，需要设置字符集
        response.setCharacterEncoding("UTF-8");

        //响应输出流
        PrintWriter out = response.getWriter();

        //响应内容
        out.println("<html>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<body>");
        out.println("<h1>Hello, Servlet</h1>");
        out.println("<h1>你好，Servlet</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}