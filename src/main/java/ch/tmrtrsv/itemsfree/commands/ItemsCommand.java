package ch.tmrtrsv.itemsfree.commands;

import ch.tmrtrsv.itemsfree.ItemsFree;
import ch.tmrtrsv.itemsfree.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ItemsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.usage")));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("items.give")) {
                sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.no-permissions")));
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.give-usage")));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.target-null")));
                return true;
            }

            String itemKey = args[2].toLowerCase();
            int amount = args.length >= 4 ? Integer.parseInt(args[3]) : 1;

            ItemStack item = createItem(itemKey, amount);
            if (item == null) {
                sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.item-null")));
                return true;
            }

            target.getInventory().addItem(item);
            sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.item-given")
                    .replace("%player%", target.getName())
                    .replace("%amount%", String.valueOf(amount))
                    .replace("%item%", itemKey)));
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("items.reload")) {
                sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.no-permissions")));
                return true;
            }
            ItemsFree.getInstance().reloadConfig();
            sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.config-reloaded")));
        } else {
            sender.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.usage")));
        }

        return true;
    }

    private ItemStack createItem(String key, int amount) {
        if (!ItemsFree.getInstance().getConfig().contains("items." + key)) return null;

        String name = Utils.color(ItemsFree.getInstance().getConfig().getString("items." + key + ".name"));
        List<String> lore = Utils.color(ItemsFree.getInstance().getConfig().getStringList("items." + key + ".lore"));
        Material material = Material.valueOf(ItemsFree.getInstance().getConfig().getString("items." + key + ".material"));

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        meta.getPersistentDataContainer().set(ItemsFree.getInstance().getKey(key), PersistentDataType.STRING, key);
        item.setItemMeta(meta);
        return item;
    }
}