<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>

<!-- jQuery -->
<script type="text/javascript" src="${ctx}/statics/jslib/jquery-1.11.3.min.js"></script>

<!-- jQueryForm -->
<script type="text/javascript" src="${ctx}/statics/jslib/jquery.form.js"></script>

<!-- easyUI -->
<link rel="stylesheet" type="text/css" href="${ctx}/statics/jslib/jquery-easyui-1.4.2/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/statics/jslib/jquery-easyui-1.4.2/themes/icon.css" />
<script type="text/javascript" src="${ctx}/statics/jslib/jquery-easyui-1.4.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${ctx}/statics/jslib/jquery-easyui-1.4.2/locale/easyui-lang-zh_CN.js"></script>

<!-- zTree -->
<link rel="stylesheet" type="text/css" href="${ctx}/statics/jslib/zTree_v3/css/zTreeStyle/zTreeStyle.css" />
<script type="text/javascript" src="${ctx}/statics/jslib/zTree_v3/js/jquery.ztree.all-3.5.js"></script>

<!-- common javascript code -->
<script type="text/javascript">
    $.ajaxSetup({
        cache: false,
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 401) {
                $.messager.alert({
                    title: '错误',
                    msg: '您没有足够的权限执行该操作!'
                });
            }
        }
    });

    $.extend($.fn.form.defaults, {
        queryParams: {ajax : 'true'},
        success: function(data) {
            if (data.search(/HTTP Status 401/) != -1) {
                $.messager.alert({
                    title: '错误',
                    msg: '您没有足够的权限执行该操作!'
                });
            } else {
                try {
                    data = $.parseJSON(data);

                    this.successHandler(data);
                } catch(e) {
                    $.messager.show({
                        title: '错误',
                        msg: '服务器返回数据解析错误'
                    });
                }
            }
        },
        successHandler: function(result) {}
    });

    $.extend($.fn.datagrid.defaults, {
        queryParams: {ajax: 'true'},
        onLoadError: function(XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 401) {
                $.messager.alert({
                    title: '错误',
                    msg: '您没有足够的权限执行该操作!'
                });
            }
        }
    });
</script>