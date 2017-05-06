package dinglydell.tftechness.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFAnvil;
import dinglydell.tftechness.tileentities.machine.TileRFAnvil;

public class GuiRFAnvil extends GuiAugmentableBase {
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFAnvil.png");

	protected TileRFAnvil myTile;

	protected ElementSlotOverlay[] inputSlots = new ElementSlotOverlay[6];
	protected ElementSlotOverlay[] inputRowSlots = new ElementSlotOverlay[6];
	protected ElementSlotOverlay tankSlotOrange;
	protected ElementSlotOverlay tankSlotRed;
	protected ElementSlotOverlay moldSlotOrange;
	protected ElementSlotOverlay moldSlotYellow;

	protected ElementDualScaled progress;

	public GuiRFAnvil(InventoryPlayer inv, TileRFAnvil te) {
		super(new ContainerRFAnvil(inv, te), te, inv.player, TEXTURE);

		myTile = te;

		generateInfo("tab." + TFTechness.MODID + ".machine.rfanvil", 1);
	}

	@Override
	public void initGui() {
		super.initGui();
		addElement(new ElementEnergyStored(this, 8, 8,
				myTile.getEnergyStorage()));
		this.progress = ((ElementDualScaled) addElement(new ElementDualScaled(
				this, 79, 34)
				.setMode(1)
				.setSize(24, 16)
				.setTexture("cofh:textures/gui/elements/Progress_Arrow_Right.png",
						64,
						16)));
	}

	@Override
	protected void updateElementInformation() {
		super.updateElementInformation();

		// this.slotInput.setVisible(this.myTile.hasSide(1));
		//this.slotOutput.setVisible(this.myTile.hasSide(2));

		this.progress.setQuantity(this.myTile.getScaledProgress(24));
		//this.speed.setQuantity(this.myTile.getScaledSpeed(16));
	}

}
