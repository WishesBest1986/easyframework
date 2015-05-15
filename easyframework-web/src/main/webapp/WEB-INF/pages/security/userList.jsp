<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>用户管理</title>
  <%@ include file="/common/js.jsp" %>

  <script type="text/javascript">
    $(function() {
      initSearchForm();
      initDataGrid();
    });

    function initSearchForm() {
      $('#queryForm').form();
    }

    function clearForm() {
      $('#queryForm').form('clear');
      searchUser();
    }

    function searchUser() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_username: $('input[name=filter_LIKES_username]').val(),
        filter_LIKES_fullname: $('input[name=filter_LIKES_fullname]').val()
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/user/list',
        pagination: true,
        fit: true,
        rownumbers: true,
        idField: 'id',
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
        onBeforeLoad: function(param) {
          // redefine pagination parameter names.
          if (param.page) {
            param.pageNo = param.page;
          }
          if (param.rows) {
            param.pageSize = param.rows;
          }
          if (param.sort) {
            param.orderBy = param.sort;
          }

          return true;
        }
      });
      dataGrid.datagrid('getPager').pagination({
        pageSize: 10,
        pageList: [10, 20, 50, 100]
      });
    }
  </script>

</head>
<body class="easyui-layout" data-options="fit:true, border:false">
<div data-options="region:'north', border:false" style="height: 30px;overflow: hidden;">
  <form id="queryForm" style="text-align: center;">
    <table width="100%">
      <tr>
        <td>
          登录名: <input name="filter_LIKES_username" style="width: 200px;" />
        </td>
        <td>
          姓名: <input name="filter_LIKES_fullname" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchUser();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'用户管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>
</body>
</html>
