pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RolePlayAI"
include(":RolePlayAI")
include(":shared")
include(":data")
include(":domain")
include(":util")
