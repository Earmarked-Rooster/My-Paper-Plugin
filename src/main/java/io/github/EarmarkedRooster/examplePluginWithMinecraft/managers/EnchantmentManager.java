package io.github.EarmarkedRooster.examplePluginWithMinecraft.managers;

import com.google.gson.Gson;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.ExamplePluginWithMinecraft;
import org.bukkit.NamespacedKey;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentManager {

    private final ExamplePluginWithMinecraft plugin;
    private final Map<NamespacedKey, CustomEnchantment> enchantments = new HashMap<>();

    public EnchantmentManager(ExamplePluginWithMinecraft plugin) {
        this.plugin = plugin;
    }

    public void loadEnchantments() {
        // This is a simplified example. In a real plugin, you would scan for all JSON files in the enchantments folder.
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(plugin.getResource("enchantments/wheat_fields.json"));
        CustomEnchantment enchantment = gson.fromJson(reader, CustomEnchantment.class);
        enchantments.put(new NamespacedKey(plugin, "wheat_fields"), enchantment);
    }

    public CustomEnchantment getEnchantment(NamespacedKey key) {
        return enchantments.get(key);
    }

    public static class CustomEnchantment {
        private String name;
        private String description;
        private String[] applicable_items;

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String[] getApplicableItems() {
            return applicable_items;
        }
    }
}
