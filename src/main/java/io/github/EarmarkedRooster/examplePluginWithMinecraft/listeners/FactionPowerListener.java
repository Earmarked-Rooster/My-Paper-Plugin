package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.FactionDatabase;
import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class FactionPowerListener implements Listener {

    private static final int FACTIONPOWERCONSTANT = 10;
    private final FactionDatabase factionDatabase;
    private final SocialCreditDatabase socialCreditDatabase;

    public FactionPowerListener(FactionDatabase factionDatabase, SocialCreditDatabase socialCreditDatabase) {
        this.factionDatabase = factionDatabase;
        this.socialCreditDatabase = socialCreditDatabase;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();
            // grabs the killer and victim of the killer for this code to function
            String killerFaction = factionDatabase.getPlayerFaction(killer.getUniqueId());
            String victimFaction = factionDatabase.getPlayerFaction(victim.getUniqueId());
            
            if (killerFaction != null && victimFaction != null && !killerFaction.equals(victimFaction)) {
                // increases faction power for killer's faction
                int factionPower = factionDatabase.getFactionPower(killerFaction);
                factionDatabase.setFactionPower(killerFaction, factionPower + FACTIONPOWERCONSTANT);

                // increases social credit score for killer
                int currentScore = socialCreditDatabase.getSocialCreditScores().getOrDefault(killer.getUniqueId(), 0);
                int newScore = currentScore + FACTIONPOWERCONSTANT;
                socialCreditDatabase.getSocialCreditScores().put(killer.getUniqueId(), newScore);

                // decreases faction power for victim's faction
                int victimFactionPower = factionDatabase.getFactionPower(victimFaction);
                factionDatabase.setFactionPower(victimFaction, victimFactionPower - FACTIONPOWERCONSTANT);

                // decreases social credit for victim
                int victimCurrentScore = socialCreditDatabase.getSocialCreditScores().getOrDefault(victim.getUniqueId(), 0);
                socialCreditDatabase.getSocialCreditScores().put(victim.getUniqueId(), victimCurrentScore - FACTIONPOWERCONSTANT);
            }
        }
    }
}
