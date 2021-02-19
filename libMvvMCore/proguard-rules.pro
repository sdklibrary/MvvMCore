#noinspection ShrinkerUnresolvedReference

##---------------Begin: proguard configuration for EventBus  ----------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

##---------------End: proguard configuration for EventBus  ----------
-keep class com.pretty.library.mvvm.** { *; }