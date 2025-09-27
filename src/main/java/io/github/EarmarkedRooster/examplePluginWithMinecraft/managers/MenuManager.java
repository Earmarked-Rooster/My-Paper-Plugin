package io.github.EarmarkedRooster.examplePluginWithMinecraft.managers;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class MenuManager {

    public MenuManager(SocialCreditDatabase socialCreditDatabase) {
    }

    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, Component.text("Menu"));

        ItemStack killItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta killMeta = killItem.getItemMeta();
        killMeta.displayName(Component.text("Kill...").color(RED).decorate(BOLD));
        killMeta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
        killItem.setItemMeta(killMeta);

        ItemStack barrierItem = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrierItem.getItemMeta();
        barrierMeta.displayName(Component.text("Exit"));
        barrierItem.setItemMeta(barrierMeta);

        menu.setItem(0, killItem);
        menu.setItem(1, new ItemStack(Material.GOLDEN_APPLE));
        menu.setItem(8, barrierItem);
        player.openInventory(menu);
    }

    public void openKillMenu(Player player) {
        int onlinePlayerCount = Bukkit.getOnlinePlayers().size();
        int inventorySize = (int) (Math.ceil(onlinePlayerCount / 9.0) * 9);
        if (inventorySize == 0) {
            inventorySize = 9;
        }

        Inventory killMenu = Bukkit.createInventory(null, inventorySize, Component.text("Kill Menu"));

        for (Player p : Bukkit.getOnlinePlayers()) {
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            org.bukkit.inventory.meta.SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
            skullMeta.setOwningPlayer(p);
            skullMeta.displayName(Component.text(p.getName()));
            playerHead.setItemMeta(skullMeta);
            killMenu.addItem(playerHead);
        }

        player.openInventory(killMenu);
    }
}
