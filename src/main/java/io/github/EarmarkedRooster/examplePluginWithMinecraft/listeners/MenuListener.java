package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.MenuManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuListener implements Listener {

    private final MenuManager menuManager;

    public MenuListener(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text("Menu"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
                    Player player = (Player) event.getWhoClicked();
                    menuManager.openKillMenu(player);
                } else if (event.getCurrentItem().getType() == Material.BARRIER) {
                    event.getWhoClicked().closeInventory();
                }
            }
        } else if (event.getView().title().equals(Component.text("Kill Menu"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                org.bukkit.inventory.meta.SkullMeta skullMeta = (org.bukkit.inventory.meta.SkullMeta) event.getCurrentItem().getItemMeta();
                if (skullMeta != null && skullMeta.getOwningPlayer() != null) {
                    org.bukkit.entity.Player playerToKill = skullMeta.getOwningPlayer().getPlayer();
                    if (playerToKill != null) {
                        playerToKill.setHealth(0);
                        event.getWhoClicked().closeInventory();
                    }
                }
            }
        }
    }
}
