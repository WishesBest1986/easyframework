<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>菜单管理</title>
  <%@ include file="/common/js.jsp" %>

  <style type="text/css">
    .ftitle {
      font-size: 14px;
      font-weight: bold;
      color: #666;
      padding: 5px 0;
      margin-bottom: 10px;
      border-bottom: 1px solid #ccc;
    }
    .fitem {
      margin-bottom: 5px;
    }
    .fitem label {
      display: inline-block;
      width: 80px;
    }
  </style>

  <script type="text/javascript">
    $(function() {
      initSearchForm();
      initDataGrid();
      initModifyForm();
    });

    function initSearchForm() {
      $('#queryForm').form();
    }

    function clearForm() {
      $('#queryForm').form('clear');
      searchMenu();
    }

    function searchMenu() {
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
        singleSelect: true,
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
            field: 'parentMenu',
            width: 200,
            formatter: function(value, row, index) {
              if (row.parentMenu) {
                return row.parentMenu.name;
              } else {
                return value;
              }
            }
          },
          {
            title: '排序号',
            field: 'orderNum',
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

    function initModifyForm() {
      initParentMenuComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/menu/doModify',
        onSubmit: function() {
          var isValid = $(this).form('validate');
          return isValid;
        },
        success: function(result) {
          result = $.parseJSON(result);

          if (result.success) {
            $('#dlg').dialog('close');
            $('#dataGrid').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: result.msg
            })
          }
        }
      });
    }

    function initParentMenuComboTree() {
      $('#parentMenuId').combotree({
        url: '${ctx}/security/menu/allTree',
        lines: true
      });
    }

    function newMenu() {
      $('#dlg').dialog('open').dialog('setTitle', '新建菜单');
      $('#modifyForm').form('clear');
    }

    function editMenu() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $('#dlg').dialog('open').dialog('setTitle', '编辑菜单');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的菜单!', 'info');
      }
    }

    function deleteMenu() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除菜单', function(ret) {
          if (ret) {
            var url = '${ctx}/security/menu/doDelete';
            var data = {id : row.id};
            $.post(url, data, function(result) {
              if (result.success) {
                $('#dlg').dialog('close');
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

    function saveMenu() {
      $('#modifyForm').submit();
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
          <a href="javascript:void(0)" onclick="searchMenu();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'菜单管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>

<div id="dlg" class="easyui-dialog" data-options="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">菜单信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>菜单名称：</label>
      <input name="name" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>菜单描述：</label>
      <input name="description" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>排序号：</label>
      <input name="orderNum" class="easyui-numberspinner" />
    </div>
    <div class="fitem">
      <label>上级菜单：</label>
      <select id="parentMenuId" name="parentMenuId"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#parentMenuId').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMenu()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
