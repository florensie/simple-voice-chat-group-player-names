plugins {
	id("net.fabricmc.fabric-loom")
	id("me.modmuss50.mod-publish-plugin") version "2.2.1"
	`maven-publish`
}

repositories {
	maven("https://api.modrinth.com/maven")
	maven("https://maven.shedaniel.me/")
	maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
	implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	implementation("maven.modrinth:simple-voice-chat:${providers.gradleProperty("voicechat_version").get()}")
	implementation("me.shedaniel.cloth:cloth-config-fabric:${providers.gradleProperty("cloth_config_version").get()}") {
		exclude(group = "net.fabricmc.fabric-api")
	}

	compileOnly(localRuntime("com.terraformersmc:modmenu:${providers.gradleProperty("modmenu_version").get()}")!!)
	localRuntime("maven.modrinth:styled-nicknames:${providers.gradleProperty("styled_nicknames_version").get()}")
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}
}

publishMods {
	file.set(tasks.jar.flatMap { it.archiveFile })
	displayName.set(version)
	changelog.set("")
	type.set(me.modmuss50.mpp.ReleaseType.STABLE)
	modLoaders.add("fabric")

	modrinth {
		projectId.set("oKsphwOn")
		accessToken.set(providers.environmentVariable("MODRINTH_TOKEN"))
		minecraftVersions.add(providers.gradleProperty("minecraft_version").get())
		requires("fabric-api", "simple-voice-chat", "cloth-config")
		optional("modmenu")
	}
}
