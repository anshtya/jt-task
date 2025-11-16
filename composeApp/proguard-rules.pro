-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.RuntimeMXBean

-keep class io.ktor.** { *; }
-keepclassmembers class io.ktor.** { *; }

-keep class org.koin.core.annotation.** { *; }
-keep @org.koin.core.annotation.* class * { *; }

-keep class * extends androidx.room.RoomDatabase { <init>(); }

-keep enum com.anshtya.taskrecorder.data.model.TaskType { *; }