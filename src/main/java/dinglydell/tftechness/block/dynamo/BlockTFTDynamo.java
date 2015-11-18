package dinglydell.tftechness.block.dynamo;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.core.TEProps;
import dinglydell.tftechness.block.BlockTETFTBase;
import dinglydell.tftechness.tileentities.dynamo.TileTFTDynamoSteam;

public class BlockTFTDynamo extends BlockTETFTBase {
	
	public static ItemStack dynamoSteam;
	
	public enum Types {
		STEAM
	}
	
	public BlockTFTDynamo() {
		super(Material.iron);
		this.setHardness(15.0f);
		this.setResistance(25.0f);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if (meta >= Types.values().length) {
			return null;
		}
		switch (Types.values()[meta]) {
			case STEAM:
				return new TileTFTDynamoSteam();
				
		}
		return null;
		
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (Types t : Types.values()) {
			IconRegistry.addIcon("Dynamo" + t.ordinal(),
					"thermalexpansion:dynamo/Dynamo_" + StringHelper.titleCase(t.name().toLowerCase()),
					reg);
		}
	}
	
	@Override
	public int getRenderBlockPass() {
		return 0;
	}
	
	@Override
	public int getRenderType() {
		return TEProps.renderIdDynamo;
	}
	
	@Override
	protected ItemStack setDefaultTag(ItemStack itemStack) {
		return ItemBlockTFTDynamo.setDefaultTag(itemStack);
	}
	
	@Override
	protected int getNumBlocks() {
		return Types.values().length;
	}
	
}
