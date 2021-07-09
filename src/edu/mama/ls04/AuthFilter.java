package edu.mama.ls04;

import edu.mama.ls04.bean.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求过滤器
 * filter能够在一个请求到达servlet之前预处理用户请求，也可以在离开servlet之后处理http响应。
 * Filter常用来转换HTTP请求、响应和头信息。
 */
@WebFilter(urlPatterns = "/user/*")
public class AuthFilter extends HttpFilter {

    /*
    - init()：Filter初始化时调用，仅被调用一次，常用来做一些初始化的操作，如：创建线程池等。
    - destroy()：Filter销毁时调用，仅被调用一次，常用来做一些资源回收的操作，如：关闭线程池等。
    - doFilter()：每次HTTP请求调用一次。
     */

    /**
     * 每一次请求时都只调用方法doFilter()进行处理；
     * @param request
     * @param response
     * @param chain 过滤链。Web项目可以配置多个Filter，一个HTTP请求要经过所有匹配的过滤器后，才会交由匹配的Servlet去处理。
     */
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //判断是否登录
        Object obj = request.getSession().getAttribute("user");

        if (obj == null) {
            //未登录，拦截HTTP请求，直接给出响应
            String context = request.getServletContext().getContextPath();
            response.sendRedirect(context + "/login.html");
        } else {
            //已登录，交由下一个过滤器去处理
            chain.doFilter(request, response);
        }
    }
}
