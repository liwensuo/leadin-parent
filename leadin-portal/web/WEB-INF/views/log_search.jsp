<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>leadin日志平台</title>
      <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
      <!-- 新 Bootstrap 核心 CSS 文件 -->
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap.min.css">
      <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap-theme.min.css">

      <link rel="stylesheet" href="/js/jquery/jquery-ui/css/jquery-ui.min.css">
      <link rel="stylesheet" href="/js/jquery/jquery-ui/css/jquery-ui-timepicker-addon.css">
      <link rel="stylesheet" href="/js/jquery/DataTables/css/jquery.dataTables.min.css">
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
          $("#logSearch").ajaxForm({
              type: 'post',
              url: '/log/toSearch.do',
              data: {"key":$("input[name='key']").val(),"tablename":$("input[name='tablename']").val()},
              dataType: 'html',
              beforeSubmit: function (formArray, jqForm) {
                  var start= $.trim($("#logSearch input[name='startTime']").val());
                  var end=$.trim($("#logSearch input[name='endTime']").val());
                  var startTime; var endTime;
                  var dateReg= /^((((1[6-9]|[2-9]\d)\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\d|3[01]))|(((1[6-9]|[2-9]\d)\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\d|30))|(((1[6-9]|[2-9]\d)\d{2})-0?2-(0?[1-9]|1\d|2[0-8]))|(((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))(\s(([01]\d{1})|(2[0123])):([0-5]\d))?$/;
                  if(start.length>0 && !dateReg.test(start)){
                      $("#errMsg").html("格式错误!正确格式：2015-01-01 00:00");
                      $("#logSearch input[name='startTime']").focus();
                      return false;
                  }
                  if(end.length>0 && !dateReg.test(end)){
                      $("#errMsg").html("格式错误!正确格式：2015-01-01 00:00");
                      $("#logSearch input[name='endTime']").focus();
                      return false;
                  }
                  if(start.length>0 &&end.length>0 && dateReg.test(start) &&dateReg.test(end)&& !isNaN( Date.parse(start)) && !isNaN( Date.parse(end))){
                      if(Date.parse(start)>Date.parse(end)){
                          $("#errMsg").html("开始时间不能大于结束时间!");
                          return false;
                      }
                  }
                  if($("#logSearch select option").length==0){
                      $("#errMsg").html("数据库中无数据，请检查!");
                      return false;
                  }
                  $("#errMsg").html("");
                  return true;
              },
              success: function(html) {
                  $('#logListDIV').html(html);
                  $('#logListDIV').slideDown("slow");
              }
          });
          $("#logSearch input[name='startTime']").datetimepicker();
          $("#logSearch input[name='endTime']").datetimepicker();
      });

  </script>
  </head>
  <body>
  <div class="panel panel-default">
      <div class="panel-body" >
          <form id="logSearch" class="navbar-form navbar-collapse" action="/log/search.do" method="post">
              <div style="text-align: center;">
                  <div id="errMsg" style="color: red;font-size: 10pt;text-align: center;width: 100%; height: 12px;margin-bottom: 2px;margin-top: -5px;"></div>
                  <input  name="key" type="text" class="form-control" style="width:400px;"  placeholder="请输入检索的内容...">
                  <input class="form-control " style="width:125px;padding: 2px 2px;" name="startTime" type="text" placeholder="起始日期">
                  到
                  <input class="form-control " style="width:125px;padding: 2px 2px;" name="endTime" type="text" placeholder="结束日期">
                  <select name="tablename" title="选择要查询的服务" class="form-control"style="padding: 0 0;" >
                    <c:forEach var="name" items="${names}" varStatus="status">
                      <option value="${name.key}">${name.value}</option>
                    </c:forEach>
                  </select>
                  <button type="search"  class="btn" >查询</button>
              </div>
          </form>
          <div class="table_div" id="logListDIV" style="display: none;overflow: hidden;"></div>
      </div>
  </div>
  </body>
</html>
