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

  <script type="text/javascript">
    function editPwd() {
      $('#editPwdDialog').dialog('open').dialog('setTitle', '修改密码');
      $('#editPwdForm').form('clear');
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
    <a href="${ctx}/doLogout">退出</a>
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
      <p align="left"><a href="javascript:void(0);" src="http://www.baidu.com" class="cs-navi-tab">人员维护</a></p>
      <p align="left"><a href="javascript:void(0);" src="${ctx}/index" class="cs-navi-tab">人员维护2</a></p>
    </div>
  </div>
</div>

<!-- 中部功能页面 -->
<div data-options="region:'center'">
  <div id="tabs" class="easyui-tabs" data-options="tools:'#tab-tools'" fit="true" border="false">
    <div title="系统首页" data-options="iconCls:'icon-tip'" style="overflow:hidden;padding: 5px;">
      <div style="margin: 10px 0;">
        <h1>欢迎使用后台管理系统</h1><br />
      </div>
    </div>
  </div>
</div>

<!-- 修改密码弹出框 -->
<div id="editPwdDialog" data-options="region:'center'" class="easyui-dialog" closed="true" buttons="#dlg-buttons" title="修改密码" style="width: 350px;height: 210px;">
  <form id="editPwdForm" method="post" novalidate>
    <div class="ftitle">密码修改</div>
    <div class="fitem">
      <label>原密码:</label>
      <input name="oldPwd" type="password" class="easyui-validatebox" required="true" />
    </div>
    <div class="fitem">
      <label>新密码:</label>
      <input id="newPwd" name="newPwd" type="password" class="easyui-validatebox" required="true" />
    </div>
    <div class="fitem">
      <label>确认密码:</label>
      <input name="rePwd" type="password" class="easyui-validatebox" required="true" validType="equalTo['#newPwd']" />
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
