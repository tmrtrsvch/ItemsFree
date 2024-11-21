package ch.tmrtrsv.itemsfree.listeners;

import ch.tmrtrsv.itemsfree.ItemsFree;
import ch.tmrtrsv.itemsfree.ItemsFree;
import ch.tmrtrsv.itemsfree.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class FeatherListener implements Listener {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;

        if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer()
                .has(ItemsFree.getInstance().getKey("feather"), PersistentDataType.STRING)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long delay = ItemsFree.getInstance().getConfig().getLong("items.feather.delay") * 1000;

        if (cooldowns.containsKey(player.getUniqueId()) && currentTime - cooldowns.get(player.getUniqueId()) < delay) {
            long remaining = (delay - (currentTime - cooldowns.get(player.getUniqueId()))) / 1000;
            player.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.item-delay")
                    .replace("%delay%", String.valueOf(remaining))));
            event.setCancelled(true);
            return;
        }

        cooldowns.put(player.getUniqueId(), currentTime);

        Vector vector = player.getLocation().getDirection();
        vector.setY(1);
        player.setVelocity(vector);

        player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
    }
}