package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class VeinMinerEnchantmentListener implements Listener {

    private final ExamplePluginWithMinecraft plugin;

    public VeinMinerEnchantmentListener(ExamplePluginWithMinecraft plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        // checks to see if the item has the enchantment vein miner by calling the Registry available through minecraft
        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:vein_miner");
        Enchantment veinMinerEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));

        if (veinMinerEnchantment != null && item.containsEnchantment(veinMinerEnchantment)) {
            Block start = event.getBlock();
            Material type = start.getType();
            if (isOre(type)) {
                // creates a list of blocks that are adjacent to the broken block that are classified as ore
                List<Block> blocksToBreak = new ArrayList<>();
                findVein(start, blocksToBreak);
                for (Block block : blocksToBreak) {
                    block.breakNaturally(item);
                }
            }
        }
    }

    private void findVein(Block start, List<Block> blocks) {
        if (blocks.size() >= 200) { // Limit the number of blocks to break to prevent lag
            return;
        }
        // checks all the immediate blocks next to the broken block on all planes
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    // gets the relative location of the broken block
                    Block relative = start.getRelative(x, y, z);
                    if (relative.getType() == start.getType() && !blocks.contains(relative)) {
                        blocks.add(relative);
                        findVein(relative, blocks);
                    }
                }
            }
        }
    }
    // checks to see if the given material (block!) is an ore or not
    private boolean isOre(Material material) {
        return material.toString().endsWith("_ORE");
    }
}
