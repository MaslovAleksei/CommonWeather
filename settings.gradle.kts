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
include(":data")
include(":domain")
include(":network")
include(":core")
include(":database")
include(":feature:search")
include(":feature:weather")
