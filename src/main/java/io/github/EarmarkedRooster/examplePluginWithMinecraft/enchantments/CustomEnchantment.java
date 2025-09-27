package io.github.EarmarkedRooster.examplePluginWithMinecraft.enchantments;

import org.bukkit.enchantments.Enchantment;

public interface CustomEnchantment {
    void register();
    Enchantment getEnchantment();
}
