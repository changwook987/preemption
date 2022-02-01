package io.github.changwook987.preemption.plugin

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*


class PreemptionPlugin : JavaPlugin() {
    val blockOwner = emptyMap<Material, UUID?>().toMutableMap()

    private val blocks: File = File(dataFolder, "blocks.yml")
    private val yaml: YamlConfiguration = YamlConfiguration.loadConfiguration(blocks)

    override fun onEnable() {
        EventListener(this).register()

        for (material in Material.values()) {
            val string = yaml.getString(material.name, null) ?: continue
            val uuid = try {
                UUID.fromString(string) ?: continue
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                continue
            }

            blockOwner[material] = uuid
        }
    }

    fun setBlockOwner(material: Material, owner: UUID?) {
        blockOwner[material] = owner
        yaml.set(material.name, owner?.toString())

        yaml.save(blocks)
    }


    private fun Listener.register() = server.pluginManager.registerEvents(this, this@PreemptionPlugin)
}