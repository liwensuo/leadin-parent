<%@ page pageEncoding="UTF-8" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>leadin日志平台</title>
  <link rel="shortcut icon" type="image/ico" href="/images/favicon.ico" />
  <!-- 新 Bootstrap 核心 CSS 文件 -->
  <link rel="stylesheet" href="/js/bootstrap-3.3.2/css/bootstrap.min.css">
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
  <!-- ECharts单文件引入 -->
  <script type="text/javascript"  src="/js/echart/echarts-all.js"></script>

  </head>
  <body style="margin-top: 20px;">
          <form id="logStat"  class="navbar-form">
              <div style="text-align: center;">
                  <div id="errMsg" style="color: red;font-size: 10pt;text-align: center;width: 100%; height: 12px;margin-bottom: 5px;margin-top: -5px;"></div>
                  <input class="form-control" name="startTime" type="text" placeholder="起始日期">
                  到
                  <input class="form-control" name="endTime" type="text" placeholder="结束日期">
                  <button type="search"  class="btn" >查询</button>
              </div>
          </form>
  <div id="statisDIV">
        <jsp:include page="statis.jsp"></jsp:include>
  </div>
  </body>
</html>
<script type="text/javascript">
    $(document).ready( function () {
        $("#logStat input[name='startTime']").datepicker();
        $("#logStat input[name='endTime']").datepicker();
        // 绑定表单提交事件处理器
        $('#logStat').submit(function () {
            // 提交表单
            $(this).ajaxSubmit({
                type: 'post',
                url: '/stat/statis.do',
                dataType: 'html',
                beforeSubmit: function (formArray, jqForm) {
                    var start=$("#logStat input[name='startTime']").val();
                    var end=$("#logStat input[name='endTime']").val();
                    var startTime = Date.parse(start);
                    var endTime = Date.parse(end);
                    if (isNaN(startTime)) {
                        $("#errMsg").html("请输入开始时间!");
                        $("#logStat input[name='startTime']").focus();
                        return false;
                    }
                    if( isNaN(endTime)){
                        $("#errMsg").html("请输入结束时间!");
                        $("#logStat input[name='endTime']").focus();
                        return false;
                    }
                    if(!/^(\d{4})-(\d{2})-(\d{2})$/.test(start)||!/^(\d{4})-(\d{2})-(\d{2})$/.test(end)){
                        $("#errMsg").html("格式错误!正确格式：2015-01-01");
                        return false;
                    }
                    if (startTime > endTime) {
                        $("#errMsg").html("开始时间不能大于结束时间!");
                        return false;
                    }
                },
                success: function (html) {
                    $('#statisDIV').html(html);
                    $("#errMsg").html("");
                },
                error: function () {
                    $("#errMsg").html("系统错误!请稍后重试!");
                }
            });
            // 为了防止普通浏览器进行表单提交和产生页面导航（防止页面刷新？）返回false
            return false;
        });
    });
</script>