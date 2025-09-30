package io.github.EarmarkedRooster.examplePluginWithMinecraft.enchantments;

import org.bukkit.enchantments.Enchantment;
// TO BE DELETED - ENCHANTMENTS ARE NOT CUSTOMIZED THIS WAY VERSIONS 1.21 +
public interface CustomEnchantment {
    void register();
    Enchantment getEnchantment();
}
