package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.gui.element.ElementSimple;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;

import com.bioxx.tfc.api.TFCItems;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFAnvil;
import dinglydell.tftechness.gui.element.ElementButtonItem;
import dinglydell.tftechness.recipe.AnvilRecipeHandler;
import dinglydell.tftechness.tileentities.machine.TileRFAnvil;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class GuiRFAnvil extends GuiAugmentableBase {
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFAnvil.png");

	protected TileRFAnvil myTile;

	protected ElementSlotOverlay inputSlotABlue;
	protected ElementSlotOverlay inputSlotAGreen;
	protected ElementSlotOverlay inputSlotBBlue;
	protected ElementSlotOverlay inputSlotBPurple;
	protected ElementSlotOverlay outputSlotOrange;
	//protected ElementSlotOverlay tankSlotOrange;
	//protected ElementSlotOverlay tankSlotRed;

	protected ElementDualScaled progress;

	private ElementButton mode;
	private ElementButtonItem plan;

	private ElementSimple modeOverlay;

	//private TabInfo tabInfo;

	private EntityPlayer player;

	//private static ItemStack blueprint = new ItemStack(TFCItems.blueprint);

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
		this.outputSlotOrange = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 113, 31).setSlotInfo(Colours.orange.gui(), 0, 2));
		this.inputSlotABlue = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 34, 31).setSlotInfo(Colours.blue.gui(), 0, 2));
		this.inputSlotBBlue = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 58, 31).setSlotInfo(Colours.blue.gui(), 0, 2));
		this.inputSlotAGreen = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 34, 31).setSlotInfo(Colours.green.gui(), 0, 2));
		this.inputSlotBPurple = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 58, 31).setSlotInfo(Colours.purple.gui(), 0, 2));
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
		this.plan = (ElementButtonItem) addElement(new ElementButtonItem(this,
				57, 52, "Plans", new ItemStack(TFCItems.blueprint)));
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
		outputSlotOrange.setVisible(myTile.hasSide(2));
		inputSlotABlue.setVisible(myTile.hasSide(1));
		inputSlotBBlue.setVisible(myTile.hasSide(1));
		inputSlotAGreen.setVisible(myTile.hasSide(3));
		inputSlotBPurple.setVisible(myTile.hasSide(4));
		if (this.myTile.weldMode) {
			this.mode.setToolTip("info.TFTechness.RFAnvil.toggleWeldOff");
			this.mode.setSheetX(176);
			this.mode.setHoverX(176);
			//this.modeOverlay.setVisible(false);
			this.plan.setVisible(false);
		} else {
			this.mode.setToolTip("info.TFTechness.RFAnvil.toggleWeldOn");
			this.mode.setSheetX(192);
			this.mode.setHoverX(192);
			this.plan.setVisible(true);
			//this.modeOverlay.setVisible(true);
		}
		this.progress.setQuantity(this.myTile.getScaledProgress(24));
		//AnvilManager manager = AnvilManager.getInstance();
		if (myTile.getPlan().equals("")) {
			this.plan.setItem(new ItemStack(TFCItems.blueprint));
			this.plan.setTooltip(StringHelper
					.localize("info.TFTechness.RFAnvil.plan.none"));
		} else {
			ItemStack item = myTile.getResult();
			this.plan.setTooltip(StringHelper
					.localize("info.TFTechness.RFAnvil.plan")
					+ StringHelper.localize("gui.plans." + myTile.getPlan()));
			this.plan.setItem(item != null ? item : AnvilRecipeHandler
					.getResultFromPlan(myTile.getPlan()));
		}
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
