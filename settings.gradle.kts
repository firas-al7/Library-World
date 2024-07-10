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
        jcenter()
        maven(url = "https://jetpack.io/" )
//        maven(url = "https://maven.ghostscript.com")
        mavenCentral()
    }
}

rootProject.name = "library of world"
include(":app")
