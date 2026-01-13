package com.sfsoftware.shifts.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private Bucket newBucket(long seconds) {
        return Bucket.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(5)
                                .refillIntervally(5, Duration.ofSeconds(seconds))
                                .build()
                )
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        return (xf != null) ? xf.split(",")[0] : request.getRemoteAddr();
    }

    private Bucket resolveBucket(String ip, String key, long seconds) {
        return buckets.computeIfAbsent(ip + ":" + key, k -> newBucket(seconds));
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String ip = getClientIp(request);
        String path = request.getRequestURI();

        Bucket bucket = null;

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        if (path.equals("/new")) {
            bucket = resolveBucket(ip, "new", 10);
        } else if (path.equals("/call")) {
            bucket = resolveBucket(ip, "call", 1);
        } else if (path.equals("/edit")) {
            bucket = resolveBucket(ip, "edit", 10);
        } else if (path.startsWith("/ticketQueue")) {
            bucket = resolveBucket(ip, "queue", 10);
        }

        if (bucket != null && !bucket.tryConsume(1)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests");
            return false;
        }

        return true;
    }
}
