package com.dnyferguson.momentorankvouchers.listeners;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;
import com.dnyferguson.momentorankvouchers.objects.Rankup;
import com.dnyferguson.momentorankvouchers.utils.Chat;
import com.dnyferguson.momentorankvouchers.utils.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.Timestamp;
import java.time.Instant;

public class VoucherRedeemListener implements Listener {
    private MomentoRankVouchers plugin;

    public VoucherRedeemListener(MomentoRankVouchers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUseVoucher(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = e.getItem();
        if (item == null || !item.getType().equals(Material.PAPER)) {
            return;
        }

        Player player = e.getPlayer();

        Rankup rankup = null;
        if (!NBT.hasRanks(item)) {
            return;
        }

        rankup = NBT.getRanks(item);

        if (player.hasPermission("group." + rankup.getRankTo().toLowerCase())) {
            player.sendMessage(Chat.format("&cYou already have that rank."));
            return;
        }

        if (!player.hasPermission("group." + rankup.getRankFrom().toLowerCase())) {
            player.sendMessage(Chat.format("&cYou must have the rank &7" + rankup.getRankFromName() + " &cto redeem this voucher!"));
            return;
        }

        player.getInventory().remove(item);
        plugin.getSql().executeStatementAsync("UPDATE `vouchers` SET `used`='1' WHERE `unique` = '" + rankup.getUnique() + "'");
        player.sendMessage(Chat.format("&aYou have successfully redeemed a rankup voucher and have advanced to &f" + rankup.getRankToName() + "&a!"));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent remove " + rankup.getRankFrom());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add " + rankup.getRankTo());

        System.out.println("[MomentoRankVouchers] User " + player.getName() + " has successfully redeemed a voucher to go from rank " + rankup.getRankFrom() + " -> " + rankup.getRankTo() + "!");
        plugin.getLogFile().log("[" + Timestamp.from(Instant.now()) + "]" + " User " + player.getName() + " has successfully redeemed a voucher to go from rank " + rankup.getRankFrom() + " -> " + rankup.getRankTo() + "!");
    }
}
