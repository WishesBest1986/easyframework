<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>菜单管理</title>
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
        filter_LIKES_name: $('input[name=name]').val(),
        filter_LIKES_description: $('input[name=description]').val()
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/menu/list',
        pagination: true,
        fit: true,
        rownumbers: true,
        idField: 'id',
        frozenColumns: [[
          {
            title: '菜单名称',
            field: 'name',
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '菜单描述',
            field: 'description',
            sortable: true,
            width: 250
          },
          {
            title: '上级菜单名称',
            field: 'parentMenu.name',
            sortable: true,
            width: 200
          },
          {
            title: '排序号',
            field: 'sortnum',
            width: 150
          }
        ]],
        toolbar: [
          {
            id: 'btnAdd',
            text: '新建菜单',
            iconCls: 'icon-add',
            handler: function() {
              newMenu();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改菜单',
            iconCls: 'icon-edit',
            handler: function () {
              editMenu();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除菜单',
            iconCls: 'icon-remove',
            handler: function() {
              deleteMenu();
            }
          }
        ],
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

    function newMenu() {

    }

    function editMenu() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {

      } else {
        $.messager.alert('提示', '请先选择待编辑的菜单!', 'info');
      }
    }

    function deleteMenu() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除菜单', function(ret) {
          if (ret) {
            var url = '';
            var data = {};
            $.post(url, data, function(result) {
              if (result.success) {
                $('#dataGrid').datagrid('reload');
              } else {
                $.messager.show({
                  title: '错误',
                  msg: result.msg
                })
              }
            }, 'json');
          }
        });
      } else {
        $.messager.alert('提示', '请先选择待删除的菜单!', 'info');
      }
    }
  </script>

</head>
<body class="easyui-layout" data-options="fit:true, border:false">
<div data-options="region:'north', border:false" style="height: 30px;overflow: hidden;">
  <form id="queryForm" style="text-align: center;">
    <table width="100%">
      <tr>
        <td>
          菜单名称: <input name="name" style="width: 200px;" />
        </td>
        <td>
          菜单描述: <input name="description" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchUser();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'菜单管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>
</body>
</html>
