package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.components.helloWorld;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class helloWorldCommand {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
        return Commands.literal("helloworld")
                .executes(ctx -> {
                    CommandSender source = ctx.getSource().getSender();
                    ForwardingAudience audience = Bukkit.getServer();
                    Component helloworld = helloWorld.helloWorld();
                    if (source instanceof Player) {
                        source.sendMessage(helloworld);
                    }


                    return Command.SINGLE_SUCCESS;
                }).build();
    }
}
