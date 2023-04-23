package com.spring.cloud.base.monitor.sleuth;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: ls
 * @Description:
 * @Date: 2023/4/23 09:47
 */
public class SourceIpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final String ip = LogIpUtil.getIpAddress((HttpServletRequest) servletRequest);
        MDC.put("sourceIp", ip);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }


}
