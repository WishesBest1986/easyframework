<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>

<html>
<head>
  <%@ include file="/common/meta.jsp" %>
  <title>后台管理系统</title>
  <%@ include file="/common/js.jsp" %>

  <style type="text/css">
    #editPwdForm {
      margin: 0;
      padding: 10px 30px;
    }
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

  <script type="text/javascript" src="${ctx}/statics/js/tab-control.js"></script>
  <script type="text/javascript">
    $(function() {
      initNaviTabBar();
      initEditPwdForm();
    });

    function initNaviTabBar() {
      var tabsObj = $('#tabs');
      tabsObj.tabs({
        fit: true,
        border: false,
        tools: [
          {
            iconCls: 'icon-tip',
            handler: function() {
              tabsObj.tabs('select', 0);
            }
          },
          {
            iconCls: 'icon-reload',
            handler: function() {
              var curTab = tabsObj.tabs('getSelected');
              tabsObj.tabs('update', {
                tab: curTab,
                options: curTab.panel('options')
              });
            }
          },
          {
            iconCls: 'icon-cancel',
            handler: function() {
              var curTab = tabsObj.tabs('getSelected');
              if (curTab.panel('options').closable) {
                var curTabIndex = tabsObj.tabs('getTabIndex', curTab);
                tabsObj.tabs('close', curTabIndex);
              }
            }
          }
        ]
      });

      $('.cs-navi-tab').click(function() {
        var self = $(this);
        var href = self.attr('src');
        var title = self.text();

        var params = {
          url: href,
          title: title
        };
        addTab(tabsObj, params);
      });
    }

    function initEditPwdForm() {
      $('#editPwdForm').form({
        url: '${ctx}/doModifyPwd',
        onSubmit: function() {
          var isValid = $(this).form('validate');
          return isValid;
        },
        successHandler: function(result) {
          if (result.success) {
            $('#editPwdDialog').dialog('close');
            $.messager.alert('提示', '密码修改成功，请重新登录!', 'info', function() {
              window.location.href = '${ctx}/doLogout';
            });
          } else {
            var msg = result.msg;
            if (!msg) {
              msg = '未知错误';
            }
            $.messager.alert('错误', msg, 'error');
          }
        }
      });
    }

    function editPwd() {
      $('#editPwdDialog').dialog('open').dialog('setTitle', '修改密码');
      $('#editPwdForm').form('clear');
    }

    function savePwd() {
      $('#editPwdForm').submit();
    }

    function logout() {
      $.messager.confirm('提示', '确定要退出?', function(ret) {
        if (ret) {
          window.location.href = '${ctx}/doLogout';
        }
      })
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
<body class="easyui-layout">

<!-- 页眉LOGO -->
<div data-options="region:'north'" style="height: 40px;padding: 2px;background: #E0ECFF;">
  <span style="float: right;padding-right: 20px;vertical-align: middle;margin-top: 10px;">
    欢迎<b>&nbsp;<shiro:principal /></b>!&nbsp;&nbsp;
    <a href="javascript:void(0)" onclick="editPwd();">修改密码</a> &nbsp;&nbsp;
    <a href="javascript:void(0)" onclick="logout();">退出</a>
  </span>
  <span style="float: left;padding-left: 20px;vertical-align: middle;margin-top: 5px;">
    <span style="font-size: 19px;font-weight: bold;font-family: 幼圆;">后台管理系统</span>
  </span>
</div>

<!-- 页脚版权 -->
<div data-options="region:'south'" style="height: 15px;line-height: 15px; overflow: hidden;text-align: center;background-color: #f0f0f0">
  Copyright &copy; 2015 Neusoft. All Rights Reserved
</div>

<!-- 右侧菜单 -->
<div data-options="region:'west', iconCls:'icon-reload', split:true" title="功能菜单" style="width: 15%;padding: 1px;overflow: auto;height: auto;">
  <div class="easyui-accordion" data-options="fit:true, border:false">
    <div title="系统管理" style="padding: 10px;overflow: auto;">
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/user" class="cs-navi-tab">用户管理</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/org" class="cs-navi-tab">部门管理</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/role" class="cs-navi-tab">角色管理</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/authority" class="cs-navi-tab">权限管理</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/resource" class="cs-navi-tab">资源管理</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/security/menu" class="cs-navi-tab">菜单管理</a></p>
    </div>
  </div>
</div>

<!-- 中部功能页面 -->
<div data-options="region:'center'">
  <div id="tabs" class="easyui-tabs">
    <div title="系统首页" data-options="iconCls:'icon-tip'" style="overflow:hidden;padding: 5px;">
      <div style="margin: 10px 0;">
        <h1>欢迎使用后台管理系统</h1><br />
      </div>
    </div>
  </div>
</div>

<!-- 修改密码弹出框 -->
<div id="editPwdDialog" data-options="region:'center', modal:true" class="easyui-dialog" closed="true" buttons="#dlg-buttons" title="修改密码" style="width: 350px;height: 210px;">
  <form id="editPwdForm" method="post" novalidate>
    <div class="ftitle">密码修改</div>
    <div class="fitem">
      <label>原密码:</label>
      <input name="oldPassword" type="password" class="easyui-validatebox" required="true" />
    </div>
    <div class="fitem">
      <label>新密码:</label>
      <input id="newPassword" name="newPassword" type="password" class="easyui-validatebox" required="true" />
    </div>
    <div class="fitem">
      <label>确认密码:</label>
      <input name="rePassword" type="password" class="easyui-validatebox" required="true" validType="equalTo['#newPassword']" />
    </div>
  </form>
</div>
<!-- 修改密码按钮 -->
<div id="dlg-buttons">
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="savePwd()" iconCls="icon-ok">确认</a>
  <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#editPwdDialog').dialog('close')" iconCls="icon-cancel">取消</a>
</div>

</body>
</html>
