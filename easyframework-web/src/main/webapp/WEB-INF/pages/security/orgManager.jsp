<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>部门管理</title>
  <%@ include file="/common/js.jsp" %>

  <link rel="stylesheet" type="text/css" href="${ctx}/statics/css/common.css" />

  <script type="text/javascript">
    var editingOrgId = null;

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
      searchOrg();
    }

    function searchOrg() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_name: $('input[name=name]').val(),
        filter_LIKES_description: $('input[name=description]').val()
      });
    }

    function initDataGrid() {
      var dataGrid = $('#dataGrid');

      dataGrid.datagrid({
        url: '${ctx}/security/org/list',
        pagination: true,
        fit: true,
        singleSelect: true,
        rownumbers: true,
        idField: 'id',
        frozenColumns: [[
          {
            title: '部门名称',
            field: 'name',
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '部门描述',
            field: 'description',
            sortable: true,
            width: 250
          },
          {
            title: '上级部门名称',
            field: 'parentOrg',
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
            text: '新建部门',
            iconCls: 'icon-add',
            handler: function() {
              newOrg();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改部门',
            iconCls: 'icon-edit',
            handler: function () {
              editOrg();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除部门',
            iconCls: 'icon-remove',
            handler: function() {
              deleteOrg();
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
      initParentOrgComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/org/doModify',
        onSubmit: function() {
          var isValid = $(this).form('validate');
          return isValid;
        },
        successHandler: function(result) {
          if (result.success) {
            $('#dlg').dialog('close');
            $('#dataGrid').datagrid('reload');
            $('#parentOrgId').combotree('reload');
          } else {
            $.messager.show({
              title: '错误',
              msg: result.msg
            });
          }
        }
      });
    }

    function initParentOrgComboTree() {
      $('#parentOrgId').combotree({
        url: '${ctx}/security/org/allTree',
        lines: true,
        onBeforeSelect: function(node) {
          if (editingOrgId) {
            if (parentOrSelfTree(editingOrgId, node)) {
              $.messager.show({
                title: '错误',
                msg: '请勿选择本身或者子菜单作为父菜单'
              });

              return false;
            } else {
              return true;
            }
          } else {
            return true;
          }
        }
      });
    }

    function parentOrSelfTree(menuId, selectedNode) {
      var ret = (menuId == selectedNode.id);

      if (!ret) {
        var menuTreeObj = $('#parentOrgId').combotree('tree');
        var parentNode = menuTreeObj.tree('getParent', selectedNode.target);
        while (parentNode) {
          if (parentNode.id == menuId) {
            ret = true;
            break;
          }

          parentNode = menuTreeObj.tree('getParent', parentNode.target);
        }
      }

      return ret;
    }

    function newOrg() {
      editingOrgId = null;
      $('#dlg').dialog('open').dialog('setTitle', '新建部门');
      $('#modifyForm').form('clear');
    }

    function editOrg() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        editingOrgId = row.id;
        $('#dlg').dialog('open').dialog('setTitle', '编辑部门');
        if (row.parentOrg) {
          row.parentOrgId = row.parentOrg.id;
        }
        $('#modifyForm').form('clear');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的部门!', 'info');
      }
    }

    function deleteOrg() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除部门', function(ret) {
          if (ret) {
            var url = '${ctx}/security/org/doDelete';
            var data = {id : row.id};
            $.post(url, data, function(result) {
              if (result.success) {
                $('#dataGrid').datagrid('reload');
                $('#parentOrgId').combotree('reload');
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
        $.messager.alert('提示', '请先选择待删除的部门!', 'info');
      }
    }

    function saveOrg() {
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
          部门名称: <input name="name" style="width: 200px;" />
        </td>
        <td>
          部门描述: <input name="description" style="width: 200px;" />
        </td>
        <td align="center">
          <a href="javascript:void(0)" onclick="clearForm();" class="easyui-linkbutton" iconCls="icon-clear">清空</a>
          <a href="javascript:void(0)" onclick="searchOrg();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
        </td>
      </tr>
    </table>
  </form>
</div>
<div data-options="region:'center', border:false, title:'部门管理'" style="overflow-y: auto;">
  <table id="dataGrid" data-options="fit:true, border:false"></table>
</div>

<div id="dlg" class="easyui-dialog" data-options="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">部门信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>部门名称：</label>
      <input name="name" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>部门描述：</label>
      <input name="description" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>上级部门：</label>
      <select id="parentOrgId" name="parentOrgId" style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#parentOrgId').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveOrg()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
