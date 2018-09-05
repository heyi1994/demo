
# Android Extends Library 

   为简化安卓开发而针对Kotlin的一系列扩展 ;




### 添加库依赖 ;
 
         //需要,版本可以不同 
         implementation 'com.android.support:appcompat-v7:28.0.0-rc02'
         implementation"org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
         
         api 'org.heyi.base:library-fab:1.0.1'
         
         
         repositories {
           jcenter()
           ...
         }
         
    
        
### 简介 ;
 
 - Activity 扩展 ;
 
   1. fun Activity.keepScreenOn() : 保持屏幕常亮 ;
   2. fun Activity.fullScreen() : 全屏 ;
   3. fun Activity.setStatusBarColor(@ColorRes colorRes: Int) : 状态栏染色 ;
   4. fun Activity.transparentStatusBar(): Int : 状态栏透明并返回状态栏高度 ;
   
   ....
   详见ActivityExtend.kt
   
 - String 扩展 ;
 
   1. String.isEmail() : 当前字符串是否为email ;
   2. String.isPhoneNumber() : 当前字符串是否为手机号 ;
   
 - Uri 扩展 ;
 
   1. fun Uri.toPath(context:Context):String : 将当前uri转为路径 ;
   
   
   持续更新中 ...
   