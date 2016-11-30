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
  </head>
  <body>
  <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
          <div class="navbar-header">
              <a class="navbar-brand" href="/log/index.do">leadin日志平台</a>
          </div>
          <div class="navbar-collapse collapse" id="navbar">
              <ul class="nav navbar-nav navbar-right" style="margin-right: 30%;">
                  <li><a href="/stat/showStat.do">图表统计</a></li>
              </ul>
          </div>
      </div>
  </nav>
  <nav class="navbar navbar-inverse navbar-fixed-bottom">
  <div class="panel panel-default">
      <div class="panel-body" >
          <div class="center-block">
              403:${exception}
          </div>
      </div>
  </div>
      </nav>
  </body>
</html>
