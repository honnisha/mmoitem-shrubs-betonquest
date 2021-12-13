package main.BetonQuest;

import gunging.ootilities.mmoitem_shrubs.events.CollectShrubEvent;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class CollectShrubObjective extends BaseObjective {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlantEvent(final CollectShrubEvent event) {
        HandleObjectiveEvent(event);
    }

    public CollectShrubObjective(Instruction instruction) throws InstructionParseException {
        super(instruction);
    }
}