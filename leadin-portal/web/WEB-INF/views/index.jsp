<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>日志平台</title>
      <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
      <!-- 新 Bootstrap 核心 CSS 文件 -->
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap.min.css">
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap-theme.min.css">

      <link rel="stylesheet" href="/js/jquery/jquery-ui/css/jquery-ui.min.css">
      <link rel="stylesheet" href="/js/jquery/jquery-ui/css/jquery-ui-timepicker-addon.css">
      <link rel="stylesheet" href="/js/jquery/DataTables/css/jquery.dataTables.min.css">
  <style>
      body {
          padding-top: 20px;
      }
      .table_div{
          overflow: auto;text-justify: auto;border: groove;margin-top: 20px;
      }
  </style>
  <!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
  <script type="text/javascript" src="/js/jquery.min.js"></script>
  <script type="text/javascript" src="/js/jquery/jquery-form/jquery.form.min.js"></script>

  <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
  <script type="text/javascript" src="/js/bootstrap-3.3.2/js/bootstrap.min.js"></script>
      <script type="text/javascript" src="/js/jquery/jquery-ui/js/jquery-ui.min.js"></script>
      <script type="text/javascript"  src="/js/jquery/jquery-ui/js/jquery-ui.datepicker.zh-CN.js"></script>
      <script type="text/javascript" src="/js/jquery/jquery-ui/js/jquery-ui-timepicker-addon.js"></script>
      <script type="text/javascript" src="/js/jquery/jquery-ui/js/jquery-ui-timepicker-zh-CN.js"></script>
      <script type="text/javascript">
      $(document).ready( function () {
          $("#logStatDIV").load("/stat/currentStatis.do");
          $("#clientMonitor").load("/log/clientMonitor2.do");
          $("ul li a").click(function(){
              var obj=$(this);
              $("#logSearchIndex").siblings().hide();
              $("#logSearchIndex").empty();
              $("body").css("overflow","hidden");
              obj.attr("disabled",true);
             /* $("#logSearchIndex").show().load($(this).attr("url"),{},function(){
                  obj.removeAttr("disabled");
                  $("#logStat input[name='startTime']").datetimepicker();
                  $("#logStat input[name='endTime']").datetimepicker();
              });*/
              $("#logSearchIndex").show().attr("src",$(this).attr("url"));
          });
      });

  </script>
  </head>
  <body>
  <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
          <div class="navbar-header">
              <a class="navbar-brand" href="/log/index.do">leadin日志平台</a>
          </div>
          <div class="navbar-collapse collapse" id="navbar">
              <ul class="nav navbar-nav navbar-right" style="margin-right: 30%;">
                  <li><a tid="logSearchIndex" url="/baseLog/search.do" href="javascript:void(0);">日志查询</a></li>
                  <li><a tid="showStat" url="/stat/showStat.do" href="javascript:void(0);">图表统计</a></li>
              </ul>
          </div>
      </div>
  </nav>

  <div class="panel panel-default">
      <div class="panel-body" >
          <iframe id="logSearchIndex" style="width: 100%;height: 90%;display: none;overflow: hidden;" frameborder=no ></iframe>
<%--
          //<div id="logSearchIndex" style="display: none;"></div>
--%>
          <div class="table_div" id="logStatDIV"></div>
          <div class="table_div" id="clientMonitor"  style="overflow: hidden;"></div>
      </div>
  </div>
  <nav class="navbar-inverse navbar-fixed-bottom" style="height:25px;text-align: center;">
              <a  href="/log/index.do">leadin日志平台</a>
  </nav>
  </body>
</html>
