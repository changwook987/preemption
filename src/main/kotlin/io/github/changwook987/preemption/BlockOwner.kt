package io.github.changwook987.preemption

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class BlockOwner(plugin: JavaPlugin) {
    private val dataFile = File(plugin.dataFolder, "blocks.yml")
    private val yaml = YamlConfiguration.loadConfiguration(dataFile)

    private val data = emptyMap<Material, UUID?>().toMutableMap()
    val iterator get() = data.iterator()

    init {
        for (material in Material.values()) {
            yaml.getString(material.name)?.let { str ->
                val uuid = try {
                    UUID.fromString(str) ?: return@let
                } catch (e: IllegalArgumentException) {
                    e.printStackTrace()
                    return@let
                }

                data[material] = uuid
            }
        }
    }

    operator fun get(material: Material): UUID? {
        return data[material]
    }

    operator fun set(material: Material, uuid: UUID?) {
        data[material] = uuid

        yaml.set(material.name, uuid?.toString())
        yaml.save(dataFile)
    }
}