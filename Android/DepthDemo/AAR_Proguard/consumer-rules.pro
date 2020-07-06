#Proguard手册：
#https://www.guardsquare.com/en/products/proguard/manual/usage
#https://stuff.mit.edu/afs/sipb/project/android/sdk/android-sdk-linux/tools/proguard/docs/index.html#manual/usage.html
#参考：
#区分ProGuard不同的"keep"指令:https://blog.csdn.net/lihenair/article/details/78591443
#Proguard的Keep使用方法:https://blog.csdn.net/zhangdaiscott/article/details/73800982
############################### 通用 ###################################
#移除未使用类 - 禁用
-dontshrink
#混淆之后将所有能修改包名的类移动到某包下
-repackageclasses 'c'


############################### keep ###################################
#-keepclassmembers  ：如果类未被使用,则删除类。如果类被使用,保留并重命名该类。类里的成员维持不变,仍然是之前的名字。
#-keepnames         : 压缩类及成员，但不混淆它们。也就是说，未使用的代码将被移除。剩下的代码则维持原状
#-keepclassmembernames: 这是最宽容的keep指令；它允许ProGuard完成几乎所有工作。移除未使用的类，剩下的类被重命名，类中未使用的成员将被移除，剩余的成员保留原来的名称。
#-keepclasseswithmembers: 这个变体没有表格，因为它与-keep作用一致。区别是它只适用于拥有类规范中所有成员的类
#-keepclasseswithmembernames: 这条规则与-keepnames一致。区别也是它只适用于拥有类规范中所有成员的类。


#-keep              : 可以保留指定的类名以及成员
#-keepclassmembers  : 只能保留住成员而不能保留住类名
#-keepclasseswithmembers: 可以根据成员找到满足条件的所有类而不用指定类名(这样的类必定拥有所列出的所有成员),可以保留类名和成员名

#保持包下所有类的 - 类名|成员方法|成员属性
#-keep class com.depth.aar_proguard.**{*;}

#保持类的 - 类名
-keep class com.depth.aar_proguard.ImClass_2{}

#保持类的 - 类名|成员方法|成员属性
-keep class com.depth.aar_proguard.ImClass_4{*;}

############################### 删除代码 ###################################
#从代码中删除某些方法的引用
-assumenosideeffects class com.depth.aar_proguard.Assumenosideeffects {
 public static void log(...);
 }