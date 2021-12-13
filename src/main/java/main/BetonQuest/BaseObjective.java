package main.BetonQuest;

import gunging.ootilities.mmoitem_shrubs.MMOItem_Shrub;
import gunging.ootilities.mmoitem_shrubs.events.AbsEvent;
import main.MMOItemShrubsBetonQuest;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.manager.TypeManager;
import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.Objective;
import org.betonquest.betonquest.config.Config;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.PlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class BaseObjective extends Objective implements Listener {

    private final Type itemType;
    private final String itemId;

    private final int neededAmount;
    private final boolean notify;

    public void HandleObjectiveEvent(final AbsEvent event) {
        if (event.isCancelled()) return;

        Player player = event.getPlayer();
        if (player == null) return;

        MMOItem_Shrub shrub = event.getShrub();
        final String playerID = PlayerConverter.getID(player);

        if (playerID == null) return;

        final EventObjectiveData objectiveData = (EventObjectiveData) dataMap.get(playerID);

        if (!containsPlayer(playerID) || !checkConditions(playerID)) return;

        final String realItemType = shrub.getMMOItem_Type();
        final String realItemID = shrub.getMMOItem_Id();
        if (!realItemID.equalsIgnoreCase(itemId) || !realItemType.equalsIgnoreCase(itemType.getId())) return;

        objectiveData.sub();
        if (objectiveData.isCompleted()) {
            completeObjective(playerID);
        } else {
            if (notify) {
                try {
                    Config.sendNotify(
                            instruction.getPackage().getName(),
                            playerID,
                            "items_to_pickup",
                            new String[]{String.valueOf(objectiveData.getAmount())},
                            "items_to_pickup,info"
                    );
                } catch (final QuestRuntimeException exception) {
                    MMOItemShrubsBetonQuest.getInstance().getLogger().info("The notify system was unable to play a sound; Error was: '" + exception.getMessage() + "'");
                }
            }
        }
    }

    public BaseObjective(Instruction instruction) throws InstructionParseException {
        super(instruction);
        template = EventObjectiveData.class;

        final TypeManager typeManager = MMOItems.plugin.getTypes();
        itemType = typeManager.get(instruction.next());
        itemId = instruction.next();

        final String amountStr = instruction.getOptional("amount");
        neededAmount = amountStr == null ? 1 : Integer.parseInt(amountStr);

        notify = instruction.hasArgument("notify");
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getDefaultDataInstruction() {
        return String.valueOf(neededAmount);
    }

    @Override
    public String getProperty(final String name, final String playerID) {
        if ("left".equalsIgnoreCase(name)) {
            return Integer.toString(neededAmount - ((EventObjectiveData) dataMap.get(playerID)).getAmount());
        } else if ("amount".equalsIgnoreCase(name)) {
            return Integer.toString(((EventObjectiveData) dataMap.get(playerID)).getAmount());
        }
        return "";
    }

    public static class EventObjectiveData extends ObjectiveData {
        private int amount;

        public EventObjectiveData(final String instruction, final String playerID, final String objID) {
            super(instruction, playerID, objID);
            amount = Integer.parseInt(instruction);
        }

        private boolean isCompleted() {
            return amount <= 0;
        }

        private void sub() {
            amount--;
            update();
        }

        private int getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.valueOf(amount);
        }
    }
}
