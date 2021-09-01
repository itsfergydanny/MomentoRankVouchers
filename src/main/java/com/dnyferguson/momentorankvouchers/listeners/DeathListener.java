package com.dnyferguson.momentorankvouchers.listeners;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;
import com.dnyferguson.momentorankvouchers.utils.NBT;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener {
    private MomentoRankVouchers plugin;

    public DeathListener(MomentoRankVouchers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerdeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        List<ItemStack> itemsToKeep = new ArrayList<>();
        for (ItemStack item : e.getDrops()) {
            if (NBT.hasRanks(item)) {
                itemsToKeep.add(item);
            }
        }

        e.getDrops().removeAll(itemsToKeep);

        if (!itemsToKeep.isEmpty()) {
            plugin.getItemsToGiveBackup().put(player.getUniqueId(), itemsToKeep);
        }
    }
}
