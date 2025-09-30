package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StashWithDatabaseTest implements Listener {

    private final JavaPlugin plugin;
    private static final Map<UUID, Inventory> STASHES = new HashMap<>();

    // Constructor updated to match the new class name
    public StashWithDatabaseTest(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("testpersistent")
                .executes(ctx -> {
                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                        ctx.getSource().getSender().sendMessage(Component.text("This command can only be run by a player.", NamedTextColor.RED));
                        return 0;
                    }
                    // still loads the stash of the player if said player is not loaded in the server, but the file containing their UUID is 
                    Inventory stash = STASHES.computeIfAbsent(player.getUniqueId(), uuid -> loadStash(player));
                    player.openInventory(stash);
                    return Command.SINGLE_SUCCESS;
                }).build();
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Inventory stash = STASHES.get(playerUUID);
        if (stash != null) {
            saveStash(playerUUID, stash);
            STASHES.remove(playerUUID);
        }
    }

    public void saveStash(UUID playerUUID, Inventory inventory) {
        // creates this file only if it doesn't already exist to prevent duplicates
        File playerFile = new File(plugin.getDataFolder(), "stashes/" + playerUUID + ".yml");
        FileConfiguration stashConfig = YamlConfiguration.loadConfiguration(playerFile);

        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item != null) {
                // serializes the data in each players stash as the item name + the location in the inventory it is found in
                byte[] itemBytes = item.serializeAsBytes();
                String base64Item = Base64.getEncoder().encodeToString(itemBytes);
                stashConfig.set("inventory." + i, base64Item);
            } else {
                stashConfig.set("inventory." + i, null);
            }
        }

        try {
            stashConfig.save(playerFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save stash for " + playerUUID);
            e.printStackTrace();
        }
    }
    // creation of the stash found in the game
    public Inventory loadStash(Player player) {
        Inventory stash = Bukkit.createInventory(player, 54, Component.text(player.getName() + "'s stash", NamedTextColor.DARK_RED));
        File playerFile = new File(plugin.getDataFolder(), "stashes/" + player.getUniqueId() + ".yml");

        if (!playerFile.exists()) {
            return stash;
        }

        FileConfiguration stashConfig = YamlConfiguration.loadConfiguration(playerFile);
        if (stashConfig.isConfigurationSection("inventory")) {
            for (String key : stashConfig.getConfigurationSection("inventory").getKeys(false)) {
                try {
                    int slot = Integer.parseInt(key);
                    // reloads all the items in the file to be used in the game when run
                    String base64Item = stashConfig.getString("inventory." + key);
                    byte[] itemBytes = Base64.getDecoder().decode(base64Item);
                    ItemStack item = ItemStack.deserializeBytes(itemBytes);
                    stash.setItem(slot, item);
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to load an item from stash for " + player.getName());
                }
            }
        }
        return stash;
    }

    public void saveAllStashes() {
        STASHES.forEach(this::saveStash);
        STASHES.clear();
        plugin.getLogger().info("All persistent stashes have been saved.");
    }
}
