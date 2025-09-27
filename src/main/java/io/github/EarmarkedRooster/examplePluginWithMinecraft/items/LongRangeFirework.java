package io.github.EarmarkedRooster.examplePluginWithMinecraft.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;

public class LongRangeFirework {

    public static ItemStack createLongRangeFirework() {
        ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Long Range Firework"));
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "long_range_firework");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");

        FireworkMeta fireworkMeta = (FireworkMeta) meta;
        fireworkMeta.setPower(200);

        for (int i = 0; i < 30; i++) {
            FireworkEffect effect = FireworkEffect.builder()
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withColor(Color.WHITE, Color.RED)
                    .withFade(Color.ORANGE)
                    .trail(true)
                    .build();
            fireworkMeta.addEffect(effect);
        }

        item.setItemMeta(fireworkMeta);
        return item;
    }

    public static FireworkMeta getFireworkMeta() {
        return (FireworkMeta) createLongRangeFirework().getItemMeta();
    }
}