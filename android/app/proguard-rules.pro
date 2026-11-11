# Dynasty: College Basketball — release (R8)

-keepattributes SourceFile,LineNumberTable,RuntimeVisibleAnnotations,Signature,InnerClasses,EnclosingMethod
-renamesourcefileattribute SourceFile

# Kotlin / coroutines / serialization
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.serialization.json.** { *; }
-keep,includedescriptorclasses class dev.novux.dynasty.cbb.**$$serializer { *; }
-keepclassmembers class dev.novux.dynasty.cbb.** {
    *** Companion;
}
-if class kotlinx.serialization.KSerializer
-keepclasseswithmembers class dev.novux.dynasty.cbb.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Compose runtime (reflection used by compiler)
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }
