package com.depth.aar_proguard;

class Assumenosideeffects {

    public static void log(String mes) {
        System.err.println(mes);
    }


    public static void demo() {
        System.out.println("执行Log之前...");
        log("执行了Log方法");
        System.out.println("执行Log之后...");
    }

}
