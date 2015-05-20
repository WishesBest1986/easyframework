package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.business.security.service.ResourceService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;

/**
 * 授权元数据根据两部分构成：
 * 1、数据库中动态生成，有注入的resourceService根据资源、权限构建资源-权限的键值对
 * 2、使用spring的注入filterChainDefinitions，在配置文件中定义默认的安全定义，如登录页面、首页等
 */
public class ShiroDefinitionSectionMetaSource implements FactoryBean<Ini.Section> {
    private static Logger logger = LoggerFactory.getLogger(ShiroDefinitionSectionMetaSource.class);

    private static String PERMISSION_FORMAT = "perms[\"{0}\"]";

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SessionFactory sessionFactory;

    private String filterChainDefinitions;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public Ini.Section getObject() throws Exception {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        // 由注入的资源管理对象获取所有资源数据，并且Resource的authorities的属性是EAGER的fetch属性
        List<Resource> resources = resourceService.getAll();
        Session session =  sessionFactory.openSession();
        for (Resource resource : resources) {
            if (StringUtils.isEmpty(resource.getSource())) {
                continue;
            }

            session.update(resource);

            for (Authority authority : resource.getAuthorities()) {
                // 如果资源的值为分号分隔，则循环构造元数据。分号分隔好处是对一批相同权限的资源，不需要逐个定义
                if (resource.getSource().indexOf(";") != -1) {
                    String[] sources = resource.getSource().split(";");
                    for (String source : sources) {
                        putDefinitionSection(section, source, authority.getName());
                    }
                } else {
                    putDefinitionSection(section, resource.getSource(), authority.getName());
                }
            }
        }
        session.close();

        section.put("/**", "user");
        return section;
    }

    private void putDefinitionSection(Ini.Section section, String key, String value) {
        logger.info("加载数据库权限：【key=" + key + "\tvalue=" + value + "】");
        section.put(key, MessageFormat.format(PERMISSION_FORMAT, value));
    }

    public Class<?> getObjectType() {
        return this.getClass();
    }

    public boolean isSingleton() {
        return false;
    }
}
