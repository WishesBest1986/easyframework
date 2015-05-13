<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
  <title>用户登录</title>

  <link rel="stylesheet" type="text/css" href="${ctx}/statics/js/jquery-easyui-1.4.2/themes/default/easyui.css" />
  <link rel="stylesheet" type="text/css" href="${ctx}/statics/css/login.css" />
  <script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.3.min.js"></script>
  <script type="text/javascript" src="${ctx}/statics/js/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>

  <style type="text/css">
    body {
      background-color: #e0ecff;
    }
    .layer1 {
      font-size: x-large;
    }
    .login_border_table {
      margin-left: 150px;
    }
    #layer1 {
      position: absolute;
      text-align: center;
      left: 432px;
      top: 100px;
      width: 620px;
      height: 153px;
      z-index: 1;
    }
    #layer2 {
      position: absolute;
      left: 431px;
      top: 203px;
      width: 621px;
      height: 264px;
      z-index: 2;
      border: 1px solid blue;
      background-color: rgb(163, 195, 245);
    }
  </style>

  <style>.error{color:red}</style>
</head>
<body>
<div class="error">${error}</div>
<div class="content">
  <div class="layer1" id="layer1">通用后台管理系统</div>
  <div class="layer2" id="layer2">
    <form method="post">
      <h1 class="login_title">用户登录</h1>
      <div>&nbsp;</div>

      <table class="login_border_table">
        <tr>
          <td><label>帐号：</label></td>
          <td><input name="username" type="text" class="user" id="username" value="<shiro:principal />" /></td>
        </tr>
        <tr>
          <td><label>密码：</label></td>
          <td><input name="password" type="password" class="password" id="password" /></td>
        </tr>
        <tr>
          <td><label>自动登录：</label></td>
          <td><input name="rememberMe" type="checkbox" id="rememberMe" value="true" /></td>
        </tr>
        <tr>
          <td colspan="2">
            <input type="submit" class="login_btn" id="btn_login" value="登录" />
          </td>
        </tr>
      </table>
    </form>
  </div>
</div>
</body>
</html>
