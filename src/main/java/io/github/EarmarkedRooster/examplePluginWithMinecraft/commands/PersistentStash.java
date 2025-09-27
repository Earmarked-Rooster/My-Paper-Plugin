package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;

import java.util.HashMap;
import java.util.Map;

public class PersistentStash implements Listener {

    // create a map to use for each individual player to hold their stash in
    private static final Map<Player, InventoryView> VIEWS = new HashMap<>();

    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("persistent")
                .executes(ctx -> {
                    // if the source of this command does not contain any player information, return nothing but a 0 & do not execute command
                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                        return 0;
                    }

                    // attempts to see if stash of player exists
                    InventoryView view = VIEWS.get(player);

                    // if not, creates one here:
                    if (view == null) {
                        view = MenuType.GENERIC_9X6.builder()
                                .title(Component.text(player.getName() + "'s stash", NamedTextColor.DARK_RED))
                                .build(player);

                        // store unique stash into map
                        VIEWS.put(player, view);
                    }

                    // because InventoryView is directly bound to the player who called the command, no specifics are needed
                    // to open the map & the player can just open it
                    view.open();
                    return Command.SINGLE_SUCCESS;
                }).build(); 
    }

    // quit event:
    // remove entry from the map so that no errors & phantom players show up, but save the data somewhere (called top inventory) so it persists across server restarts
    @EventHandler
    void onPlayerQuit(PlayerQuitEvent event) {
        InventoryView view = VIEWS.remove(event.getPlayer());
        if (view != null) {
            Inventory topInventory = view.getTopInventory();
            // Save the contents of the inventory to a file or database.
        }
    }
}


