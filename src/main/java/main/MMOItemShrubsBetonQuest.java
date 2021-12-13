package main;

import main.BetonQuest.CollectShrubObjective;
import main.BetonQuest.PlantShrubObjective;
import org.betonquest.betonquest.BetonQuest;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MMOItemShrubsBetonQuest extends JavaPlugin {
    private static MMOItemShrubsBetonQuest instance;

    @Override
    public void onEnable() {
        instance = this;
        BetonQuest bq = (BetonQuest) this.getServer().getPluginManager().getPlugin("BetonQuest");
        Objects.requireNonNull(bq, "BetonQuest plugin is not found");

        bq.registerObjectives("collect_shrub", CollectShrubObjective.class);
        getLogger().info("collect_shrub objective loaded");
        bq.registerObjectives("plant_shrub", PlantShrubObjective.class);
        getLogger().info("plant_shrub objective loaded");
    }

    public static MMOItemShrubsBetonQuest getInstance() { return instance; }
}
