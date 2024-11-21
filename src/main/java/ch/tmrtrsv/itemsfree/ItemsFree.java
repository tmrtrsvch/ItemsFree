package ch.tmrtrsv.itemsfree;

import ch.tmrtrsv.itemsfree.commands.ItemsCommand;
import ch.tmrtrsv.itemsfree.listeners.AppleListener;
import ch.tmrtrsv.itemsfree.listeners.FeatherListener;
import ch.tmrtrsv.itemsfree.listeners.MagmaListener;
import ch.tmrtrsv.itemsfree.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemsFree extends JavaPlugin {
    private static ItemsFree instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        sendCredit();

        getCommand("items").setExecutor(new ItemsCommand());
        getServer().getPluginManager().registerEvents(new AppleListener(), this);
        getServer().getPluginManager().registerEvents(new FeatherListener(), this);
        getServer().getPluginManager().registerEvents(new MagmaListener(), this);
    }

    @Override
    public void onDisable() {
        sendCredit();
    }

    private void sendCredit() {
        Bukkit.getConsoleSender().sendMessage(Utils.color(""));
        Bukkit.getConsoleSender().sendMessage(Utils.color("&f &#9F08FBI&#9B07FBt&#9707FCe&#9306FCm&#8F06FCs&#8B05FDF&#8704FDr&#8304FDe&#7F03FDe &#7702FEv&#7301FE1&#6F01FF.&#6B00FF0"));
        Bukkit.getConsoleSender().sendMessage(Utils.color("&f Автор: &#FB3908Т&#FC2B06и&#FD1D04м&#FE0E02у&#FF0000р"));
        Bukkit.getConsoleSender().sendMessage(Utils.color("&f Телеграм: &#008DFF@&#0086FFt&#007FFFm&#0078FFr&#0071FFt&#006BFFr&#0064FFs&#005DFFv&#0056FFc&#004FFFh"));
        Bukkit.getConsoleSender().sendMessage(Utils.color(""));
    }

    public static ItemsFree getInstance() {
        return instance;
    }

    public NamespacedKey getKey(String key) {
        return new NamespacedKey(this, key);
    }
}