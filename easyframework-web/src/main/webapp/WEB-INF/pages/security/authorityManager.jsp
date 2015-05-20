<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>权限管理</title>
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
      searchAuthority();
    }

    function searchAuthority() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_name: $('input[name=name]').val(),
        filter_LIKES_description: $('input[name=description]').val()
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/authority/list',
        pagination: true,
        fit: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        frozenColumns: [[
          {
            title: '权限名称',
            field: 'name',
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '权限描述',
            field: 'description',
            sortable: true,
            width: 250
          }
        ]],
        toolbar: [
          {
            id: 'btnAdd',
            text: '新建权限',
            iconCls: 'icon-add',
            handler: function() {
              newAuthority();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改权限',
            iconCls: 'icon-edit',
            handler: function () {
              editAuthority();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除权限',
            iconCls: 'icon-remove',
            handler: function() {
              deleteAuthority();
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
      initResourceComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/authority/doModify',
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
            });
          }
        }
      });
    }

    function initResourceComboTree() {
      $('#resourceIds').combotree({
        url: '${ctx}/security/resource/allTree',
        lines: true,
        cascadeCheck: true,
        onCheck: function(node, checked) {
          var nodes = $('#resourceIds').combotree('tree').tree('getChecked');
          for (var i = 0; i < nodes.length; i ++) {
            var node = nodes[i];

            if (!node.id) {
              $('#resourceIds ~ .combo .textbox-text').val('全选');
              break;
            }
          }
        }
      });
    }

    function newAuthority() {
      $('#resourceIds').combotree('reload');
      $('#dlg').dialog('open').dialog('setTitle', '新建权限');
      $('#modifyForm').form('clear');
    }

    function editAuthority() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $('#resourceIds').combotree('reload');
        $('#dlg').dialog('open').dialog('setTitle', '编辑权限');
//        if (row.resources) {
//          row.parentMenuId = row.parentMenu.id;
//        }
        $('#modifyForm').form('clear');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的权限!', 'info');
      }
    }

    function deleteAuthority() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除权限', function(ret) {
          if (ret) {
            var url = '${ctx}/security/authority/doDelete';
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
        $.messager.alert('提示', '请先选择待删除的权限!', 'info');
      }
    }

    function saveAuthority() {
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
          权限名称: <input name="name" style="width: 200px;" />
        </td>
        <td>
          权限描述: <input name="description" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchAuthority();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'权限管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>

<div id="dlg" class="easyui-dialog" data-options="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">权限信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>权限名称：</label>
      <input name="name" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>权限描述：</label>
      <input name="description" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>资源列表：</label>
      <select id="resourceIds" name="resourceIds" multiple style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#resourceIds').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveAuthority()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
