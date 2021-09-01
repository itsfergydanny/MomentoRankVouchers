package com.dnyferguson.momentorankvouchers.utils;

import com.dnyferguson.momentorankvouchers.objects.Rankup;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class NBT {
    public static ItemStack setRanks(ItemStack itemStack, String rankFrom, String rankTo, String rankFromName, String rankToName, String unique) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setString("rankFrom", rankFrom);
        nbtItem.setString("rankFromName", rankFromName);
        nbtItem.setString("rankTo", rankTo);
        nbtItem.setString("rankToName", rankToName);
        nbtItem.setString("voucherUnique", unique);

        return nbtItem.getItem();
    }

    public static Rankup getRanks(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        String rankFrom = nbtItem.getString("rankFrom");
        String rankFromName = nbtItem.getString("rankFromName");
        String rankTo = nbtItem.getString("rankTo");
        String rankToName = nbtItem.getString("rankToName");
        String unique = nbtItem.getString("voucherUnique");

        return new Rankup(rankFrom, rankFromName, rankTo, rankToName, unique);
    }

    public static boolean hasRanks(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);

        return nbtItem.hasKey("rankFrom") && nbtItem.hasKey("rankFromName") && nbtItem.hasKey("rankTo") && nbtItem.hasKey("rankToName") && nbtItem.hasKey("voucherUnique");
    }
}
