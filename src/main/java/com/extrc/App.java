package com.extrc;

import java.io.File;

import com.extrc.view.console.ConsoleApp;

public class App {
    public static void usage() {
        String appName = new File(App.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
        System.out.println("Usage: java -jar " + appName + " [console/desktop]");
    }

    public static void main(String[] args) throws Exception {
        // Example.run(args);
        if (args == null || args.length == 0) {
            usage();
        } else {
            String mode = args[0];
            switch (mode) {
                case "console" -> ConsoleApp.run();
                default -> usage();
            }
        }
    }
}
