package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes; // Changed import
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.FactionDatabase;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase; // Import database class

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SocialCreditCommand implements Listener {

    private final SocialCreditDatabase socialCreditDatabase; // Reference to database
    private final FactionDatabase factionDatabase;

    public static final int MENU_PERCENTAGE_CONVERSION = 10;
    public static final int BEGINNING_ITERATOR = 10;
    public static final int TOTAL_NUMBER_OF_PLATES_PLUS_BEGINNING_ITERATOR = 17;
    public static final int INVENTORY_SLOT_17 = 17;
    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;
    public SocialCreditCommand(SocialCreditDatabase socialCreditDatabase, FactionDatabase factionDatabase) { // Constructor
        this.socialCreditDatabase = socialCreditDatabase;
        this.factionDatabase = factionDatabase;
    }

    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("socialcredit")
                .executes(context -> {
                    if (context.getSource().getSender() instanceof Player) {
                        // gets the player object (cast creation of variable bc ctx.getSource().getSender() can be any object such as an Entity
                        Player player = (Player) context.getSource().getSender();
                        Inventory socialCreditMenu = Bukkit.createInventory(null, InventoryType.CHEST, Component.text(player.getName() + "'s Social Credit"));

                        // Get social credit from database
                        int socialCredit = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 50); // Default to 50 if not set

                        // math to set the number of green plates depending on the amount of social credit
                        int numGreenPanes = (int) Math.round((socialCredit / (double) MAX_SCORE) * 7);

                        for (int i = BEGINNING_ITERATOR; i < TOTAL_NUMBER_OF_PLATES_PLUS_BEGINNING_ITERATOR; i++) {
                            if (i < BEGINNING_ITERATOR + numGreenPanes) {
                                socialCreditMenu.setItem(i, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
                            } else {
                                socialCreditMenu.setItem(i, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                            }
                        }

                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);

                        //gets the Skull metadata from the playerHead object, type cast to SkullMeta
                        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
                        playerHeadMeta.setOwningPlayer(player);
                        playerHeadMeta.displayName(Component.text("Your Social Credit Score: " + socialCredit + "/100"));
                        List<Component> lore = new ArrayList<>();
                        String faction = factionDatabase.getPlayerFaction(player.getUniqueId());
                        if (faction != null) {
                            lore.add(Component.text("Faction: " + faction));
                            lore.add(Component.text("Donate to your faction to increase your standing with this faction & get rewarded!"));
                            ItemStack hopper = new ItemStack(Material.HOPPER);
                            hopper.editMeta(meta -> meta.displayName(Component.text("Donate to your faction!").color(NamedTextColor.GREEN).decorate(TextDecoration.ITALIC)));
                            socialCreditMenu.setItem(18, hopper);
                        } else {
                            lore.add(Component.text("Join a faction to utilize social standing for points!"));
                        }
                        playerHeadMeta.lore(lore);
                        playerHead.setItemMeta(playerHeadMeta);
                        socialCreditMenu.setItem(INVENTORY_SLOT_17, playerHead);

                        player.openInventory(socialCreditMenu);
                    } // end of lambda .executes()
                    return Command.SINGLE_SUCCESS;
                })// beginning of branch of command tree to set the social credit score of a player
                .then(Commands.literal("set") // Add set command
                        .then(Commands.argument("player", ArgumentTypes.player()) // ArgumentTypes.player() is necessary to determine who is receiving the .executes() part of this branch
                                .then(Commands.argument("score", IntegerArgumentType.integer(MIN_SCORE, MAX_SCORE)) // asks for a social credit "score" from one to 100
                                        .executes(context -> {
                                            Collection<Player> targetPlayers = context.getArgument("player", PlayerSelectorArgumentResolver.class).resolve(context.getSource()); // Corrected line
                                            int score = IntegerArgumentType.getInteger(context, "score");

                                            Player targetPlayer = targetPlayers.iterator().next();
                                            socialCreditDatabase.getSocialCreditScores().put(targetPlayer.getUniqueId(), score);
                                            socialCreditDatabase.saveScores(); // Save immediately after setting
                                            context.getSource().getSender().sendMessage(Component.text("Set social credit for " + targetPlayer.getName() + " to " + score + "."));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )
                )
                .then(Commands.literal("shop")
                        .executes(context -> {
                            if (context.getSource().getSender() instanceof Player) {
                                Player player = (Player) context.getSource().getSender();
                                player.openInventory(new io.github.EarmarkedRooster.examplePluginWithMinecraft.menus.SocialCreditShopMenu(socialCreditDatabase, new io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.EnchantmentManager(io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft.getPlugin(io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft.class))).createShopMenu(player));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                );
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text(event.getWhoClicked().getName() + "'s Social Credit"))) {
            if (event.getClickedInventory() == event.getView().getTopInventory()) {
                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();
                ItemStack currentItem = event.getCurrentItem();

                if (currentItem != null && currentItem.getType() == Material.HOPPER) {
                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem != null && cursorItem.getType() == Material.DIAMOND) {
                        String factionName = factionDatabase.getPlayerFaction(player.getUniqueId());
                        if (factionName != null) {
                            int currentPower = factionDatabase.getFactionPower(factionName);
                            factionDatabase.setFactionPower(factionName, currentPower + 1);
                            int currentSocialCredit = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 0);
                            socialCreditDatabase.getSocialCreditScores().put(player.getUniqueId(), currentSocialCredit + 1);
                            cursorItem.setAmount(cursorItem.getAmount() - 1);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                            player.sendMessage(Component.text("You donated to " + factionName + "! Your faction power has increased by 1!"));
                        }
                    }
                }
            }
        }
    }
}