package io.github.EarmarkedRooster.examplePluginWithMinecraft.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactionDatabase {

    private final JavaPlugin plugin;
    private final SocialCreditDatabase socialCreditDatabase;
    private FileConfiguration factionConfig;
    private File factionFile;

    public FactionDatabase(JavaPlugin plugin, SocialCreditDatabase socialCreditDatabase) {
        this.plugin = plugin;
        this.socialCreditDatabase = socialCreditDatabase;
        setup();
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        //creates a new yml file named factions
        factionFile = new File(plugin.getDataFolder(), "factions.yml");
        if (!factionFile.exists()) {
            try {
                factionFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create factions.yml file!");
            }
        }
        factionConfig = YamlConfiguration.loadConfiguration(factionFile);
    }

    public void saveFaction(String factionName) {
        // creates the format that each faction and its members will be saved to
        factionConfig.set("factions." + factionName + ".members", new ArrayList<String>());
        factionConfig.set("factions." + factionName + ".factionPower", 0);
        factionConfig.set("factions." + factionName + ".leader", null);
        try {
            factionConfig.save(factionFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save to factions.yml file!");
        }
    }

    public boolean factionExists(String factionName) {
        // checks to see if requested factionName exists
        return factionConfig.getConfigurationSection("factions." + factionName) != null;
    }

    public void addPlayerToFaction(String factionName, UUID playerUUID) {
        List<String> members = factionConfig.getStringList("factions." + factionName + ".members");
        members.add(playerUUID.toString());
        // set the .yml configuration for each faction's members based on the members List
        factionConfig.set("factions." + factionName + ".members", members);
        try {
            factionConfig.save(factionFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save to factions.yml file!");
        }
    }

    public String getPlayerFaction(UUID playerUUID) {
        if (factionConfig.getConfigurationSection("factions") == null) {
            return null;
        }
        // for each faction in the factionConfig file, grab the members file and check if any player UUID exists in said file
        for (String factionName : factionConfig.getConfigurationSection("factions").getKeys(false)) {
            List<String> members = factionConfig.getStringList("factions." + factionName + ".members");
            if (members.contains(playerUUID.toString())) {
                return factionName;
            }
        }
        return null;
    }

    public void removePlayerFromFaction(UUID playerUUID) {
        String factionName = getPlayerFaction(playerUUID);
        if (factionName != null) {
            List<String> members = factionConfig.getStringList("factions." + factionName + ".members");
            members.remove(playerUUID.toString());
            // resets the factionConfig to the new members List after player has been removed
            factionConfig.set("factions." + factionName + ".members", members);
            try {
                factionConfig.save(factionFile);
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save to factions.yml file!");
            }
            socialCreditDatabase.getSocialCreditScores().put(playerUUID, 1);
            socialCreditDatabase.saveScores();
        }
    }

    public List<String> getFactions() {
        if (factionConfig.getConfigurationSection("factions") == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(factionConfig.getConfigurationSection("factions").getKeys(false));
    }

    public int getFactionPower(String factionName) {
        return factionConfig.getInt("factions." + factionName + ".factionPower");
    }

    public void setFactionPower(String factionName, int factionPower) {
        factionConfig.set("factions." + factionName + ".factionPower", factionPower);
        try {
            factionConfig.save(factionFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save to factions.yml file!");
        }
    }

    public List<String> getFactionMembers(String factionName) {
        List<String> memberUUIDs = factionConfig.getStringList("factions." + factionName + ".members");
        List<String> memberNames = new ArrayList<>();
        for (String memberUUID : memberUUIDs) {
            memberNames.add(plugin.getServer().getOfflinePlayer(UUID.fromString(memberUUID)).getName());
        }
        return memberNames;
    }

    public void setLeader(String factionName, UUID playerUUID) {
        if (playerUUID == null) {
            factionConfig.set("factions." + factionName + ".leader", null);
        } else {
            factionConfig.set("factions." + factionName + ".leader", playerUUID.toString());
        }
        try {
            factionConfig.save(factionFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save to factions.yml file!");
        }
    }

    public String getLeader(String factionName) {
        return factionConfig.getString("factions." + factionName + ".leader");
    }

    public boolean isLeader(UUID playerUUID, String factionName) {
        String leaderUUID = getLeader(factionName);
        return leaderUUID != null && leaderUUID.equals(playerUUID.toString());
    }
}
