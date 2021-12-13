package main.BetonQuest;

import gunging.ootilities.mmoitem_shrubs.events.PlantShrubEvent;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PlantShrubObjective extends BaseObjective {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlantEvent(final PlantShrubEvent event) {
        HandleObjectiveEvent(event);
    }

    public PlantShrubObjective(Instruction instruction) throws InstructionParseException {
        super(instruction);
    }
}
