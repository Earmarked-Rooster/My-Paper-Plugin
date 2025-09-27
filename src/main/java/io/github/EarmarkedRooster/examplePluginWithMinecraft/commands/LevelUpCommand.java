package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.events.PlayerLevelUpEvent;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelUpCommand {
    /*
    create our command with LiteralArgumentBuilder to later turn into a
    LiteralCommandNode once built use .build() at the end of the class
     */
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("levelup")
                // lambda that contains the brain of the command
                .executes(ctx ->
                {

                    CommandSender source = ctx.getSource().getSender();
                    if (!(source instanceof Player)) {
                        source.sendMessage("You must be a player to use this command!");
                        return Command.SINGLE_SUCCESS;
                    }
                    ;

                    // get old/new levels for the event
                    int oldLevel = ((Player) source).getLevel();
                    int newLevel = oldLevel + 1;

                    // firing the custom event!
                    PlayerLevelUpEvent levelUpEvent = new PlayerLevelUpEvent((Player) source, oldLevel, newLevel);

                    // calling the event! (can also use the Bukkit PluginManager to call)
                    levelUpEvent.callEvent();

                    // use a "qualifier
                    ((Player) source).setLevel(newLevel);
                    return Command.SINGLE_SUCCESS;
                })
                .build();
    }
}
