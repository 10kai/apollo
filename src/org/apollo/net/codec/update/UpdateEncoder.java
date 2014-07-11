package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import org.apollo.fs.FileDescriptor;

/**
 * A {@link MessageToMessageEncoder} for the 'on-demand' protocol.
 * 
 * @author Graham
 */
public final class UpdateEncoder extends MessageToMessageEncoder<OnDemandResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, OnDemandResponse response, List<Object> out) {
		FileDescriptor descriptor = response.getFileDescriptor();
		int fileSize = response.getFileSize();
		int chunkId = response.getChunkId();
		ByteBuf chunkData = response.getChunkData();

		ByteBuf buffer = ctx.alloc().buffer(6 + chunkData.readableBytes());
		buffer.writeByte(descriptor.getType() - 1);
		buffer.writeShort(descriptor.getFile());
		buffer.writeShort(fileSize);
		buffer.writeByte(chunkId);
		buffer.writeBytes(chunkData);

		out.add(buffer);
	}

}