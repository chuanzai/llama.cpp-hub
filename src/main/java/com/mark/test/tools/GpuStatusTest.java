package com.mark.test.tools;

import org.mark.llamacpp.server.service.GpuService;
import org.mark.llamacpp.server.tools.JsonUtil;

public class GpuStatusTest {

    public static void main(String[] args) {
        System.out.println("======== GpuService Info (init snapshot) ========");
        String infoJson = JsonUtil.toJson(GpuService.getInstance().getServiceInfo());
        System.out.println(infoJson);

        System.out.println("\n======== GpuService Status (live query) ========");
        String statusJson = JsonUtil.toJson(GpuService.getInstance().queryGpuStatus());
        System.out.println(statusJson);
    }
}
