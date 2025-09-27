package io.github.EarmarkedRooster.examplePluginWithMinecraft.database;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class SocialCreditDatabase {

    private final ExamplePluginWithMinecraft plugin;
    private final File socialCreditFile;
    private final Map<UUID, Integer> socialCreditScores;

    public SocialCreditDatabase(ExamplePluginWithMinecraft plugin) {
        this.plugin = plugin;
        this.socialCreditFile = new File(plugin.getDataFolder(), "social_credit.yml");
        this.socialCreditScores = new HashMap<>();
        loadScores();
    }

    public Map<UUID, Integer> getSocialCreditScores() {
        return socialCreditScores;
    }

    public void loadScores() {
        if (!socialCreditFile.exists()) {
            try {
                socialCreditFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create social_credit.yml", e);
                return;
            }
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(socialCreditFile);
        ConfigurationSection scoresSection = config.getConfigurationSection("scores");
        if (scoresSection != null) {
            //process of saving scores that do not have existing keys in the .yml file
            for (String uuidString : scoresSection.getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(uuidString);
                    int score = scoresSection.getInt(uuidString);
                    socialCreditScores.put(uuid, score);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid UUID found in social_credit.yml: " + uuidString);
                }
            }
        }
        plugin.getLogger().info("Loaded " + socialCreditScores.size() + " social credit scores.");
    }

    public void saveScores() {
        YamlConfiguration config = new YamlConfiguration();
        for (Map.Entry<UUID, Integer> entry : socialCreditScores.entrySet()) {
            config.set("scores." + entry.getKey().toString(), entry.getValue());
        }
        try {
            config.save(socialCreditFile);
            plugin.getLogger().info("Saved " + socialCreditScores.size() + " social credit scores.");
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save social credit scores: " + e.getMessage(), e);
        }
    }
}
