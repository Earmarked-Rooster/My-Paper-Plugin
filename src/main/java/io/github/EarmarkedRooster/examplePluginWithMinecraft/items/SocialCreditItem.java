package io.github.EarmarkedRooster.examplePluginWithMinecraft.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class SocialCreditItem {

    public static ItemStack createSocialCreditItem() {
        ItemStack item = new ItemStack(Material.BAMBOO);
        // gets the metadata from the newly created item
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("BLANK"));
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "social_credit_item");
        // creates an entry of this item to be saved within the PDC
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
        item.setItemMeta(meta);
        return item;
    }
}
