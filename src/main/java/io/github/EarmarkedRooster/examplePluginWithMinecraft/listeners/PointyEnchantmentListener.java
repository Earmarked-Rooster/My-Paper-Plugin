package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PointyEnchantmentListener implements Listener {

    private final ExamplePluginWithMinecraft plugin;

    public PointyEnchantmentListener(ExamplePluginWithMinecraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();
            // we check to see if the item that is currently being used by the player has the pointy enchantment
            Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
            NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:pointy");
            Enchantment pointyEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));

            if (pointyEnchantment != null && item.containsEnchantment(pointyEnchantment)) {
                Entity entity = event.getEntity();
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    // we check to see if the entity is wearing armor 
                    if (livingEntity.getEquipment() != null && (livingEntity.getEquipment().getHelmet() != null || livingEntity.getEquipment().getChestplate() != null || livingEntity.getEquipment().getLeggings() != null || livingEntity.getEquipment().getBoots() != null)) {
                        int enchantmentLevel = item.getEnchantmentLevel(pointyEnchantment);
                        double damageIncrease = 1.0 + (0.25 * enchantmentLevel);
                        double newDamage = event.getDamage() * damageIncrease;
                        event.setDamage(newDamage);
                    }
                }
            }
        }
    }
}
