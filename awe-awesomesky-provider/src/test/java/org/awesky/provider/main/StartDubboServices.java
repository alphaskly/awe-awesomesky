package org.awesky.provider.main;

/**
 * Created by vme on 2017/8/29.
 */
public class StartDubboServices {

    public static void main(String[] args) {
        System.out.println("Starting dubbo service...");
        com.alibaba.dubbo.container.Main.main(args);
        System.out.println("Started dubbo success!");
    }

}
