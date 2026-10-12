package com.saucedemo.playwright.testdata;

public final class DemoUsers {
    public static final String STANDARD_USERNAME = credential("DEMO_USERNAME", "standard_user");
    public static final String PASSWORD = credential("DEMO_PASSWORD", "secret_sauce");

    private DemoUsers() {
    }

    private static String credential(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
