<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>角色管理</title>
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
      searchRole();
    }

    function searchRole() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_name: $('input[name=name]').val(),
        filter_LIKES_description: $('input[name=description]').val()
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/role/list',
        pagination: true,
        fit: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        frozenColumns: [[
          {
            title: '角色名称',
            field: 'name',
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '角色描述',
            field: 'description',
            sortable: true,
            width: 250
          }
        ]],
        toolbar: [
          {
            id: 'btnAdd',
            text: '新建角色',
            iconCls: 'icon-add',
            handler: function() {
              newRole();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改角色',
            iconCls: 'icon-edit',
            handler: function () {
              editRole();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除角色',
            iconCls: 'icon-remove',
            handler: function() {
              deleteRole();
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
      initAuthorityComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/role/doModify',
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

    function initAuthorityComboTree() {
      $('#authorityIds').combotree({
        url: '${ctx}/security/authority/allTree',
        lines: true,
        multiple: true,
        cascadeCheck: true,
        onChange: function(newValue, oldValue) {
          comboTreeFilterAllCondition();
        },
        onShowPanel: function() {
          comboTreeFilterAllCondition();
        },
        onHidePanel: function() {
          comboTreeFilterAllCondition();
        }
      });
    }

    function comboTreeFilterAllCondition() {
      var nodes = $('#authorityIds').combotree('tree').tree('getChecked');
      for (var i = 0; i < nodes.length; i ++) {
        var node = nodes[i];
        if (!node.id) {
          $('#authorityIds ~ .combo .textbox-text').val('全选');
          break;
        }
      }
    }

    function newRole() {
      $('#authorityIds').combotree('reload');
      $('#dlg').dialog('open').dialog('setTitle', '新建角色');
      $('#modifyForm').form('clear');
    }

    function editRole() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $('#authorityIds').combotree('reload');
        $('#dlg').dialog('open').dialog('setTitle', '编辑角色');
        if (row.authorities) {
          var authorityIds = new Array();
          for (var i = 0; i < row.authorities.length; i ++) {
            var authority = row.authorities[i];
            authorityIds.push(authority.id);
          }
          row.authorityIds = authorityIds;
        }
        $('#modifyForm').form('clear');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的角色!', 'info');
      }
    }

    function deleteRole() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除角色', function(ret) {
          if (ret) {
            var url = '${ctx}/security/role/doDelete';
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
        $.messager.alert('提示', '请先选择待删除的角色!', 'info');
      }
    }

    function saveRole() {
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
          角色名称: <input name="name" style="width: 200px;" />
        </td>
        <td>
          角色描述: <input name="description" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchRole();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'角色管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>

<div id="dlg" class="easyui-dialog" data-options="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">角色信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>角色名称：</label>
      <input name="name" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>角色描述：</label>
      <input name="description" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>权限列表：</label>
      <select id="authorityIds" name="authorityIds" style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#authorityIds').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRole()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
