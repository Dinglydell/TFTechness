package dinglydell.tftechness.fluid;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockMoltenMetal extends BlockFluidClassic {
	
	public BlockMoltenMetal(Fluid fluid) {
		super(fluid, Material.lava);
		setBlockName(fluid.getName());
		
	}
}
