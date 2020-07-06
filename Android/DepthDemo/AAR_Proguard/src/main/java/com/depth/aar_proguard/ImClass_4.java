package com.depth.aar_proguard;

class ImClass_4 {
    String 类成员 = "类成员";

    static String 静态_类成员 = "静态_类成员";

    static final String 静态常量_类成员 = "静态常量_类成员";

    public String 公有_类成员 = "公有_类成员";

    public static String 公有_静态_类成员 = "公有_静态_类成员";

    public static final String 公有_静态常量_类成员 = "公有_静态常量_类成员";

    public ImClass_4() {
        System.out.println("构造函数");
    }

    public ImClass_4(String args) {
        System.out.println("带参数构造函数");
    }

    public void 公有_普通函数() {
        System.out.println("公有_普通函数");
    }

    public void 公有_带参数函数(String args) {
        System.out.println("公有_带参数函数");
    }

    public String 公有_普通函数_带返回值() {
        System.out.println("公有_普通函数_带返回值");
        return "";
    }

    public String 公有_带参数函数_带返回值(String args) {
        System.out.println("公有_带参数函数_带返回值");
        return "";
    }

    public static void 公有_静态_普通函数() {
        System.out.println("公有_静态_普通函数");
    }

    public static void 公有_静态_带参数函数(String args) {
        System.out.println("公有_静态_带参数函数");
    }

    public static String 公有_静态_普通函数_带返回值() {
        System.out.println("公有_静态_普通函数_带返回值");
        return "";
    }

    public static String 公有_静态_带参数函数_带返回值(String args) {
        System.out.println("公有_静态_带参数函数_带返回值");
        return "";
    }

    private void 私有_普通函数() {
        System.out.println("私有_普通函数");
    }

    private void 私有_带参数函数(String args) {
        System.out.println("私有_带参数函数");
    }

    private String 私有_普通函数_带返回值() {
        System.out.println("私有_普通函数_带返回值");
        return "";
    }

    private String 私有_带参数函数_带返回值(String args) {
        System.out.println("私有_带参数函数_带返回值");
        return "";
    }

    private static void 私有_静态_普通函数() {
        System.out.println("私有_静态_普通函数");
    }

    private static void 私有_静态_带参数函数(String args) {
        System.out.println("私有_静态_带参数函数");
    }

    private static String 私有_静态_普通函数_带返回值() {
        System.out.println("私有_静态_普通函数_带返回值");
        return "";
    }

    private static String 私有_静态_带参数函数_带返回值(String args) {
        System.out.println("私有_静态_带参数函数_带返回值");
        return "";
    }
}
