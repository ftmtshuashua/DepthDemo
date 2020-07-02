#移除未使用类 - 禁用
-dontshrink
#混淆之后将所有能修改包名的类移动到某包下
-repackageclasses 'c'

#保持类的 - 类名
-keepnames  class com.depth.aar_proguard.KeepNames{}


#-assumenosideeffects class com.tools.openadsdk.utils.LogUtils {
# public static void i(...);
# }