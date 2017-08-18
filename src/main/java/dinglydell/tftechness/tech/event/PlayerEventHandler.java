package dinglydell.tftechness.tech.event;

import net.minecraft.entity.player.EntityPlayer;

import com.bioxx.tfc.api.Events.AnvilCraftEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dinglydell.techresearch.PlayerTechDataExtendedProps;
import dinglydell.tftechness.tech.experiment.TFTExperiments;

public class PlayerEventHandler {
	@SubscribeEvent
	public void onAnvilCraft(AnvilCraftEvent event) {
		EntityPlayer p = (EntityPlayer) event.entity;
		PlayerTechDataExtendedProps ptdep = PlayerTechDataExtendedProps.get(p);
		ptdep.addResearchPoints(TFTExperiments.anvil);
	}
}
