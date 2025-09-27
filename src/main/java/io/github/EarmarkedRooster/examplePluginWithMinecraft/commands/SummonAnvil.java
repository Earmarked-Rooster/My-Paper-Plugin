package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

import java.util.Collection;

public class SummonAnvil {
    public static LiteralCommandNode<CommandSourceStack> createCommand()
    {
        return Commands.literal("summonanvil")
                .requires(source -> source.getSender() instanceof Player)
                .executes(ctx -> {
                    Player player = (Player) ctx.getSource().getSender();
                    return Command.SINGLE_SUCCESS;
                })
                .then(RequiredArgumentBuilder.<CommandSourceStack, PlayerSelectorArgumentResolver>argument("player", ArgumentTypes.player())
                        .executes(ctx ->{

                            // Resolve argument into a collection of players

                            Collection<Player> targets = ctx.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(ctx.getSource());


                            if (targets.isEmpty()) {
                                ctx.getSource().getSender().sendMessage("Player not found!");
                                return 0;
                            }
                            Player target = targets.iterator().next();
                            Location spawnLocation = target.getLocation();

                            spawnLocation.setY(spawnLocation.getY() + 30);
                            spawnLocation.getBlock().setType(Material.ANVIL);

                            //FallingBlock fallingAnvil = spawnLocation.getWorld().spawnFallingBlock(spawnLocation, Material.ANVIL.createBlockData());

                            ctx.getSource().getSender().sendPlainMessage("Summoned an anvil above " + target.getName() + ".");
                            return Command.SINGLE_SUCCESS;
                        })).build();
    }
}
