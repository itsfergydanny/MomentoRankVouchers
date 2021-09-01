package com.dnyferguson.momentorankvouchers;

import com.dnyferguson.momentorankvouchers.commands.VoucherCommand;
import com.dnyferguson.momentorankvouchers.listeners.DeathListener;
import com.dnyferguson.momentorankvouchers.listeners.RespawnListener;
import com.dnyferguson.momentorankvouchers.listeners.VoucherRedeemListener;
import com.dnyferguson.momentorankvouchers.mysql.MySQL;
import com.dnyferguson.momentorankvouchers.utils.LogFile;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MomentoRankVouchers extends JavaPlugin {
    private MySQL sql;
    private Map<UUID, List<ItemStack>> itemsToGiveBackup = new HashMap<>();
    private LogFile logFile;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        sql = new MySQL(this);
        logFile = new LogFile(this);

        getCommand("voucher").setExecutor(new VoucherCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new VoucherRedeemListener(this), this);
        pm.registerEvents(new DeathListener(this), this);
        pm.registerEvents(new RespawnListener(this), this);
    }

    @Override
    public void onDisable() {
        if (sql != null) {
            sql.close();
        }
        logFile.close();
    }

    public MySQL getSql() {
        return sql;
    }

    public Map<UUID, List<ItemStack>> getItemsToGiveBackup() {
        return itemsToGiveBackup;
    }

    public LogFile getLogFile() {
        return logFile;
    }
}
