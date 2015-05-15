function addTab(tabsObj, params) {
    var frame = createFrame(params.url);
    var options = {
        title: params.title,
        iconCls: params.iconCls,
        content: frame,
        closable: true,
        fit: true,
        border: false
    };

    if (tabsObj.tabs('exists', options.title)) {
        tabsObj.tabs('select', options.title);
        if (params.forceRefresh) {
            var curTab = tabsObj.tabs('getSelected');
            tabsObj.tabs('update', {
                tab: curTab,
                options: options
            });
        } else if (params.loadNew) {
            var curTab = tabsObj.tabs('getSelected');
            if (curTab.panel('options').closable) {
                var curTabIndex = tabsObj.tabs('getTabIndex', curTab);
                tabsObj.tabs('close', curTabIndex);
            }
            tabsObj.tabs('add', options);
        }
    } else {
        tabsObj.tabs('add', options);
    }
}

function createFrame(url) {
    var content = '<iframe scrolling="auto" frameborder="0" src="' + url + '" style="border:0;width:100%;height:98%;"></iframe>';
    return content;
}
