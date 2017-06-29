package com.urwoo.config.shiro;

import com.urwoo.framework.annotation.Description;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

    @Bean(name = "lifecycleBeanPostProcessor")
    @Description("保证实现了Shiro内部lifecycle函数的bean执行")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public UrwooAuthorizingRealm shiroRealm() {
        UrwooAuthorizingRealm realm = new UrwooAuthorizingRealm();
        return realm;
    }

    /**
     * SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。
     * 包含（securityManager，filters，filterChainDefinitionManager）
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        Map<String, Filter> filters = new LinkedHashMap<>();
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/login");
        filters.put("logout", logoutFilter);
        shiroFilterFactoryBean.setFilters(filters);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

        filterChainDefinitionMap.put("/logout", "logout");
//        filterChainDefinitionMap.put("/user/**", "authc,roles[user]");
//        filterChainDefinitionMap.put("/shop/**", "authc,roles[shop]");
//        filterChainDefinitionMap.put("/admin/**", "authc,roles[admin]");
//        filterChainDefinitionMap.put("/**", "anon");

//        filterChainDefinitionMap.put("/user/index", "perms[system:user:index]");
//        filterChainDefinitionMap.put("/user/add", "perms[system:user:add]");
//        filterChainDefinitionMap.put("/user/edit*", "perms[system:user:edit]");
//        filterChainDefinitionMap.put("/user/deleteBatch", "perms[system:user:deleteBatch]");
//        filterChainDefinitionMap.put("/user/grant/**", "perms[system:user:grant]");

        filterChainDefinitionMap.put("/**", "authc");
        filterChainDefinitionMap.put("/auth/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        return shiroFilterFactoryBean;
    }
}
