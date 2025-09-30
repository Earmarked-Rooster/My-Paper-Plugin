package io.github.EarmarkedRooster.examplePluginWithMinecraft.menus;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.EnchantmentManager;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Sound;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SocialCreditShopMenu implements Listener {

    private final SocialCreditDatabase socialCreditDatabase;
    private final EnchantmentManager enchantmentManager;
        private static final int FIELDS_ENCHANTMENT_COST = 32;
    private static final int VEIN_MINER_ENCHANTMENT_COST = 75;

    public SocialCreditShopMenu(SocialCreditDatabase socialCreditDatabase, EnchantmentManager enchantmentManager) {
        this.socialCreditDatabase = socialCreditDatabase;
        this.enchantmentManager = enchantmentManager;
    }

    public EnchantmentManager getEnchantmentManager() {
        return enchantmentManager;
    }

    public Inventory createShopMenu(Player player) {
        Inventory shopMenu = Bukkit.createInventory(null, 54, Component.text("Shop"));
        // using the available registries to make a list of all the available items to buy in the shop,
        // displayed through the GUI of a double chest
        // Player head
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
        playerHeadMeta.setOwningPlayer(player);
        playerHeadMeta.displayName(Component.text("Information"));
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Click on an item to purchase it."));
        playerHeadMeta.lore(lore);
        playerHead.setItemMeta(playerHeadMeta);
        shopMenu.setItem(53, playerHead);

        // Items on display
        ItemStack fieldsBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) fieldsBook.getItemMeta();
        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:fields");
        Enchantment fieldsEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));
        if (fieldsEnchantment != null) {
            bookMeta.addStoredEnchant(fieldsEnchantment, 1, true);
        }
        bookMeta.displayName(Component.text("Fields Enchantment").color(NamedTextColor.GOLD));
        List<Component> bookLore = new ArrayList<>();
        bookLore.add(Component.text("Cost: " + FIELDS_ENCHANTMENT_COST + " Social Credit"));
        bookMeta.lore(bookLore);
        fieldsBook.setItemMeta(bookMeta);
                shopMenu.setItem(21, fieldsBook);

        ItemStack veinMinerBook = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta veinMinerBookMeta = (EnchantmentStorageMeta) veinMinerBook.getItemMeta();
        NamespacedKey veinMinerEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:vein_miner");
        Enchantment veinMinerEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, veinMinerEnchantmentKey));
        if (veinMinerEnchantment != null) {
            veinMinerBookMeta.addStoredEnchant(veinMinerEnchantment, 1, true);
        }
        veinMinerBookMeta.displayName(Component.text("Vein Miner Enchantment").color(NamedTextColor.GOLD));
        List<Component> veinMinerBookLore = new ArrayList<>();
        veinMinerBookLore.add(Component.text("Cost: " + VEIN_MINER_ENCHANTMENT_COST + " Social Credit"));
        veinMinerBookMeta.lore(veinMinerBookLore);
        veinMinerBook.setItemMeta(veinMinerBookMeta);
        shopMenu.setItem(20, veinMinerBook);

        ItemStack string = new ItemStack(Material.STRING);
        ItemMeta stringMeta = string.getItemMeta();
        stringMeta.displayName(Component.text("String").color(NamedTextColor.WHITE));
        string.setItemMeta(stringMeta);
        shopMenu.setItem(22, string);

        ItemStack emerald = new ItemStack(Material.EMERALD);
        ItemMeta emeraldMeta = emerald.getItemMeta();
        emeraldMeta.displayName(Component.text("Emerald").color(NamedTextColor.GREEN));
        emerald.setItemMeta(emeraldMeta);
        shopMenu.setItem(23, emerald);
        // repeatedly grabs the item meta for each displayed item and lists them in the shop
        return shopMenu;
    }

    public static Inventory createQuantityMenu(ItemStack item) {
        Inventory quantityMenu = Bukkit.createInventory(null, 54, Component.text("Select Quantity"));
        // amount of items that can be bought at a single time
        int[] quantities = {1, 4, 16, 32, 64};
        // the slots these quantities will be displayed in another double chest GUI once an item in the shop is clicked on by a player
        int[] slots = {20, 21, 22, 23, 24};

        for (int i = 0; i < quantities.length; i++) {
            int quantity = quantities[i];
            ItemStack quantityItem = new ItemStack(item.getType(), quantity);
            ItemMeta quantityMeta = quantityItem.getItemMeta();
            quantityMeta.displayName(Component.text("Quantity: " + quantity));
            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Cost: " + quantity + " Social Credit"));
            quantityMeta.lore(lore);
            quantityItem.setItemMeta(quantityMeta);
            quantityMenu.setItem(slots[i], quantityItem);
        }

        ItemStack exitButton = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exitButton.getItemMeta();
        exitMeta.displayName(Component.text("Exit"));
        exitButton.setItemMeta(exitMeta);
        quantityMenu.setItem(45, exitButton);

        return quantityMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text("Shop"))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack currentItem = event.getCurrentItem();
            // handler to deduct social credit points from players when they buy items
            if (currentItem != null) {
                if (currentItem.getType() == Material.STRING || currentItem.getType() == Material.EMERALD) {
                    player.openInventory(createQuantityMenu(currentItem));
                } else if (currentItem.hasItemMeta() && currentItem.getItemMeta().displayName().equals(Component.text("Fields Enchantment").color(NamedTextColor.GOLD))) {
                    int socialCredit = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 0);
                    // specifically handlers for the custom enchantments
                    if (socialCredit >= FIELDS_ENCHANTMENT_COST) {
                        socialCreditDatabase.getSocialCreditScores().put(player.getUniqueId(), socialCredit - FIELDS_ENCHANTMENT_COST);

                        ItemStack fieldsBook = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) fieldsBook.getItemMeta();
                        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
                        NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:fields");
                        Enchantment fieldsEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));
                        if (fieldsEnchantment != null) {
                            bookMeta.addStoredEnchant(fieldsEnchantment, 1, true);
                        }
                        bookMeta.displayName(Component.text("Fields Enchantment").color(NamedTextColor.GOLD));
                        fieldsBook.setItemMeta(bookMeta);

                        player.getInventory().addItem(fieldsBook);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Component.text("You don't have enough social credit!"));
                    }
                } else if (currentItem.hasItemMeta() && currentItem.getItemMeta().displayName().equals(Component.text("Vein Miner Enchantment").color(NamedTextColor.GOLD))) {
                    int socialCredit = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 0);
                    if (socialCredit >= VEIN_MINER_ENCHANTMENT_COST) {
                        socialCreditDatabase.getSocialCreditScores().put(player.getUniqueId(), socialCredit - VEIN_MINER_ENCHANTMENT_COST);

                        ItemStack veinMinerBook = new ItemStack(Material.ENCHANTED_BOOK);
                        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) veinMinerBook.getItemMeta();
                        Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
                        NamespacedKey customEnchantmentKey = NamespacedKey.fromString("examplepluginwithminecraft:vein_miner");
                        Enchantment veinMinerEnchantment = enchantmentRegistry.get(TypedKey.create(RegistryKey.ENCHANTMENT, customEnchantmentKey));
                        if (veinMinerEnchantment != null) {
                            bookMeta.addStoredEnchant(veinMinerEnchantment, 1, true);
                        }
                        bookMeta.displayName(Component.text("Vein Miner Enchantment").color(NamedTextColor.GOLD));
                        veinMinerBook.setItemMeta(bookMeta);

                        player.getInventory().addItem(veinMinerBook);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Component.text("You don't have enough social credit!"));
                    }
                }
            }
        } else if (event.getView().title().equals(Component.text("Select Quantity"))) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack currentItem = event.getCurrentItem();

            if (currentItem != null) {
                if (currentItem.getType() == Material.BARRIER) {
                    player.openInventory(createShopMenu(player));
                } else {
                    int quantity = currentItem.getAmount();
                    int socialCredit = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 0);
                    if (socialCredit >= quantity) {
                        socialCreditDatabase.getSocialCreditScores().put(player.getUniqueId(), socialCredit - quantity);
                        player.getInventory().addItem(new ItemStack(currentItem.getType(), quantity));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    } else {
                        player.sendMessage(Component.text("You don't have enough social credit!"));
                    }
                }
            }
        }
    }
}
