-keepattributes *Annotation*
-keep class com.noplans.blockchain.** { *; }
-keep class org.java_websocket.** { *; }
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Preserve line numbers for crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Timber
-dontwarn org.jetbrains.annotations.**
