package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FieldsEnchantmentListener implements Listener {

    private final ExamplePluginWithMinecraft plugin;
    private final SocialCreditDatabase socialCreditDatabase;
    private final Map<UUID, Integer> wheatHarvested = new HashMap<>();

    public FieldsEnchantmentListener(ExamplePluginWithMinecraft plugin, SocialCreditDatabase socialCreditDatabase) {
        this.plugin = plugin;
        this.socialCreditDatabase = socialCreditDatabase;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        // checks to see if item in main hand of player has the enchantment called Fields
        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:fields");
        Enchantment fieldsEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));

        if (fieldsEnchantment != null && item.containsEnchantment(fieldsEnchantment)) {
            if (event.getBlock().getType() == Material.WHEAT) {
                // checks to see if the age of the wheat is at the maximum possible, then rewards the player 
                Ageable wheat = (Ageable) event.getBlock().getBlockData();
                if (wheat.getAge() == wheat.getMaximumAge()) {
                    UUID playerUUID = player.getUniqueId();
                    int harvested = wheatHarvested.getOrDefault(playerUUID, 0) + 1;
                    wheatHarvested.put(playerUUID, harvested);

                    if (harvested >= 100) {
                        // rewards the player with social credit if amount harvested is at the optimal amount
                        Map<UUID, Integer> socialCreditScores = socialCreditDatabase.getSocialCreditScores();
                        int currentScore = socialCreditScores.getOrDefault(playerUUID, 0);
                        int newScore = currentScore + 1;
                        socialCreditScores.put(playerUUID, newScore);
                        wheatHarvested.put(playerUUID, 0);
                    }
                }
            }
        }
    }
}
