plugins {
    java
    war
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "java-prac"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

val systemTest by sourceSets.creating {
    java.srcDir("src/systemTest/java")
    resources.srcDir("src/systemTest/resources")
    compileClasspath += sourceSets.main.get().output + configurations.testCompileClasspath.get()
    runtimeClasspath += output + compileClasspath + configurations.testRuntimeClasspath.get()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    named(systemTest.implementationConfigurationName) {
        extendsFrom(configurations.testImplementation.get())
    }

    named(systemTest.runtimeOnlyConfigurationName) {
        extendsFrom(configurations.testRuntimeOnly.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.2")
    runtimeOnly("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.testng:testng:7.10.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.31.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.withType<Test> {
    useTestNG()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("systemTest") {
    description = "Runs Selenium system tests against the Spring Boot web application."
    group = "verification"

    testClassesDirs = systemTest.output.classesDirs
    classpath = systemTest.runtimeClasspath
    shouldRunAfter(tasks.test)

    useTestNG {
        includeGroups("system")
    }

    systemProperty("java.awt.headless", "true")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    include("java_prac/dao/impl/**")
                    include("java_prac/service/impl/**")
                    include("java_prac/controller/**")
                }
            }
        )
    )
}
