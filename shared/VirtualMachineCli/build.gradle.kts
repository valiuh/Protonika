import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrainsKotlinJvm)
    application
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "11"
    targetCompatibility = "11"
    options.release.set(11)
}

dependencies {
    implementation(projects.shared.virtualMachine)
    testImplementation(libs.kotlin.testJunit)
}

application {
    mainClass = "com.valiukh.protonika.virtualmachine.cliwrapper.MainKt"
}

tasks.test {
    useJUnit()
}

val fatJar by tasks.registering(Jar::class) {
    group = "build"
    description = "Builds an executable fat JAR for neutronika CLI."
    archiveBaseName.set("neutronika-cli")
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)

    from({
        configurations.runtimeClasspath.get()
            .filter { it.extension == "jar" }
            .map { zipTree(it) }
    })
}

tasks.assemble {
    dependsOn(fatJar)
}
