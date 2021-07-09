<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>计算结果</title>
  </head>
  <body>
  <%
    String num1 = request.getParameter("num1");
    String num2 = request.getParameter("num2");

    int result = Integer.valueOf(num1) + Integer.valueOf(num2);
  %>
  <h1>求和结果：<%=result%></h1>
  </body>
</html>
