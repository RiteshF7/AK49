# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Check your missing_rules.txt and add those rules here
# Common rules for Retrofit, Moshi, and Firebase
-keepclassmembers class * {
    @com.squareup.moshi.* <methods>;
}

-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }
-keep class com.google.firebase.** { *; }
-keep class your.package.name.models.** { *; }  # Replace with your model package

# Keep your data classes
-keep class com.trex.rexnetwork.data.** { *; }
# Keep the entire package
-keep class com.trex.rexnetwork.data.** { *; }

# If you're using Serializable (which is common for DTOs)
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# For Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

# For Moshi
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }

# Keep all Kotlin data classes
-keepclassmembers class ** {
    @kotlin.Metadata <fields>;
}
-keep class kotlin.Metadata { *; }

# Keep all classes with @Keep annotation
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# Keep all data classes specifically
-keep class **.*Dto { *; }
-keep class **.*DTO { *; }
-keep class **.*Entity { *; }
-keep class **.*Request { *; }
-keep class **.*Response { *; }
-keep class **.*Model { *; }
-keep class **.data.** { *; }
-keep class **.domain.model.** { *; }

# Keep Serializable and Parcelable implementations
-keep class * implements java.io.Serializable { *; }
-keep class * implements android.os.Parcelable { *; }

# If using Moshi
-keepclassmembers class ** {
    @com.squareup.moshi.* <methods>;
    @com.squareup.moshi.* <fields>;
}

# If using Gson
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
## Keep StringConcatFactory and its methods
#-keep class java.lang.invoke.StringConcatFactory {
#    public static java.lang.invoke.CallSite makeConcat(java.lang.invoke.MethodHandles$Lookup, java.lang.String, java.lang.invoke.MethodType, java.lang.String, java.lang.Object[]);
#    public static java.lang.invoke.CallSite makeConcatWithConstants(java.lang.invoke.MethodHandles$Lookup, java.lang.String, java.lang.invoke.MethodType, java.lang.String, java.lang.Object[]);
#}

# Additional Java 11 related rules
-dontwarn java.lang.invoke.**
-keep class java.lang.invoke.** { *; }

# Keep FCM related classes
-keep class com.trex.rexnetwork.domain.firebasecore.fcm.** { *; }
-keep interface com.trex.rexnetwork.domain.firebasecore.fcm.** { *; }

# Keep Firestore related classes
-keep class com.trex.rexnetwork.domain.firebasecore.firesstore.** { *; }

# Keep Repository classes
-keep class com.trex.rexnetwork.domain.repositories.** { *; }

# Keep Utility classes
-keep class com.trex.rexnetwork.utils.** { *; }
-keep class com.trex.rexnetwork.utils.ExtensionFunctionsKt { *; }

# Since you're using Kotlin, add these general Kotlin rules
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# For Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# If you're using Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
#-keepclassmembers @kotlinx.serialization.Serializable class ** {
#    static ** Companion;
#}