<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>资源管理</title>
  <%@ include file="/common/js.jsp" %>

  <link rel="stylesheet" type="text/css" href="${ctx}/statics/css/common.css" />

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
      searchResource();
    }

    function searchResource() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_name: $('input[name=name]').val(),
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/resource/list',
        pagination: true,
        fit: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        frozenColumns: [[
          {
            title: '资源名称',
            field: 'name',
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '资源值',
            field: 'source',
            sortable: true,
            width: 400
          },
          {
            title: '所属菜单',
            field: 'menu',
            sortable: true,
            width: 200,
            formatter: function(value, row, index) {
              if (value) {
                return value.name;
              } else {
                return value;
              }
            }
          }
        ]],
        toolbar: [
          {
            id: 'btnAdd',
            text: '新建资源',
            iconCls: 'icon-add',
            handler: function() {
              newResource();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改资源',
            iconCls: 'icon-edit',
            handler: function () {
              editResource();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除资源',
            iconCls: 'icon-remove',
            handler: function() {
              deleteResource();
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
      initMenuComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/resource/doModify',
        onSubmit: function() {
          var isValid = $(this).form('validate');
          return isValid;
        },
        successHandler: function(result) {
          if (result.success) {
            $('#dlg').dialog('close');
            $('#dataGrid').datagrid('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: result.msg
            });
          }
        }
      });
    }

    function initMenuComboTree() {
      $('#menuId').combotree({
        url: '${ctx}/security/menu/allTree',
        lines: true
      });
    }

    function newResource() {
      $('#menuId').combotree('reload');
      $('#dlg').dialog('open').dialog('setTitle', '新建资源');
      $('#modifyForm').form('clear');
    }

    function editResource() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $('#menuId').combotree('reload');
        $('#dlg').dialog('open').dialog('setTitle', '编辑资源');
        if (row.menu) {
          row.menuId = row.menu.id;
        }
        $('#modifyForm').form('clear');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的资源!', 'info');
      }
    }

    function deleteResource() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除资源', function(ret) {
          if (ret) {
            var url = '${ctx}/security/resource/doDelete';
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
        $.messager.alert('提示', '请先选择待删除的资源!', 'info');
      }
    }

    function saveResource() {
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
          资源名称: <input name="name" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchResource();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'资源管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>

<div id="dlg" class="easyui-dialog" data-options="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">资源信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>资源名称：</label>
      <input name="name" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>资源值：</label>
      <input name="source" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>所属菜单：</label>
      <select id="menuId" name="menuId" style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#menuId').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveResource()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
