package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.gui.element.ElementSimple;
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
	//protected ElementSlotOverlay tankSlotOrange;
	//protected ElementSlotOverlay tankSlotRed;
	protected ElementSlotOverlay moldSlotOrange;
	protected ElementSlotOverlay moldSlotYellow;

	protected ElementDualScaled progress;

	private ElementButton mode;
	private ElementButton plan;

	private ElementSimple modeOverlay;

	//private TabInfo tabInfo;

	private EntityPlayer player;

	public GuiRFAnvil(InventoryPlayer inv, TileRFAnvil te) {
		super(new ContainerRFAnvil(inv, te), te, inv.player, TEXTURE);

		myTile = te;
		player = inv.player;
		generateInfo("tab." + TFTechness.MODID + ".machine.rfanvil", 1);
	}

	//
	//private void updateInfo() {
	//	generateInfo("tab." + TFTechness.MODID + ".machine.rfanvil", 1);
	//		if (myTile.inventory[0] != null) {
	//			myInfo += "\n\n"
	//					+ StringHelper.localize(myTile.inventory[0]
	//							.getUnlocalizedName() + ".name");
	//			if (myTile.weldMode) {
	//				myInfo += "\n\n"
	//						+ StringHelper.localize("tab." + TFTechness.MODID
	//								+ ".machine.rfanvil.weld");
	//			} else {
	//				myInfo += "\n\n"
	//						+ StringHelper.localize("tab." + TFTechness.MODID
	//								+ ".machine.rfanvil.work");
	//			}
	//			int cost = myTile.getRecipeCost();
	//			myInfo += (cost == -1) ? "Recipe Not Found" : (cost + "RF");
	//		}
	//		addTab(arg0)
	//	}

	@Override
	public void initGui() {
		super.initGui();
		//for (TabBase t : tabs) {
		//	if (t instanceof TabInfo) {
		//		tabInfo = (TabInfo) t;
		//		break;
		//	}
		//}

		addElement(new ElementEnergyStored(this, 8, 8,
				myTile.getEnergyStorage()));
		this.progress = ((ElementDualScaled) addElement(new ElementDualScaled(
				this, 79, 31)
				.setMode(1)
				.setSize(24, 16)
				.setTexture("cofh:textures/gui/elements/Progress_Arrow_Right.png",
						64,
						16)));

		this.mode = (ElementButton) addElement(new ElementButton(this, 80, 53,
				"Mode", 176, 0, 176, 16, 176, 32, 16, 16, TFTechness.MODID
						+ ":textures/gui/machine/RFAnvil.png"));
		this.plan = (ElementButton) addElement(new ElementButton(this, 58, 31,
				"Plans", 176, 48, 176, 48, 176, 48, 16, 16, TFTechness.MODID
						+ ":textures/gui/machine/RFAnvil.png"));
		//this.modeOverlay = ((ElementSimple) addElement(new ElementSimple(this,
		//		58, 31)
		//		.setTextureOffsets(176, 48)
		//		.setSize(16, 16)
		//		.setTexture(TFTechness.MODID
		//				+ ":textures/gui/machine/RFAnvil.png",
		//				256,
		//				256)));
	}

	@Override
	protected void updateElementInformation() {
		super.updateElementInformation();
		// this.slotInput.setVisible(this.myTile.hasSide(1));
		//this.slotOutput.setVisible(this.myTile.hasSide(2));
		if (this.myTile.weldMode) {
			this.mode.setToolTip("info.TFTechness.toggleAnvilWeldOff");
			this.mode.setSheetX(176);
			this.mode.setHoverX(176);
			//this.modeOverlay.setVisible(false);
			this.plan.setVisible(false);
		} else {
			this.mode.setToolTip("info.TFTechness.toggleAnvilWeldOn");
			this.mode.setSheetX(192);
			this.mode.setHoverX(192);
			this.plan.setVisible(true);
			//this.modeOverlay.setVisible(true);
		}
		this.progress.setQuantity(this.myTile.getScaledProgress(24));

		//this.speed.setQuantity(this.myTile.getScaledSpeed(16));
	}

	public void handleElementButtonClick(String button, int paramInt) {
		if (button.equals("Mode")) {
			playSound("random.click", 1.0F, 0.8F);
			this.myTile.setMode(!this.myTile.weldMode);
		} else if (button.equals("Plans")) {
			player.openGui(TFTechness.getInstance(),
					TFTGuiHandler.TFTGui.AnvilPlanSelection.ordinal(),
					player.worldObj,
					myTile.xCoord,
					myTile.yCoord,
					myTile.zCoord);
		}

	}
}
