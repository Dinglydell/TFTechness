package dinglydell.tftechness.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementFluidTank;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import cofh.thermalexpansion.gui.container.ContainerTEBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.tileentities.machine.TileTFTAccumulator;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class GuiTFTAccumulator extends GuiAugmentableBase {

	static final int slotOutputIndex = 0;
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/Accumulator.png");
	protected ElementBase slotOutput;
	protected ElementBase slotOutputRed;
	protected ElementBase slotOutput2;
	protected ElementBase slotOutput2Yellow;

	public GuiTFTAccumulator(InventoryPlayer inv, TileEntity te) {
		super(new ContainerTEBase(inv, te), te, inv.player, TEXTURE);

		generateInfo("tab.thermalexpansion.machine.accumulator", 3);
	}

	@Override
	public void initGui() {
		super.initGui();
		// fresh water tank orange highlight (output configuration display)
		slotOutput = addElement(new ElementSlotOverlay(this, 152, 9)
				.setSlotInfo(Colours.orange.gui(), 3, 2));
		// salt water tank orange highlight
		slotOutput2 = addElement(new ElementSlotOverlay(this, 8, 9)
				.setSlotInfo(Colours.orange.gui(), 3, 2));
		// fresh water tank red highlight
		slotOutputRed = addElement(new ElementSlotOverlay(this, 152, 9)
				.setSlotInfo(Colours.red.gui(), 3, 2));
		// salt water tank yellow highlight
		slotOutput2Yellow = addElement(new ElementSlotOverlay(this, 8, 9)
				.setSlotInfo(Colours.yellow.gui(), 3, 2));

		// fresh water tank
		addElement(new ElementFluidTank(this, 152, 9, myTile.getTank())
				.setAlwaysShow(true));
		// salt water tank
		addElement(new ElementFluidTank(this, 8, 9,
				((TileTFTAccumulator) myTile).getTank2()).setAlwaysShow(true));
	}

	@Override
	protected void updateElementInformation() {
		super.updateElementInformation();
		slotOutput.setVisible(myTile.hasSide(3));
		slotOutputRed.setVisible(myTile.hasSide(1));
		slotOutput2.setVisible(myTile.hasSide(3));
		slotOutput2Yellow.setVisible(myTile.hasSide(2));

	}

}
