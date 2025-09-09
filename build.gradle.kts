plugins {
    war
    id("java")
    id("org.gretty") version ("4.1.1")
}

group = "dev.chearcode"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    implementation("org.hibernate.validator:hibernate-validator:9.0.1.Final")
    implementation("org.hibernate.orm:hibernate-core:7.1.0.Final")
    implementation("com.h2database:h2:2.3.232")
    implementation("com.zaxxer:HikariCP:7.0.2")

    runtimeOnly("org.slf4j:slf4j-simple:2.0.7")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

gretty {
    servletContainer = "jetty11"
    httpPort = 8080
    contextPath = "/tennis-scoreboard"
}