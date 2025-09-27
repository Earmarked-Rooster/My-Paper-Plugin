package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.icecream;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class IceCreamClass {
    public static LiteralCommandNode<CommandSourceStack> createCommand() {
      return Commands.literal("icecream")
              .then(Commands.argument("flavor", new IceCreamArgument())
                      .executes(ctx -> {
                          final IceCreamFlavor flavor = ctx.getArgument("flavor", IceCreamFlavor.class);

                          ctx.getSource().getSender().sendRichMessage("<b><red>Y<green>U<aqua>M<light_purple>!</b> You just had a scoop of <flavor>!",
                                  Placeholder.unparsed("flavor", flavor.toString())
                          );
                          return Command.SINGLE_SUCCESS;
                      })
              )
              .build();
    };
}
