package io.github.changwook987.preemption.plugin

import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.FireworkEffect
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.util.Vector

class EventListener(private val plugin: PreemptionPlugin) : Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    fun onBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        val world = block.world
        val material = block.type

        val uuid = plugin.blockOwner[material]

        if (uuid == null) {
            plugin.setBlockOwner(material, player.uniqueId)
            plugin.server.sendMessage(
                text().content("${player.name}님이 ${material.name}을 차지하였습니다!").color(NamedTextColor.RED).build()
            )

            world.spawn(block.location.clone().add(Vector(0.5, 0.5, 0.5)), Firework::class.java).apply {
                fireworkMeta = fireworkMeta.apply {
                    addEffect(
                        FireworkEffect.builder().withColor(
                            Color.fromRGB((0x0..0xffffff).random()),
                            Color.fromRGB((0x0..0xffffff).random()),
                            Color.fromRGB((0x0..0xffffff).random())
                        ).with(FireworkEffect.Type.STAR).build()
                    )
                    addScoreboardTag("피해x")
                }
                detonate()
            }
        } else {
            if (uuid != player.uniqueId) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun disableFireworkDamage(event: EntityDamageByEntityEvent) {
        if ("피해x" in event.damager.scoreboardTags) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage(text(""))

        val player = event.player
        val uuid = player.uniqueId

        val iterator = plugin.blockOwner.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()

            if (entry.value == uuid) {
                plugin.setBlockOwner(entry.key, null)

                plugin.server.sendMessage(
                    text().content(
                        "${player.name}이 사망하여 ${entry.key.name}이 풀렸습니다!"
                    ).color(NamedTextColor.GREEN).build()
                )
            }
        }
    }
}
