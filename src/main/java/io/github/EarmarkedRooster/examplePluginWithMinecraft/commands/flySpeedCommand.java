package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

//create a separate class per command for ease of use
public class flySpeedCommand {
    //separation of the creation of a command & its logic
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        //returns a variable w/ datatype listed in function with the name "flyspeed"
        return Commands.literal("flyspeed")
                //uses a lambda expression to check through a sender object if the user executing code has permission to
                .requires(sender -> sender.getSender().hasPermission("permission.node"))
                //extenstion of the command branch to include a speed argument which holds a value between 0 and 1
                .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 1.0f))
                        //value said within speed executes a method refernce name runFlySpeedLogic)
                        .executes(flySpeedCommand::runFlySpeedLogic)
                );
    }

    private static int runFlySpeedLogic(CommandContext<CommandSourceStack> ctx) {
        //retrieves the value of speed from the .then extension
        float speed = FloatArgumentType.getFloat(ctx, "speed"); // Retrieve the speed argument
        CommandSender sender = ctx.getSource().getSender(); // Retrieve the command sender
        Entity executor = ctx.getSource().getExecutor(); // Retrieve the command executor, which may or may not be the same as the sender

        // Check whether the executor is a player, as you can only set a player's flight speed
        if (!(executor instanceof Player player)) {
            // If a non-player tried to set their own flight speed
            sender.sendPlainMessage("Only players can fly!");
            //return statement returns an value to eventManager saying it worked
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

        //if statement to check if players can see the command
        if(ctx.getSource().getExecutor() instanceof Player) {
            player.setFlySpeed(speed);
            player.sendRichMessage("<gold>Successfully updated your commands! ");
        }
        return Command.SINGLE_SUCCESS;
    }
}
