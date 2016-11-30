<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page session="false" %>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>leadin日志平台</title>
    <script type="text/javascript" src="/js/jquery.min.js"></script>
    <script src="/js/jquery/DataTables/js/jquery.dataTables.min.js"></script>
    <script  type="text/javascript">
        $('#clientMonitorTable').dataTable({
            "sAjaxSource" : "/log/clientMonitor.do?r=" + new Date().getTime(),
            //服务器端，数据回调处理
            "fnServerData" : function(sSource, aDataSet, fnCallback) {
                $.ajax({
                    "dataType": 'json',
                    "type": "post",
                    "url": sSource,
                    "data": aDataSet,
                    "success": function (resp) {
                        console.log(resp);
                        fnCallback(resp);
                    }
                });
            },
            "oLanguage": {//语言国际化
                "sUrl": "/js/jquery/DataTables/js/jquery.dataTable.cn.txt"
            },
            "aoColumns" : [{
                "mData" : "clientId",	//列标识，和服务器返回数据中的属性名称对应
                "sTitle" : "客户端IP",//列标题
                "sDefaultContent" : "", //此列默认值为""，以防数据中没有此值，DataTables加载数据的时候报错
                "bVisible" : true //此列不显示
                //"sClass" : "hidden"//定义列的class参数，隐藏列也可以通过这种方式设置
            }, {
                "mData" : "clientStartTime",
                "sTitle" : "开始时间",
                "sWidth":"12%",//定义列宽度，以百分比表示
                "sDefaultContent" : ""
            }, {
                "mData" : "receiveTime",
                "sTitle" : "上次活动时间",
                "sDefaultContent" : "",
                "sWidth":"12%",//定义列宽度，以百分比表示
                "bSortable":false	//此列不需要排序
            }, {
                "mData" : "serverLastTime",
                "sTitle" : "上次服务端时间",
                "sWidth":"12%",//定义列宽度，以百分比表示
                "bVisible" : false, //此列不显示
                "sDefaultContent" : "",
                "bSortable":false	//此列不需要排序
            },  {
                "mData" : "status",
                "sTitle" : "客户端状态",
                "sDefaultContent" : ""
            },  {
                "mData" : "exception",
                "sTitle" : "异常信息",
                "sClass" : "hidden",//定义列的class参数，隐藏列也可以通过这种方式设置
                "sDefaultContent" : ""
            }, {
                "mData" : "msg",
                "sTitle" : "异常信息2",
                "sClass" : "hidden",//定义列的class参数，隐藏列也可以通过这种方式设置
                "sDefaultContent" : ""
            },  {
                "mData" : "oper",
                "sTitle" : "操作",
                "sDefaultContent" : "<button>查看异常信息</button>"
            }],
            "scrollCollapse": true,
            "paging":         false,
            //"sDom": "t<'row-fluid'<'span6'i><'span6'p>>"//定义表格的显示方式
            //"sScrollX": "150%",//是否开启水平滚动，以及指定滚动区域大小,可设值：'disabled','2000%'
            'bPaginate': false,  //是否分页。
            "bProcessing": true, //当datatable获取数据时候是否显示正在处理提示信息。
            'bFilter': true,  //是否使用内置的过滤功能
            'bLengthChange': true, //是否允许自定义每页显示条数.
            "bInfo": true, //开关，是否显示表格的一些信息
            "bSort": true, //开关，是否启用各列具有按列排序的功能
            //"sPaginationType": "bootstrap",
            //"sDom": "<'row'<'span8'l><'span8'f>r>t<'row'<'span8'i><'span8'p>>",
            "sPaginationType": "full_numbers"//分页样式   full_numbers
        });
        $('#clientMonitorTable tbody').on( 'click', 'button', function () {
            alert($(this).closest('tr').children("td").eq(5).text()+$(this).closest('tr').children("td").eq(6).text());
        } );
    </script>
  </head>
  <body>
      <div style="text-align: center;">客户端状态</div>
      <table id="clientMonitorTable" class="dataTable table-bordered display" style="font-size: 10pt;width: 100%;">
      </table>
  </body>
</html>
