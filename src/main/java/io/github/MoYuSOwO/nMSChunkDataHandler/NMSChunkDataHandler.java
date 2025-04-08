package io.github.MoYuSOwO.nMSChunkDataHandler;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMSChunkDataHandler extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        serverPlayer.connection.connection.channel.pipeline().addBefore(
                "packet_handler", "NMS_custom_handler", new NMSBlockHandler(serverPlayer)
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
