package com.engulf.config;

import com.engulf.pojo.User;
import com.engulf.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class ChampionRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==>授权doGetAuthorizationInfo");

        SimpleAuthorizationInfo Info = new SimpleAuthorizationInfo();
//        Info.addStringPermission("user:add");
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        //获取用户
        User currentUser = (User)subject.getPrincipal();     //取的就是下面传过来的user，SimpleAuthenticationInfo(user,user.getPassword(),""); 使得授权和认证关联

        //设置用户权限
        Info.addStringPermission(currentUser.getPerms());

        return Info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了==>认证doGetAuthenticationInfo");

        // 用户名，密码,数据库中获取
        UsernamePasswordToken userTaken = (UsernamePasswordToken)authenticationToken;

        User user = userService.getUserByName(userTaken.getUsername());

        if(!userTaken.getUsername().equals(user.getUsername())){  //判断用户名是否正确
            return null;  //return null 就会抛出UnknownAccountException
        }
        //密码认证shiro自己完成,并加密
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
