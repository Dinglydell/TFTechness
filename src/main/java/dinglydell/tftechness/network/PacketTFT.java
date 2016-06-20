package dinglydell.tftechness.network;

import net.minecraft.entity.player.EntityPlayer;
import cofh.core.network.PacketCoFHBase;
import cofh.core.network.PacketHandler;
import dinglydell.tftechness.gui.container.ContainerRFCrucible;
import dinglydell.tftechness.gui.container.ContainerRFCrucible.CommandType;

public class PacketTFT extends PacketCoFHBase {

	enum PacketTFTTypes {
		RFCRUCIBLE
	}

	@Override
	public void handlePacket(EntityPlayer player, boolean isServer) {
		try {
			int type = getByte();

			switch (PacketTFTTypes.values()[type]) {
			case RFCRUCIBLE:
				if (player.openContainer instanceof ContainerRFCrucible) {
					((ContainerRFCrucible) player.openContainer)
							.handlePacket(this);
				}
				return;
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static void sendRFCruciblePacketToServer(CommandType type) {
		PacketHandler.sendToServer(getPacket(PacketTFTTypes.RFCRUCIBLE)
				.addByte(type.ordinal()));

	}

	private static PacketCoFHBase getPacket(PacketTFTTypes type) {
		return new PacketTFT().addByte(type.ordinal());
	}

}
