plugins {
    java
    `java-library`
    signing
    id("com.gradleup.shadow")
    id("com.vanniktech.maven.publish")
}

group = "io.github.lumine1909"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
tasks.test {
    useJUnitPlatform()
}
tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}


mavenPublishing {
    coordinates(
        groupId = group as String,
        artifactId = "reflexion",
        version = version as String
    )
    pom {
        name.set("reflexion")
        description.set("A fast implementation library for Java reflection.")
        url.set("https://github.com/Lumine1909/Reflexion")
        licenses {
            license {
                name.set("LGPL License")
                url.set("https://github.com/Lumine1909/Reflexion/blob/main/LICENSE")
            }
        }

        developers {
            developer {
                id.set("Lumine1909")
                name.set("Lumine1909")
                email.set("133463833+Lumine1909@users.noreply.github.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/Lumine1909/Reflexion.git")
            developerConnection.set("scm:git:ssh://github.com/Lumine1909/Reflexion.git")
            url.set("https://github.com/Lumine1909/Reflexion")
        }
    }

    publishToMavenCentral(true)
    signAllPublications()
}
