sunnybear快速开发框架源码和例子

sample是测试试例

library是快速开发框架
该框架中的方法与类见注释
说明:BasicFragment和BasicFragmentActivity调整为过期,新增DispatchActivity和DispatchFragment(类似MVP模式),
试例见sample

blur是C层图片模糊库(动态库已集成如library中)
生成头文件时一定要移动到src/main/java下
生成h头文件的命令为javah -jni -d ../jni xxx.xxx.xxx.xxx

videoplayer是视频播放库
使用方法:
    AndroidManifest.xml
        <!--视频播放器-->
        <activity
            android:name="com.sunnybear.player.VideoPlayerActivity"
            android:screenOrientation="landscape"/>
    Activity或者Fragment中调用
         Bundle bundle = new Bundle();
         bundle.putString(VideoPlayerActivity.VIDEO_URL,
                                SDCardUtils.getSDCardPath() + File.separator + "test.mp4");
         startActivity(VideoPlayerActivity.class, bundle);
         
makejar是打jar包试例
使用方法:
        配置见build.gradle
        在控制台中输入gradlew makeJar
        输入路径 build/libs/xxxx.jar
