plugins {
    id("java-library")
    id("maven-publish")
}
val appVersion: String by project

group = "io.rsug"
version = "0.0.1"

repositories {
    mavenCentral()
    mavenLocal()
}

java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(8)
//    }
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0.1")
    testImplementation("commons-io:commons-io:2.22.0")

    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jar {
    from("src/main") {
        into("src")
    }

    from("build.gradle.kts") {
        into("src")
    }
    from("settings.gradle.kts") {
        into("src")
    }
    manifest {
//        attributes["Implementation-Version"] = "7.654321"
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

