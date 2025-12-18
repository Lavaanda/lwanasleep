// Top-level build file for the project

plugins {
    id("com.android.application") version "8.13.0" apply false
    id("com.android.library") version "8.13.0" apply false
    kotlin("android") version "1.9.10" apply false
}

// В корневом build.gradle.kts больше не нужны блоки repositories
// все репозитории управляются через settings.gradle.kts
