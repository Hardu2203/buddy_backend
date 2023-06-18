import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven(url = "https://jitpack.io")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-web:3.0.1")
	implementation("org.postgresql:postgresql:42.5.1")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.1")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	implementation("org.springframework.boot:spring-boot-starter-security:2.7.3")
//	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
	implementation("com.github.komputing.khash:keccak:1.0.0-RC5")
	implementation("org.web3j:core:5.0.0")
	implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
	kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kapt {
	arguments {
		arg("mapstruct.defaultComponentModel", "spring")
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
