package io.github.EarmarkedRooster.examplePluginWithMinecraft.recipes;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.items.LongRangeFirework;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class LongRangeFireworkRecipe {

    public static void registerRecipes() {
        Bukkit.addRecipe(createLongRangeFireworkRecipe());
    }

    private static ShapedRecipe createLongRangeFireworkRecipe() {
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "long_range_firework");
        ShapedRecipe recipe = new ShapedRecipe(key, LongRangeFirework.createLongRangeFirework());
        recipe.shape(" G ", " D ", " G ");
        recipe.setIngredient('G', Material.GUNPOWDER);
        recipe.setIngredient('D', Material.DIAMOND);
        return recipe;
    }
}
