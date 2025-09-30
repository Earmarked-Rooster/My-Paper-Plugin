package io.github.EarmarkedRooster.examplePluginWithMinecraft;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.registry.data.EnchantmentRegistryEntry;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.EnchantmentKeys;
import io.papermc.paper.registry.keys.tags.ItemTypeTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.EquipmentSlotGroup;

public class ExamplePluginWithMinecraftBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(BootstrapContext context) {
        // Register a new handler for the compose lifecycle event on the enchantment registry
        context.getLifecycleManager().registerEventHandler(RegistryEvents.ENCHANTMENT.compose().newHandler(event -> {
            event.registry().register(
                    // The key of the registry
                    // Plugins should use their own namespace
                    EnchantmentKeys.create(Key.key("examplepluginwithminecraft:pointy")),
                    b -> b.description(Component.text("Pointy"))
                // what items can use the enchantment
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.SWORDS))
                // how much it costs on an anvil
                            .anvilCost(1)
                // the maximum possible cost to enchant said item
                            .maxLevel(25)
                // its chance to be found on an enchantment table
                                                        .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                            .activeSlots(EquipmentSlotGroup.MAINHAND)

            );
            event.registry().register(
                    // we make a new key for the Fields Enchantment
                    EnchantmentKeys.create(Key.key("examplepluginwithminecraft:fields")),
                    b -> b.description(Component.text("Fields"))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.HOES))
                            .anvilCost(1)
                            .maxLevel(5)
                                                        .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                            .activeSlots(EquipmentSlotGroup.MAINHAND)
            );
            event.registry().register(
                // new key likewise for the vein miner enchantment
                    EnchantmentKeys.create(Key.key("examplepluginwithminecraft:vein_miner")),
                    b -> b.description(Component.text("Vein Miner"))
                            .supportedItems(event.getOrCreateTag(ItemTypeTagKeys.PICKAXES))
                            .anvilCost(1)
                            .maxLevel(1)
                                                        .weight(1)
                            .minimumCost(EnchantmentRegistryEntry.EnchantmentCost.of(1, 1))
                            .maximumCost(EnchantmentRegistryEntry.EnchantmentCost.of(3, 1))
                            .activeSlots(EquipmentSlotGroup.MAINHAND)
            );
        }));
    }


}
