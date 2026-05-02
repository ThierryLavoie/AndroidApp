plugins {
    id("com.android.application") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}

val localBuildRoot = System.getenv("LOCALAPPDATA") ?: "C:/Temp"

allprojects {
    layout.buildDirectory.set(file("$localBuildRoot/AndroidAppBuild/${project.name}"))
}
