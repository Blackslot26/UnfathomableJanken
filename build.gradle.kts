plugins {
    java
    application
}

application {
    mainClass.set("org.fornipinto.unfathomable_janken.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_27
    targetCompatibility = JavaVersion.VERSION_27
}

group = "org.fornipinto.unfathomable_janken"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jline:jline:3.30.17")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "org.fornipinto.unfathomable_janken.Main"
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
