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
    versionCatalogs {
        create("libs") {
            version("ktor", "2.3.1")
            library("ktor-core", "io.ktor", "ktor-client-core").versionRef("ktor")
            library("ktor-cio", "io.ktor", "ktor-client-cio").versionRef("ktor")
            library("ktor-logging", "io.ktor", "ktor-client-logging").versionRef("ktor")
            library(
                "ktor-negotiation",
                "io.ktor",
                "ktor-client-content-negotiation"
            ).versionRef("ktor")
            library(
                "ktor-serialization",
                "io.ktor",
                "ktor-serialization-kotlinx-json"
            ).versionRef("ktor")

            bundle(
                "ktor",
                listOf(
                    "ktor-core",
                    "ktor-cio",
                    "ktor-negotiation",
                    "ktor-serialization",
                    "ktor-logging"
                )
            )

            version("koin", "3.4.0")
            library("koin-android", "io.insert-koin", "koin-android").versionRef("koin")
            library("koin-compose", "io.insert-koin", "koin-androidx-compose").versionRef("koin")
            bundle("koin", listOf("koin-android", "koin_compose"))

            library("lifecycle-compose", "androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
        }
    }

}

rootProject.name = "RolePlayAI"
include(":RolePlayAI")
include(":shared")
include(":data")
