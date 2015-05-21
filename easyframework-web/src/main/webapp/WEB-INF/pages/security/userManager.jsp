<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>用户管理</title>
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
      searchUser();
    }

    function searchUser() {
      $('#dataGrid').datagrid('load', {
        filter_LIKES_username: $('input[name=username]').val(),
        filter_LIKES_fullname: $('input[name=fullname]').val()
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
            sortable: true,
            width: 200
          }
        ]],
        columns: [[
          {
            title: '姓名',
            field: 'fullname',
            sortable: true,
            width: 200
          },
          {
            title: '是否可用',
            field: 'enabled',
            sortable: true,
            width: 200
          }
        ]],
        toolbar: [
          {
            id: 'btnAdd',
            text: '新建用户',
            iconCls: 'icon-add',
            handler: function() {
              newUser();
            }
          },
          '-',
          {
            id: 'btnEdit',
            text: '修改用户',
            iconCls: 'icon-edit',
            handler: function () {
              editUser();
            }
          },
          '-',
          {
            id: 'btnDel',
            text: '删除用户',
            iconCls: 'icon-remove',
            handler: function() {
              deleteUser();
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
      initOrgComboTree();
      initRoleComboTree();

      $('#modifyForm').form({
        url: '${ctx}/security/user/doModify',
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

    function initOrgComboTree() {
      $('#orgId').combotree({
        url: '${ctx}/security/org/allTree',
        lines: true
      });
    }

    function initRoleComboTree() {
      $('#roleIds').combotree({
        url: '${ctx}/security/role/allTree',
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
        },
        onLoadSuccess: function(node, data) {
          comboTreeFilterAllCondition();
        }
      });
    }

    function comboTreeFilterAllCondition() {
      var nodes = $('#roleIds').combotree('tree').tree('getChecked');
      for (var i = 0; i < nodes.length; i ++) {
        var node = nodes[i];
        if (!node.id) {
          $('#roleIds').combotree('setText', '全选');
          break;
        }
      }
    }

    function newUser() {
      $('#password').textbox({
        required: true,
        onChange: function(newValue, oldValue) { }
      });
      $('#rePassword').textbox({
        required: true
      });

      $('#dlg').dialog('open').dialog('setTitle', '新建用户');
      $('#modifyForm').form('clear');
    }

    function editUser() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $('#password').textbox({
          required: false,
          onChange: function(newValue, oldValue) {
            if (newValue) {
              $('#rePassword').textbox({
                required: true
              });
            } else {
              $('#rePassword').textbox({
                required: false
              });
            }
          }
        });
        $('#rePassword').textbox({
          required: false
        });
        $('#dlg').dialog('open').dialog('setTitle', '编辑用户');
        row.password = '';

        $('#modifyForm').form('clear');
        $('#modifyForm').form('load', row);
      } else {
        $.messager.alert('提示', '请先选择待编辑的用户!', 'info');
      }
    }

    function deleteUser() {
      var row = $('#dataGrid').datagrid('getSelected');
      if (row) {
        $.messager.confirm('提示', '确认要删除用户', function(ret) {
          if (ret) {
            var url = '${ctx}/security/user/doDelete';
            var data = {id : row.id};
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
        $.messager.alert('提示', '请先选择待删除的用户!', 'info');
      }
    }

    function saveUser() {
      $('#modifyForm').submit();
    }

    <!-- 拓展easyUI的验证框架，验证两次输入值一致 -->
    $.extend($.fn.validatebox.defaults.rules, {
      equalTo: {
        validator: function(value, param) {
          return $(param[0]).val() == value;
        },
        message: '字段内容不配置'
      }
    });
  </script>

</head>
<body class="easyui-layout" data-options="fit:true, border:false">
<div data-options="region:'north', border:false" style="height: 30px;overflow: hidden;">
  <form id="queryForm" style="text-align: center;">
    <table width="100%">
      <tr>
        <td>
          登录名: <input name="username" style="width: 200px;" />
        </td>
        <td>
          姓名: <input name="fullname" style="width: 200px;" />
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

<div id="dlg" class="easyui-dialog" data-option="region:'center', modal:true" style="width: 400px;padding: 10px 20px;" closed="true" buttons="#dlg-buttons">
  <div class="ftitle">用户信息</div>
  <form id="modifyForm" method="post" novalidate>
    <input type="hidden" name="id" />
    <div class="fitem">
      <label>登录名：</label>
      <input name="username" class="easyui-textbox" required="true" />
    </div>
    <div class="fitem">
      <label>姓名：</label>
      <input name="fullname" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>密码：</label>
      <input id="password" name="password" type="password" class="easyui-textbox" />
    </div>
    <div class="fitem">
      <label>密码确认：</label>
      <input id="rePassword" name="rePassword" type="password" class="easyui-textbox" validType="equalTo['#password']" />
    </div>
    <div class="fitem">
      <label>所属部门：</label>
      <select id="orgId" name="orgId" style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#orgId').combotree('clear');">清空</a>
    </div>
    <div class="fitem">
      <label>角色列表：</label>
      <select id="roleIds" name="roleIds" style="width: 150px;"></select>
      <a class="easyui-linkbutton" href="javascript:void(0)" onclick="$('#roleIds').combotree('clear');">清空</a>
    </div>
  </form>
</div>
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUser()" style="width: 90px;">保存</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width: 90px;">取消</a>
</div>

</body>
</html>
