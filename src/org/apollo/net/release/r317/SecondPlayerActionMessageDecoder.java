package org.apollo.net.release.r317;

import org.apollo.game.message.impl.SecondPlayerActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SecondPlayerActionMessage}.
 * 
 * @author Major
 */
public final class SecondPlayerActionMessageDecoder extends MessageDecoder<SecondPlayerActionMessage> {

	@Override
	public SecondPlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new SecondPlayerActionMessage(index);
	}

}