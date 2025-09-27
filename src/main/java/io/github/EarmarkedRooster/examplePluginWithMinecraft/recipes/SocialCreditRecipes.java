package io.github.EarmarkedRooster.examplePluginWithMinecraft.recipes;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.items.SocialCreditItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class SocialCreditRecipes {

    public static void registerRecipes() {
        Bukkit.addRecipe(createSocialCreditRecipe());
    }

    private static ShapedRecipe createSocialCreditRecipe() {
        //creates a new entry using namespaceKey to the minecraft data within this plugin's scope
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "social_credit");
        ShapedRecipe recipe = new ShapedRecipe(key, SocialCreditItem.createSocialCreditItem());
        recipe.shape("S  ", " S ", "  S");
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }
}
