package io.github.EarmarkedRooster.examplePluginWithMinecraft;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.*;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.icecream.IceCreamClass;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.FactionDatabase;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase; // Import new database class
import io.github.EarmarkedRooster.examplePluginWithMinecraft.events.PaperIsCoolEvent;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.ChatListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.LevelListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.FactionPowerListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.PaperIsCoolListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.MenuListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.EnchantmentManager;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.managers.MenuManager;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.menus.SocialCreditShopMenu;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.recipes.SocialCreditRecipes;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.recipes.LongRangeFireworkRecipe;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.SocialCreditListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.FireworkListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.AnvilListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.PointyEnchantmentListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.FieldsEnchantmentListener;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners.VeinMinerEnchantmentListener;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
// Removed unused imports for HashMap, Map, UUID, YamlConfiguration, ConfigurationSection, IOException

public final class ExamplePluginWithMinecraft extends JavaPlugin {


    private StashWithDatabaseTest stashWithDatabaseTest;
    private SocialCreditDatabase socialCreditDatabase; // New database instance

    @Override
    public void onLoad() {
        createMyCommands();
    }

    public void createMyCommands() {
        // creates our early "commands" for testing
        commandBuilder CommandClass = new commandBuilder();
        LiteralArgumentBuilder<CommandSourceStack> mainRoot = CommandClass.getEatGrassRoot();
        LiteralArgumentBuilder<CommandSourceStack> flyRoot = CommandClass.getToggleFlyRoot();
        LiteralArgumentBuilder<CommandSourceStack> flySpeedRoot = CommandClass.getFlySpeedRoot();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // running the eventHandler used by Paper for Brigadier to specifically register commands

        SocialCreditRecipes.registerRecipes();
        LongRangeFireworkRecipe.registerRecipes();
        DatabaseTest();
        this.stashWithDatabaseTest = new StashWithDatabaseTest(this);

        this.socialCreditDatabase = new SocialCreditDatabase(this); // Initialize database
        FactionDatabase factionDatabase = new FactionDatabase(this, socialCreditDatabase);

        EnchantmentManager enchantmentManager = new EnchantmentManager(this);
        enchantmentManager.loadEnchantments();

        MenuManager menuManager = new MenuManager(socialCreditDatabase);
        SocialCreditCommand socialCreditCommand = new SocialCreditCommand(socialCreditDatabase, factionDatabase); // Pass database instance
        FactionCommand factionCommand = new FactionCommand(factionDatabase, socialCreditDatabase);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            // register your commands here ...
            // within a lambda statement, .build() used to make commands into usable states
            commands.registrar().register(flySpeedCommand.createCommand().build());
            commands.registrar().register(IceCreamClass.createCommand());
            commands.registrar().register(helloWorldCommand.createCommand());
            commands.registrar().register(SummonAnvil.createCommand());
            commands.registrar().register(LevelUpCommand.createCommand());
            commands.registrar().register(PersistentStash.createCommand());
            commands.registrar().register(this.stashWithDatabaseTest.createCommand());
            commands.registrar().register(new MenuCommand(menuManager).createCommand().build());
            commands.registrar().register(socialCreditCommand.createCommand().build());
            commands.registrar().register(factionCommand.createCommand().build());
        });


        getLogger().info("All commands registered!");

        // starts event Listener
        getServer().getPluginManager().registerEvents(new LevelListener(), this);
        getServer().getPluginManager().registerEvents(new PaperIsCoolListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PersistentStash(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(menuManager), this);
        getServer().getPluginManager().registerEvents(socialCreditCommand, this);
        getServer().getPluginManager().registerEvents(new SocialCreditListener(socialCreditDatabase), this);
        getServer().getPluginManager().registerEvents(new FactionPowerListener(factionDatabase, socialCreditDatabase), this);
                getServer().getPluginManager().registerEvents(new FireworkListener(this), this);
        SocialCreditShopMenu socialCreditShopMenu = new SocialCreditShopMenu(socialCreditDatabase, enchantmentManager);
        getServer().getPluginManager().registerEvents(socialCreditShopMenu, this);
        getServer().getPluginManager().registerEvents(new AnvilListener(socialCreditShopMenu), this);
        getServer().getPluginManager().registerEvents(new PointyEnchantmentListener(this), this);
        getServer().getPluginManager().registerEvents(new FieldsEnchantmentListener(this, socialCreditDatabase), this);
        getServer().getPluginManager().registerEvents(new VeinMinerEnchantmentListener(this), this);
        getLogger().info("All events registered!");

        // calls method for PaperIsCoolEvent
        callCoolPaperEvent();

        getLogger().info("Plugin enabled!");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        socialCreditDatabase.saveScores(); // Save scores on disable
        if (stashWithDatabaseTest != null) {
            stashWithDatabaseTest.saveAllStashes();
        }
        getLogger().info("Plugin disabled!");
    }

    // Removed social credit persistence methods from here

    public void callCoolPaperEvent() {
        PaperIsCoolEvent coolEvent = new PaperIsCoolEvent(Component.text("Paper is cool!"));
        coolEvent.callEvent();
        // Plugins could have changed the message from inside their listeners here. So we need to get the message again.
        // This event structure allows for other plugins to change the message to their taste.
        // Like, for example, a plugin that adds a prefix to all messages.

        // checks to see if event is canceled, if is cancellable, Event#callEvent()
        // will return false (bool value), which is why we can use it in an if statement
        // this or !(coolEvent.isCancelled()) or coolEvent.callEvent()
        if (!(coolEvent.isCancelled())) {
            Bukkit.broadcast(coolEvent.getMessage());
        }


    }

    public void DatabaseTest()
    {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        File stashesFolder = new File(getDataFolder(), "stashes");
        if (!stashesFolder.exists()) {
            stashesFolder.mkdirs();
        }
    }


}
