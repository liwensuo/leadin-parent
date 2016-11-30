<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>错误页面</title>
      <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
  </head>
  <body>
  <!--404 404 404-->
  <div id="content">
      <div style="margin-left: 30%;;margin-top: 200px;">
          <ul>
              <h2><span id="rest" style="color:#FF0000">哎呀！数据库连接失败，请检查！</span></h2>
              <h2><a href="/log/index.do"><img src="/images/error/fs.png"></a></h2>
          </ul>
      </div>
  </div>
  </body>
</html>
