package com.dnyferguson.momentorankvouchers.listeners;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class RespawnListener implements Listener {
    private MomentoRankVouchers plugin;

    public RespawnListener(MomentoRankVouchers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        if (plugin.getItemsToGiveBackup().containsKey(player.getUniqueId())) {
            for (ItemStack item : plugin.getItemsToGiveBackup().get(player.getUniqueId())) {
                player.getInventory().addItem(item);
            }
            plugin.getItemsToGiveBackup().remove(player.getUniqueId());
        }
    }
}
