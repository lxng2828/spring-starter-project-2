package com.vtlong.my_spring_boot_project.util;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class RequestContextUtil {

    public static String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    public static String getRequestId() {

        return generateRequestId();
    }
}
