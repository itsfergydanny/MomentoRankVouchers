package com.dnyferguson.momentorankvouchers.commands;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;
import com.dnyferguson.momentorankvouchers.mysql.FindResultCallback;
import com.dnyferguson.momentorankvouchers.utils.Chat;
import com.dnyferguson.momentorankvouchers.utils.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoucherCommand implements CommandExecutor {
    private MomentoRankVouchers plugin;

    public VoucherCommand(MomentoRankVouchers plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*

        rankFromName and rankToName are just the ranks without the server names to look better on the notes

        /voucher give <player/uuid> <rankfrom> <rankto> <rankFromName> <rankToName>

         */
        if (args.length == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("&eRank Vouchers:\n" +
                    "&fVouchers are used to obtain rank upgrades. They are purchasable from the store and give the player using it the ability to upgrade their rank to the next one" +
                    " if they have the required rank.\n \n" +
                    "&eCommands:\n" +
                    "&f/voucher verify <id> &7(Verify if a voucher with a certain id is valid or already used)\n");
            if (sender.hasPermission("momentorankvouchers.admin")) {
                msg.append("&c/voucher give <player/uuid> <rankFrom> <rankTo> <rankFromName> <rankToName> &7(Give a user a rank voucher)\n");
            }
            sender.sendMessage(Chat.format(msg.toString()));
            return true;
        }

        String subcommand = args[0].toLowerCase();
        if (subcommand.equals("verify")) {
            if (args.length != 2) {
                sender.sendMessage(Chat.format("&cInvalid usage. Command: &7/voucher verify <id>&c."));
                return true;
            }

            String id = args[1];
            plugin.getSql().getResultAsync("SELECT * FROM `vouchers` WHERE `unique` = '" + id + "'", new FindResultCallback() {
                @Override
                public void onQueryDone(ResultSet result) throws SQLException {
                    if (result.next()) {
                        boolean isUsed = result.getBoolean("used");
                        if (isUsed) {
                            sender.sendMessage(Chat.format("&cThat voucher has already been claimed."));
                            return;
                        }

                        sender.sendMessage(Chat.format("&aThat voucher is still valid!"));
                        return;
                    }
                    sender.sendMessage(Chat.format("&cNo voucher found by that id."));
                }
            });
            return true;
        }

        if (subcommand.equals("give") && sender.hasPermission("momentorankvouchers.admin")) {
            if (args.length != 6) {
                sender.sendMessage(Chat.format("&cInvalid usage. Command: &7/voucher verify <id>&c."));
                return true;
            }

            String player = args[1];
            String rankFrom = args[2];
            String rankTo = args[3];
            String rankFromName = args[4];
            String rankToName = args[5];

            Player target;
            if (player.contains("-")) {
                target = Bukkit.getPlayer(UUID.fromString(player));
            } else {
                target = Bukkit.getPlayer(player);
            }

            if (target == null) {
                sender.sendMessage(Chat.format("&cPlayer not found."));
                return true;
            }

            String unique = UUID.randomUUID().toString().split("-")[0];
            createNote(unique, target, rankFrom, rankTo, rankFromName, rankToName, sender);
            return true;
        }

        return false;
    }

    public void createNote(String unique, Player target, String rankFrom, String rankTo, String rankFromName, String rankToName, CommandSender sender) {
        plugin.getSql().getResultAsync("SELECT * FROM `vouchers` WHERE `unique` = '" + unique + "'", new FindResultCallback() {
            @Override
            public void onQueryDone(ResultSet result) throws SQLException {
                if (result.next()) {
                    sender.sendMessage(Chat.format("&cUnique code was already in use, regenrating!"));
                    String newUnique = UUID.randomUUID().toString().split("-")[0];
                    createNote(newUnique, target, rankFrom, rankTo, rankFromName, rankToName, sender);
                    return;
                }

                plugin.getSql().executeStatementAsync("INSERT INTO `vouchers` (`id`, `unique`, `ign`, `uuid`, `rankFrom`, `rankTo`, `rankFromName`, `rankToName`, `used`, `createdAt`) VALUES" +
                        " (NULL, '" + unique + "', '" + target.getName() + "', '" + target.getUniqueId() + "', '" + rankFrom + "', '" + rankTo + "', '" + rankFromName + "', '" + rankToName + "', '0', current_timestamp())");

                ItemStack note = new ItemStack(Material.PAPER, 1);
                ItemMeta noteMeta = note.getItemMeta();
                noteMeta.setDisplayName(Chat.format("&eRankup Voucher"));

                List<String> lore = new ArrayList<>();
                lore.add(Chat.format("&7Right-Click this to rankup from &f" + rankFromName + " &7to &f" + rankToName + "&7!"));
                lore.add(" ");
                lore.add(Chat.format("&eRequired Rank: &f" + rankFromName));
                lore.add(" ");
                lore.add(Chat.format("&aID: " + unique));
                lore.add(" ");
                lore.add(Chat.format("&cMake sure the note is active using &7/voucher verify <id>&c."));

                noteMeta.setLore(lore);
                note.setItemMeta(noteMeta);

                note = NBT.setRanks(note, rankFrom, rankTo, rankFromName, rankToName, unique);

                target.getInventory().addItem(note);

                System.out.println("[MomentoRankVouchers] " + sender.getName() + " has successfully given a rank note (" + rankFrom + "->" + rankTo + ") to " + target.getName() + "(" + target.getUniqueId() + ").");
                plugin.getLogFile().log("[" + Timestamp.from(Instant.now()) + "] " + sender.getName() + " has successfully given a rank note (" + rankFrom + "->" + rankTo + ") to " + target.getName() + "(" + target.getUniqueId() + ").");
                sender.sendMessage(Chat.format("&ayou have successfully given a rank note (&7" + rankFrom + "&f->&7" + rankTo + "&a) to " + target.getName() + " (" + target.getUniqueId() + ")."));

                target.sendMessage(Chat.format("&aYou have received a rank note!"));
            }
        });
    }
}
