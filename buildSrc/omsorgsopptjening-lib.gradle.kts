import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import net.researchgate.release.ReleaseExtension


import net.researchgate.release.ReleaseExtension
        repositories {
            maven {
                url 'https://plugins.gradle.org/m2/'
            }
        }
dependencies {
    classpath 'net.researchgate:gradle-release:3.0.2'
}

apply(plugin = "base")
apply(plugin = "net.researchgate.release")

configure<ReleaseExtension> {
    with(git) {
        requireBranch.set("main")
        // to disable branch verification: requireBranch.set(null as String?)
    }
}

release {
    newVersionCommitMessage = "P - [Release Plugin] - next version commit: "
    tagTemplate = "release-\${version}"
    git {
        requireBranch.set('main')
    }
}