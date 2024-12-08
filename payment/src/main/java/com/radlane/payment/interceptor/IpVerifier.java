package com.radlane.payment.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
public class IpVerifier implements HandlerInterceptor {

    private final String allowedIp;

    public IpVerifier(String allowedIp) {
        this.allowedIp = allowedIp;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        if (!allowedIp.equals(clientIp)) {
            log.warn("Unauthorized IP address: {}", clientIp);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized IP");
            return false;
        }
        return true;
    }
}