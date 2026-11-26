package com.sqx.common.shiro.realm;

import com.sqx.common.core.enums.SystemErrorCode;
import com.sqx.common.shiro.service.ShiroService;
import com.sqx.common.shiro.token.JWTToken;
import com.sqx.common.shiro.utils.JwtUtil;
import com.sqx.domain.db.entity.SysUserEntity;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author chengming
 * @since 2025/4/30
 */
@Slf4j
@Component
public class JWTRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ShiroService shiroService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 查询用户权限信息. 真正的授权在SecurityManager和Authorizer组件内部
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity sysUser = (SysUserEntity) principals.getPrimaryPrincipal();

        // 查询用户的身份和权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> userPermissions = shiroService.getUserPermissions(sysUser.getUserId());
        info.addStringPermissions(userPermissions);
        return info;
    }

    /**
     * 认证
     * @param authenticationToken the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials(); // 重写了该类，实际上返回的是token
        if (StringUtils.isBlank(token) || !jwtUtil.validateToken(token)) {
            log.error("token校验失败: {}", token);
            throw new AuthenticationException(SystemErrorCode.UNAUTHORIZED.getMessage());
        }

        String userId;
        try {
            // 根据token获得登录用户的id
            Claims claims = jwtUtil.getClaimByToken(token);
            userId = (String) claims.get("sub");
        } catch (Exception e) {
            throw new AuthenticationException(SystemErrorCode.UNAUTHORIZED.getMessage());
        }

        SysUserEntity sysUser = shiroService.queryUser(Long.parseLong(userId));
        if (Objects.isNull(sysUser)) {
            throw new AuthenticationException(SystemErrorCode.USERNAME_ERROR.getMessage());
        }

        if(0 == sysUser.getStatus()){
            throw new AuthenticationException(SystemErrorCode.DISABLE_USER.getMessage());
        }

        return new SimpleAuthenticationInfo(sysUser, token, this.getName());
    }
}
