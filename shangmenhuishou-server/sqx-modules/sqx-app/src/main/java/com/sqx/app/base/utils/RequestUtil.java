package com.sqx.app.base.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chengming
 * @since 2025/11/18
 */
public class RequestUtil {

    public static String getClientType() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return null;
        }

        return request.getHeader("clientType");
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }

        return null;
    }
}
