package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.FactionDatabase;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FactionCommand {

    private final FactionDatabase factionDatabase;
    private final SocialCreditDatabase socialCreditDatabase;

    public FactionCommand(FactionDatabase factionDatabase, SocialCreditDatabase socialCreditDatabase) {
        this.factionDatabase = factionDatabase;
        this.socialCreditDatabase = socialCreditDatabase;
    }

    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("faction")
                .then(Commands.literal("create")
                        .then(Commands.argument("factionName", StringArgumentType.string())
                                .executes(context -> {
                                    String factionName = StringArgumentType.getString(context, "factionName");
                                    if (factionDatabase.factionExists(factionName)) {
                                        context.getSource().getSender().sendMessage(Component.text("Faction '" + factionName + "' already exists!"));
                                    } else {
                                        factionDatabase.saveFaction(factionName);
                                        context.getSource().getSender().sendMessage(Component.text("Faction '" + factionName + "' created!"));
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("join")
                        .then(Commands.argument("factionName", StringArgumentType.string())
                                // calls the suggestFactions method to display current factions within the .yml file
                                .suggests(this::suggestFactions)
                                .executes(context -> {
                                    if (!(context.getSource().getSender() instanceof Player)) {
                                        context.getSource().getSender().sendMessage(Component.text("Only players can join factions."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    Player player = (Player) context.getSource().getSender();
                                    String factionName = StringArgumentType.getString(context, "factionName");
                                    if (!factionDatabase.factionExists(factionName)) {
                                        player.sendMessage(Component.text("Faction '" + factionName + "' does not exist!"));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    String playerFaction = factionDatabase.getPlayerFaction(player.getUniqueId());
                                    if (playerFaction != null) {
                                        player.sendMessage(Component.text("You are already in a faction."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    factionDatabase.addPlayerToFaction(factionName, player.getUniqueId());
                                    player.sendMessage(Component.text("You have joined faction '" + factionName + "'!"));
                                    return Command.SINGLE_SUCCESS;
                                })))
                .then(Commands.literal("leave")
                        .executes(context -> {
                            if (!(context.getSource().getSender() instanceof Player)) {
                                context.getSource().getSender().sendMessage(Component.text("Only players can leave factions."));
                                return Command.SINGLE_SUCCESS;
                            }
                            Player player = (Player) context.getSource().getSender();
                            String playerFaction = factionDatabase.getPlayerFaction(player.getUniqueId());
                            if (playerFaction == null) {
                                player.sendMessage(Component.text("You are not in a faction."));
                                return Command.SINGLE_SUCCESS;
                            }
                            if (factionDatabase.isLeader(player.getUniqueId(), playerFaction)) {
                                player.sendMessage(Component.text("You must step down as leader before leaving the faction."));
                                return Command.SINGLE_SUCCESS;
                            }
                            factionDatabase.removePlayerFromFaction(player.getUniqueId());
                            player.sendMessage(Component.text("You have left faction '" + playerFaction + "'!"));
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("top")
                        .requires(source -> source.getSender().hasPermission("ExamplePluginWithMinecraft.faction.top"))
                        .executes(context -> {
                            if (!(context.getSource().getSender() instanceof Player)) {
                                context.getSource().getSender().sendMessage(Component.text("Only players can use this command."));
                                return Command.SINGLE_SUCCESS;
                            }
                            Player player = (Player) context.getSource().getSender();
                            List<String> factions = factionDatabase.getFactions();
                            if (factions.isEmpty()) {
                                player.sendMessage(Component.text("There are no factions to display."));
                                return Command.SINGLE_SUCCESS;
                            }

                            factions.sort(Comparator.comparingInt(factionDatabase::getFactionPower).reversed());

                            player.sendMessage(Component.text("--- Faction Leaderboard ---").decorate(TextDecoration.BOLD));
                            for (String factionName : factions) {
                                int factionPower = factionDatabase.getFactionPower(factionName);
                                player.sendMessage(Component.text(factionName, NamedTextColor.GOLD).decorate(TextDecoration.BOLD).append(Component.text(": " + factionPower)));
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("display")
                        .executes(context -> {
                            if (!(context.getSource().getSender() instanceof Player)) {
                                context.getSource().getSender().sendMessage(Component.text("Only players can use this command."));
                                return Command.SINGLE_SUCCESS;
                            }
                            Player player = (Player) context.getSource().getSender();
                            String playerFaction = factionDatabase.getPlayerFaction(player.getUniqueId());
                            if (playerFaction == null) {
                                player.sendMessage(Component.text("You are not in a faction."));
                                return Command.SINGLE_SUCCESS;
                            }
                            List<String> members = factionDatabase.getFactionMembers(playerFaction);
                            if (members.isEmpty()) {
                                player.sendMessage(Component.text("Your faction has no members."));
                                return Command.SINGLE_SUCCESS;
                            }
                            player.sendMessage(Component.text("--- Members of " + playerFaction + " ---").decorate(TextDecoration.BOLD));
                            for (String memberName : members) {
                                player.sendMessage(Component.text(memberName));
                            }
                            return Command.SINGLE_SUCCESS;
                        }))
                .then(Commands.literal("leader")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String newLeaderName = StringArgumentType.getString(context, "player");
                                    Player newLeader = context.getSource().getSender().getServer().getPlayer(newLeaderName);
                                    if (newLeader == null) {
                                        context.getSource().getSender().sendMessage(Component.text("Player not found."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    String newLeaderFaction = factionDatabase.getPlayerFaction(newLeader.getUniqueId());
                                    if (newLeaderFaction == null) {
                                        context.getSource().getSender().sendMessage(Component.text("That player is not in a faction."));
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    if (context.getSource().getSender() instanceof Player) {
                                        Player player = (Player) context.getSource().getSender();
                                        String playerFaction = factionDatabase.getPlayerFaction(player.getUniqueId());
                                        if (playerFaction == null) {
                                            player.sendMessage(Component.text("You are not in a faction."));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        if (!newLeaderFaction.equals(playerFaction)) {
                                            player.sendMessage(Component.text("That player is not in your faction."));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                    }

                                    factionDatabase.setLeader(newLeaderFaction, newLeader.getUniqueId());
                                    context.getSource().getSender().sendMessage(Component.text(newLeader.getName() + " is now the leader of " + newLeaderFaction + "."));
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(Commands.literal("leave")
                                .executes(context -> {
                                    if (!(context.getSource().getSender() instanceof Player)) {
                                        context.getSource().getSender().sendMessage(Component.text("Only players can use this command."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    Player player = (Player) context.getSource().getSender();
                                    String playerFaction = factionDatabase.getPlayerFaction(player.getUniqueId());
                                    if (playerFaction == null) {
                                        player.sendMessage(Component.text("You are not in a faction."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if (!factionDatabase.isLeader(player.getUniqueId(), playerFaction) && !player.isOp()) {
                                        player.sendMessage(Component.text("Only the leader or an operator can do this."));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    factionDatabase.setLeader(playerFaction, null);
                                    player.sendMessage(Component.text("You have stepped down as leader of " + playerFaction + "."));
                                    return Command.SINGLE_SUCCESS;
                                })));
    }

    private CompletableFuture<Suggestions> suggestFactions(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        // grabs the input for the argument
        String input = builder.getRemaining().toLowerCase();
        // for each faction name in the database have the SuggestionsBuilder append each faction name if they start with the input for the argument
        for (String factionName : factionDatabase.getFactions()) {
            if (factionName.toLowerCase().startsWith(input)) {
                builder.suggest(factionName);
            }
        }
        // allows use of PaperAPI when run
        return builder.buildFuture();
    }
}