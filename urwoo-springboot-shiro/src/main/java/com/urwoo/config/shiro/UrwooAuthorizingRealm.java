package com.urwoo.config.shiro;

import com.urwoo.framework.lang.StringUtils;
import com.urwoo.po.RightPo;
import com.urwoo.po.UserPo;
import com.urwoo.service.RightService;
import com.urwoo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义授权Realm
 */
@Component
public class UrwooAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RightService rightService;

    /** 授权 */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserPo userPo = (UserPo)principalCollection.getPrimaryPrincipal();
        List<RightPo> rightList = rightService.userRightList(userPo.getId());
        List<String> rights = new ArrayList<>();
        if (null != rightList && !rightList.isEmpty()){
            for(RightPo rightPo : rightList){
                if(StringUtils.isNotBlank(rightPo.getRightCode())){
                    rights.add(rightPo.getRightCode());
                }
            }
        }
        //查到权限数据，返回授权信息(要包括上边的rights)
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        simpleAuthorizationInfo.addStringPermissions(rights);
        return simpleAuthorizationInfo;
    }

    /** 认证 */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        try {

            String username = authenticationToken.getPrincipal().toString();  //获得用户名称
            String password = new String((char[])authenticationToken.getCredentials()); //获得密码

            UserPo userPo = userService.getUser(username);
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userPo, password, this.getName());
            info.setCredentialsSalt(ByteSource.Util.bytes(username));
            return info;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
