package com.dnyferguson.momentorankvouchers.utils;

import org.bukkit.ChatColor;

public class Chat {
    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
