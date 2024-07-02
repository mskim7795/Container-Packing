import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation("com.google.code.gson:gson:2.8.8")
                implementation("org.slf4j:slf4j-api:2.0.13")
                implementation("ch.qos.logback:logback-classic:1.4.14")
                implementation("org.apache.poi:poi-ooxml:5.2.3")
                implementation(project.dependencies.platform("org.dizitart:nitrite-bom:4.2.2"))
                implementation("org.dizitart:potassium-nitrite")
                implementation("org.dizitart:nitrite-mvstore-adapter")
            }
        }
    }

}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            modules("java.compiler", "java.instrument", "java.sql", "jdk.unsupported", "java.naming")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "container_packing"
            packageVersion = "1.0.0"

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}
