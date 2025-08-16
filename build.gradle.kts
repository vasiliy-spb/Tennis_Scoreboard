plugins {
    war
    id("java")
    id("org.gretty") version("4.1.0")
}

group = "dev.chearcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.hibernate.validator:hibernate-validator:9.0.1.Final")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
}

tasks.test {
    useJUnitPlatform()
}

gretty {
    servletContainer = "jetty11"
    httpPort = 8080
//    contextPath = "/tennis-scoreboard"
    contextPath = "/myapp"
}