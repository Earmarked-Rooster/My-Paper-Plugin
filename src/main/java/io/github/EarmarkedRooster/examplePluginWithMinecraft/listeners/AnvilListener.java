package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.enchantments.WheatFieldsEnchantment;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.menus.SocialCreditShopMenu;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListener implements Listener {
// TO BE DELETED - ENCHANTMENTS ARE NOT CUSTOMIZED THIS WAY VERSIONS 1.21 +
    private final SocialCreditShopMenu socialCreditShopMenu;

    public AnvilListener(SocialCreditShopMenu socialCreditShopMenu) {
        this.socialCreditShopMenu = socialCreditShopMenu;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack[] ingredients = event.getInventory().getContents();
        if (ingredients.length < 2) return;
        ItemStack base = ingredients[0];
        ItemStack addition = ingredients[1];

        if (base != null && base.getType().toString().endsWith("_HOE") && addition != null && addition.getType() == Material.ENCHANTED_BOOK) {
            if (WheatFieldsEnchantment.hasEnchantment(addition)) {
                ItemStack result = base.clone();
                WheatFieldsEnchantment.applyEnchantment(result);
                event.setResult(result);
            }
        }
    }
}
