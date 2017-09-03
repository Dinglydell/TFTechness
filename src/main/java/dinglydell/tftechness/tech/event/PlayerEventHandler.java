package dinglydell.tftechness.tech.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;

import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Events.AnvilCraftEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dinglydell.techresearch.PlayerTechDataExtendedProps;
import dinglydell.tftechness.tech.experiment.TFTExperiments;

public class PlayerEventHandler {

	@SubscribeEvent
	public void onAnvilCraft(AnvilCraftEvent event) {
		EntityPlayer p = (EntityPlayer) event.entity;
		Metal metal = MetalRegistry.instance.getMetalFromItem(event.input1
				.getItem());
		PlayerTechDataExtendedProps ptdep = PlayerTechDataExtendedProps.get(p);
		if (metal != null
				&& (metal.ingot == TFCItems.wroughtIronIngot
						|| metal.ingot == TFCItems.pigIronIngot || metal.ingot == TFCItems.steelIngot)) {
			//iron!
			ptdep.addResearchPoints(TFTExperiments.anvilIron);
		} else {

			ptdep.addResearchPoints(TFTExperiments.anvil);
		}
	}

	@SubscribeEvent
	public void onFillBucket(FillBucketEvent event) {
		if (!event.world.isRemote) {

			if (FluidContainerRegistry.isEmptyContainer(event.current)) {
				PlayerTechDataExtendedProps ptdep = PlayerTechDataExtendedProps
						.get(event.entityPlayer);
				ptdep.addResearchPoints(TFTExperiments.bucket);
			}
		}

	}

	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) event.entity;
			PlayerTechDataExtendedProps ptdep = PlayerTechDataExtendedProps
					.get(p);
			//TODO: make this less of a hack
			if (TFTExperiments.hurtMap.containsKey(event.source)) {
				ptdep.addResearchPoints(TFTExperiments.hurtMap
						.get(event.source));
			} else {
				ptdep.addResearchPoints(TFTExperiments.hurtGeneral);

			}

		}
	}

	@SubscribeEvent
	public void onLivingJump(LivingJumpEvent event) {
		if (!event.entity.worldObj.isRemote
				&& event.entity instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) event.entity;
			PlayerTechDataExtendedProps ptdep = PlayerTechDataExtendedProps
					.get(p);
			ptdep.addResearchPoints(TFTExperiments.jump);
		}
	}
}
