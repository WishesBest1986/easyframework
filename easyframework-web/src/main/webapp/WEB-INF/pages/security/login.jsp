<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/common/taglibs.jsp" %>
<html>
<head>
  <meta http-equiv="x-ua-compatible" content="edge" />
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

  <title>用户登录</title>

  <!-- jQuery -->
  <script type="text/javascript" src="${ctx}/statics/js/jquery-1.11.3.min.js"></script>

  <!-- jQueryForm -->
  <script type="text/javascript" src="${ctx}/statics/js/jquery.form.js"></script>

  <!-- easyUI -->
  <link rel="stylesheet" type="text/css" href="${ctx}/statics/js/jquery-easyui-1.4.2/themes/default/easyui.css" />
  <script type="text/javascript" src="${ctx}/statics/js/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
  <script type="text/javascript" src="${ctx}/statics/js/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>

  <script type="text/javascript">
    $(function() {
      $('#loginForm').form({
        url: '${ctx}/doLogin',
        onSubmit: function() {
          var isValid = $(this).form('validate');
          return isValid;
        },
        success: function(result) {
          result = $.parseJSON(result);

          if (result.success) {
            window.location.href = '${ctx}/index';
          } else {
            $.messager.show({
              title: '提示',
              msg: '<div class="light-info"><div class="light-tip icon-tip"></div><div>' + result.msg + '</div></div>'
            });
          }
        }
      });
    });

    function submitForm() {
      $('#loginForm').submit();
    }

    function clearForm() {
      $('#loginForm').form('clear');
    }

    function bindEnter(obj) {
      if (obj.keyCode == 13) {
        submitForm();
        obj.returnValue = false;
      }
    }
  </script>
</head>
<body onkeydown="bindEnter(event)">
  <div align="center">
    <div class="easyui-panel" title="登录" style="width: 400px;">
      <div style="padding: 10px 0 10px 100px;">
        <form id="loginForm" method="post">
          <table>
            <tr>
              <td>用户名:</td>
              <td>
                <input class="easyui-validatebox" type="text" name="username" value="<shiro:principal />" data-options="required:true" />
              </td>
            </tr>
            <tr>
              <td>密码:</td>
              <td>
                <input class="easyui-validatebox" type="password" name="password" data-options="required:true" />
              </td>
            </tr>
            <tr>
              <td>自动登录:</td>
              <td>
                <input type="checkbox" name="rememberMe" value="on" />
              </td>
            </tr>
          </table>
        </form>
      </div>
      <div style="text-align: center;padding: 5px;">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">登录</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">清除</a>
      </div>
    </div>
  </div>
</body>
</html>
