package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.blockposition;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.minecraft.commands.CommandSource;

public class BlockPositionCommand {
    LiteralArgumentBuilder<CommandSourceStack> createCommand(){
        return Commands.literal("blockposition");


    }
}
