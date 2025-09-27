package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class commandBuilder {
    private final LiteralArgumentBuilder<CommandSourceStack> eatGrassRoot = Commands.literal("eatGrass")
            .then(Commands.literal("chew")
                    .then(Commands.literal("green_grass"))
                    .then(Commands.literal("red_grass"))
            );
    private final LiteralArgumentBuilder<CommandSourceStack> toggleFlyRoot = Commands.literal("serverflight")
            //checks for argument in /serverflight <arg> with the name "allow", if so execute following lambda
            .then(Commands.argument("allow", BoolArgumentType.bool())
                    .executes(ctx -> {
                        //creates a boolean allowed that retrieves the argument from the previous statement (is true)
                        boolean allowed = ctx.getArgument("allow", boolean.class);
                        /* Toggle server flying */
                        return 0;
                    })
            );

    private final LiteralArgumentBuilder<CommandSourceStack> flySpeedRoot = Commands.literal("flyspeed")
            .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 1.0f)) // looks for values under the catagory name "speed" between 0 & 1, and is a floating-point value
                    .executes(ctx -> { //lambda to execute the speed of the player
                        float speed = FloatArgumentType.getFloat(ctx, "speed"); // Retrieve the speed argument
                        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender (who sent the command)
                        Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender

                        // Check whether the executor is a player, as you can only set a player's flight speed
                        if (!(executor instanceof Player player)) {
                            // If a non-player tried to set their own flight speed
                            sender.sendPlainMessage("Only players can fly!");
                            return Command.SINGLE_SUCCESS;
                        }

                        // Set the player's speed
                        player.setFlySpeed(speed);

                        if (sender == executor) {
                            // If the player executed the command themselves
                            player.sendPlainMessage("Successfully set your flight speed to " + speed);
                            return Command.SINGLE_SUCCESS;
                        }

                        // If the speed was set by a different sender (Like using /execute)
                        sender.sendRichMessage("Successfully set <playername>'s flight speed to " + speed, Placeholder.component("playername", player.name()));
                        player.sendPlainMessage("Your flight speed has been set to " + speed);
                        return Command.SINGLE_SUCCESS;
                    }).build()
            );

    public commandBuilder() {
    }

    public LiteralArgumentBuilder<CommandSourceStack> getEatGrassRoot() {
        return eatGrassRoot;
    }

    public LiteralArgumentBuilder<CommandSourceStack> getToggleFlyRoot() {
        return toggleFlyRoot;
    }

    public LiteralArgumentBuilder<CommandSourceStack> getFlySpeedRoot() {
        return flySpeedRoot;
    }


}
