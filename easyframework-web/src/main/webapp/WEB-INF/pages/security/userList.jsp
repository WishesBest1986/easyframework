<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>用户管理</title>
  <%@ include file="/common/js.jsp" %>

  <script type="text/javascript">
    $(function() {
      initDataGrid();
    });

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/user/list',
        frozenColumns: [[
          {
            title: '登录名',
            field: 'username',
            sortable: true
          }
        ]],
        columns: [[
          {
            title: '姓名',
            field: 'fullname',
            sortable: true
          },
          {
            title: '是否可用',
            field: 'enabled',
            sortable: true
          }
        ]],
        pagination: true
      });
    }
  </script>

</head>
<body class="easyui-layout" data-options="fit:true, border:false">
<div data-options="region:'north', border:false" style="height: 30px;overflow: hidden;background-color: #F4F4F4;">

</div>
<div data-options="region:'center', border:false, title:'用户管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>
</body>
</html>
