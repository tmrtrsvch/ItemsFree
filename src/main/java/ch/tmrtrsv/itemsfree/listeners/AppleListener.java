package ch.tmrtrsv.itemsfree.listeners;

import ch.tmrtrsv.itemsfree.ItemsFree;
import ch.tmrtrsv.itemsfree.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class AppleListener implements Listener {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!event.getItem().hasItemMeta()) return;

        if (!event.getItem().getItemMeta().getPersistentDataContainer()
                .has(ItemsFree.getInstance().getKey("apple"), PersistentDataType.STRING)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        long delay = ItemsFree.getInstance().getConfig().getLong("items.apple.delay") * 1000;

        if (cooldowns.containsKey(player.getUniqueId()) && currentTime - cooldowns.get(player.getUniqueId()) < delay) {
            long remaining = (delay - (currentTime - cooldowns.get(player.getUniqueId()))) / 1000;
            player.sendMessage(Utils.color(ItemsFree.getInstance().getConfig().getString("messages.item-delay")
                    .replace("%delay%", String.valueOf(remaining))));
            event.setCancelled(true);
            return;
        }

        cooldowns.put(player.getUniqueId(), currentTime);

        for (String effectKey : ItemsFree.getInstance().getConfig().getConfigurationSection("items.apple.effects").getKeys(false)) {
            PotionEffectType effectType = PotionEffectType.getByName(effectKey);
            if (effectType != null) {
                String[] data = ItemsFree.getInstance().getConfig().getString("items.apple.effects." + effectKey).split(", ");
                int level = Integer.parseInt(data[0]);
                int duration = Integer.parseInt(data[1]) * 20;
                player.addPotionEffect(new PotionEffect(effectType, duration, level - 1));
            }
        }
    }
}