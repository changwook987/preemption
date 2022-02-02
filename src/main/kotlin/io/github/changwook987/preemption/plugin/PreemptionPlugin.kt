package io.github.changwook987.preemption.plugin

import io.github.changwook987.preemption.BlockOwner
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin


class PreemptionPlugin : JavaPlugin() {
    val blockOwner = BlockOwner(this)

    override fun onEnable() {
        EventListener(this).register()
    }

    private fun Listener.register() = server.pluginManager.registerEvents(this, this@PreemptionPlugin)
}