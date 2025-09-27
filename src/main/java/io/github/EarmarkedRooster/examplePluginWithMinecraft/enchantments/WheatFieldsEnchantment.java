package io.github.EarmarkedRooster.examplePluginWithMinecraft.enchantments;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class WheatFieldsEnchantment {

    private static final NamespacedKey ENCHANTMENT_KEY = new NamespacedKey("examplepluginwithminecraft", "wheat_fields");

    public static ItemStack applyEnchantment(ItemStack item) {
        if (item != null && (item.getType().toString().endsWith("_HOE") || item.getType() == Material.BOOK)) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.getPersistentDataContainer().set(ENCHANTMENT_KEY, PersistentDataType.BYTE, (byte) 1);
                List<Component> lore = meta.lore();
                if (lore == null) {
                    lore = new ArrayList<>();
                }
                lore.add(Component.text("Wheat Fields"));
                meta.lore(lore);
                item.setItemMeta(meta);
            }
        }
        return item;
    }

    public static boolean hasEnchantment(ItemStack item) {
        if (item == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(ENCHANTMENT_KEY, PersistentDataType.BYTE);
    }
}
