import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	id("org.asciidoctor.jvm.convert")
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
    kotlin("kapt")
}

val projectGroup: String by project
val applicationVersion: String by project

group = projectGroup
version = applicationVersion

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    // kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

    // presentation
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // persistence
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
	runtimeOnly("com.mysql:mysql-connector-j")
    implementation("com.querydsl:querydsl-jpa:${property("querydslVersion")}:jakarta")
    kapt("com.querydsl:querydsl-apt:${property("querydslVersion")}:jakarta")
    kapt("jakarta.annotation:jakarta.annotation-api")
    kapt("jakarta.persistence:jakarta.persistence-api")
    implementation("com.github.codemonstur:embedded-redis:${property("embeddedRedisVersion")}")
    runtimeOnly("com.h2database:h2")

    // mail
	implementation("org.springframework.boot:spring-boot-starter-mail")

    // security
	implementation("org.springframework.boot:spring-boot-starter-security")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtVersion")}")
    implementation("io.jsonwebtoken:jjwt-jackson:${property("jwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtVersion")}")

    // aws
    implementation("com.amazonaws:aws-java-sdk-s3:${property("s3Version")}")

    // logging
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
	testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${property("kotestExtensionsSpringVersion")}")
    testImplementation("io.kotest:kotest-runner-junit5:${property("kotestVersion")}")
    testImplementation("io.kotest:kotest-assertions-core:${property("kotestVersion")}")
    testImplementation("io.mockk:mockk:${property("mockkVersion")}")
    testImplementation("com.ninja-squad:springmockk:${property("springmockkVersion")}")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
	inputs.dir(project.extra["snippetsDir"]!!)
	dependsOn(tasks.test)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/.githooks"))
    into(file("$rootDir/.git/hooks"))
    fileMode = "0775".toInt()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
