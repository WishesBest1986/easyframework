package com.neusoft.easyframework.business.security.shiro;

import com.neusoft.easyframework.business.security.entity.Authority;
import com.neusoft.easyframework.business.security.entity.Resource;
import com.neusoft.easyframework.business.security.service.ResourceService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权元数据根据三部分构成：
 * 1、使用spring的注入filterChainDefinitions，在配置文件中定义默认的安全定义，如登录页面、首页等
 * 2、数据库中动态生成，有注入的resourceService根据资源、权限构建资源-权限的键值对
 * 3、定义默认的安全定义
 *
 * 该服务类同时提供了方法供应用在不重启服务器的情况下，更新授权元数据
 */
public class ShiroDefinitionSectionMetaSourceService {
    private static Logger logger = LoggerFactory.getLogger(ShiroDefinitionSectionMetaSourceService.class);

    private static String PERMISSION_FORMAT = "perms[\"{0}\"]";

//    private static String ROLE_FORMAT = "roles[\"{0}\"]";

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private SessionFactory sessionFactory;

    private String filterChainDefinitions;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    @PostConstruct
    public void initPermission() {
        shiroFilterFactoryBean.setFilterChainDefinitionMap(obtainPermission());
        logger.info("Initialize Shiro permission success...");
    }

    /**
     * 该方法供应用在不重启服务器的情况下，动态更新授权元数据
     */
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver)shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager)filterChainResolver.getFilterChainManager();

            filterChainManager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

            shiroFilterFactoryBean.setFilterChainDefinitionMap(obtainPermission());
            Map<String, String> chainDefinitionMap = shiroFilterFactoryBean.getFilterChainDefinitionMap();

            for (Map.Entry<String, String> entry : chainDefinitionMap.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim().replace(" ", "");
                filterChainManager.createChain(url, chainDefinition);
            }

            logger.info("Update shiro permission success...");
        }
    }

    /**
     * 获取授权元数据
     * 1、使用spring的注入filterChainDefinitions，在配置文件中定义默认的安全定义，如登录页面、首页等
     * 2、数据库中动态生成，由注入的resourceService根据资源、权限构建资源-权限的键值对
     * 3、定义默认的安全定义
     */
    private Ini.Section obtainPermission() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }

        Map<String, String> configurableMap = obtainConfigurableResourcePermission();
        if (configurableMap != null && !configurableMap.isEmpty()) {
            section.putAll(configurableMap);
        }
        section.put("/**", "user");

        return section;
    }

    /**
     * 获取动态生成授权元数据：由注入的resourceService根据资源、权限构建资源-权限的键值对
     * 1、采用LinkedHashMap：保留插入顺序
     * 2、由于Resource类中的authorities使用的FetchType.Lazy,
     *   在首次启动服务器时，为了解决Hibernate中的org.hibernate.LazyInitializationException,
     *   使用注入的sessionFactory显示开启Session的方式予以解决
     */
    private Map<String, String> obtainConfigurableResourcePermission() {
        Map<String, String> permissionMap = new LinkedHashMap<String, String>();

        // 由注入的资源管理对象获取所有资源数据
        List<Resource> resources = resourceService.getAll();
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (Exception e) {
            session = sessionFactory.openSession();
        }

        for (Resource resource : resources) {
            if (StringUtils.isBlank(resource.getSource())) {
                continue;
            }

            session.update(resource);

            for (Authority authority : resource.getAuthorities()) {
                // 如果资源的值为分号分隔，则循环构造元数据。分号分隔好处是对一批相同权限的资源，不需要逐个定义
                if (resource.getSource().indexOf(";") != -1) {
                    String[] sources = resource.getSource().split(";");
                    for (String source : sources) {
                        putDefinitionSection(permissionMap, source, authority.getName());
                    }
                } else {
                    putDefinitionSection(permissionMap, resource.getSource(), authority.getName());
                }
            }
        }
        session.close();

        return permissionMap;
    }

    private void putDefinitionSection(Map<String, String> map, String key, String value) {
        logger.info("加载数据库权限：【key=" + key + "\tvalue=" + value + "】");
        map.put(key, MessageFormat.format(PERMISSION_FORMAT, value));
    }
}
