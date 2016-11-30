<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>错误页面</title>
      <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
      <!-- 新 Bootstrap 核心 CSS 文件 -->
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap.min.css">
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap-theme.min.css">

      <link rel="stylesheet" href="/js/jquery/jquery-ui/css/jquery-ui.min.css">
      <link rel="stylesheet" href="/js/jquery/DataTables/css/jquery.dataTables.min.css">
  <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
  <script type="text/javascript" src="/js/jquery.min.js"></script>
  <script type="text/javascript" src="/js/bootstrap-3.3.2/js/bootstrap.min.js"></script>
      <script type="text/javascript">
          var time=10;
          function count(){
              setTimeout("count()",1000);
              $("#rest").html(time);
              if(time == 0){
                  window.location.href = "/log/index.do";
                  return;
              }
              time--;
          }
      </script>
  </head>
  <body onLoad="window.setTimeout('count()',1)">
  <!--404 404 404-->
  <div id="content">
      <div style="margin-left: 30%;;margin-top: 200px;">
          <ul>
              <h1>哎呀！出错了！</h1>
              <h2><span id="rest" style="color:#FF0000"></span>秒之后启动传送门带亲返回，亲还可以</h2>
              <h2><a href="/log/index.do"><img src="/images/error/fs.png"></a></h2>
          </ul>
      </div>
  </div>
  </body>
</html>
