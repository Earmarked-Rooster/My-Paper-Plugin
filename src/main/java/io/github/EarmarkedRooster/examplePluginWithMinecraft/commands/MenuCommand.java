package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.MenuManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;
// annotation to prevent any warnings from popping up involving the use of new API that may be changed in the future
@SuppressWarnings("UnstableApiUsage")
public class MenuCommand {

    private final MenuManager menuManager;

    public MenuCommand(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("menu")
                .executes(context -> {
                    if (context.getSource().getSender() instanceof Player) {
                        Player player = (Player) context.getSource().getSender();
                        menuManager.openMainMenu(player);
                    }
                    return Command.SINGLE_SUCCESS;
                });
    }
}
