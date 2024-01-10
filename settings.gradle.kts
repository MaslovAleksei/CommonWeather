pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CommonWeather"
include(":app")
include(":network")
include(":database")
include(":feature:search")
include(":feature:weather")
include(":core:theme")
include(":core:core")
