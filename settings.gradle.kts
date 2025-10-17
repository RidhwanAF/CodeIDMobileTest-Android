pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://mobile.maven.couchbase.com/maven2/dev/") }
        google()
        mavenCentral()
    }
}

rootProject.name = "MobileTaskCodeIdTest"
include(":app")
include(":core")
include(":feature:settings")
include(":feature:auth")
include(":feature:profile")
include(":feature:home")
