package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.blockposition;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class BlockPositionArgument {
    public static LiteralCommandNode<CommandSourceStack> blockPositionArgument() {
      return Commands.literal("blockpositionargument")
              .then(Commands.argument("arg", ArgumentTypes.blockPosition())
                      .executes(ctx -> {
                          final BlockPositionResolver blockPositionResolver = ctx.getArgument("arg", BlockPositionResolver.class);
                          final BlockPosition blockPosition = blockPositionResolver.resolve(ctx.getSource());

                          ctx.getSource().getSender().sendPlainMessage("Put in" + blockPosition.x() + " " + blockPosition.y() + " " + blockPosition.y() + " " + blockPosition.z());
                          return Command.SINGLE_SUCCESS;
                      }))
              .build();
    };
}
