package com.dnyferguson.momentorankvouchers.nbt;

import com.dnyferguson.momentorankvouchers.objects.Rankup;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMS_1_8 {
    public static ItemStack setRanks(ItemStack itemStack, String rankFrom, String rankTo, String rankFromName, String rankToName, String unique) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }
        nmsItemCompound.set("rankFrom", new NBTTagString(rankFrom));
        nmsItemCompound.set("rankFromName", new NBTTagString(rankFromName));
        nmsItemCompound.set("rankTo", new NBTTagString(rankTo));
        nmsItemCompound.set("rankToName", new NBTTagString(rankToName));
        nmsItemCompound.set("voucherUnique", new NBTTagString(unique));
        nmsItem.setTag(nmsItemCompound);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public static Rankup getRanks(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return null;
        }

        String rankFrom = nmsItemCompound.getString("rankFrom");
        String rankFromName = nmsItemCompound.getString("rankFromName");
        String rankTo = nmsItemCompound.getString("rankTo");
        String rankToName = nmsItemCompound.getString("rankToName");
        String unique = nmsItemCompound.getString("voucherUnique");

        return new Rankup(rankFrom, rankFromName, rankTo, rankToName, unique);
    }

    public static boolean hasRanks(ItemStack itemStack) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nmsItemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
        if (nmsItemCompound == null) {
            return false;
        }

        try {
            String rankFrom = nmsItemCompound.getString("rankFrom");
            String rankFromName = nmsItemCompound.getString("rankFromName");
            String rankTo = nmsItemCompound.getString("rankTo");
            String rankToName = nmsItemCompound.getString("rankToName");
            String unique = nmsItemCompound.getString("voucherUnique");

            return !rankFrom.equals("") && !rankFromName.equals("") && !rankTo.equals("") && !rankToName.equals("") && !unique.equals("");
        } catch (Exception ignore) {
            return false;
        }
    }
}
