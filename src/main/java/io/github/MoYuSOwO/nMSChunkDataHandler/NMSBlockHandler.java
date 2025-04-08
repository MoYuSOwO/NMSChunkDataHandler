package io.github.MoYuSOwO.nMSChunkDataHandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.util.BitStorage;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class NMSBlockHandler extends ChannelDuplexHandler {

    private final BlockState grassBlock = Blocks.GRASS_BLOCK.defaultBlockState().setValue(BlockStateProperties.SNOWY, false);
    private final BlockState diamondBlock = Blocks.DIAMOND_BLOCK.defaultBlockState();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientboundBlockUpdatePacket packet) {
            BlockState state = (BlockState) ReflectionUtil.getField(packet, "blockState");
            if (state.is(Blocks.GRASS_BLOCK)) {
                ReflectionUtil.setField(packet, "blockState", Blocks.DIAMOND_BLOCK.defaultBlockState());
            }
        } else if (msg instanceof ClientboundLevelChunkWithLightPacket packet) {
            ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
            byte[] buffer = (byte[]) ReflectionUtil.getField(chunkData, "buffer");
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(buffer));
            FriendlyByteBuf newbuf = new FriendlyByteBuf(Unpooled.buffer());
            while (buf.readerIndex() < buffer.length) {
                int nonEmptyBlock = buf.readShort();
                newbuf.writeShort(nonEmptyBlock);
                int bitsPerBlock = buf.readByte();
                newbuf.writeByte(bitsPerBlock);
                if (bitsPerBlock == 0) {
                    int stateId = buf.readVarInt();
                    if (Block.stateById(stateId).equals(grassBlock)) {
                        newbuf.writeVarInt(Block.getId(diamondBlock));
                    } else {
                        newbuf.writeVarInt(stateId);
                    }
                    long[] data = buf.readLongArray();
                    newbuf.writeLongArray(data);
                } else if (bitsPerBlock <= 8) {
                    int sizeOfPalette = buf.readVarInt();
                    newbuf.writeVarInt(sizeOfPalette);
                    int[] palette = new int[sizeOfPalette];
                    for (int i = 0; i < sizeOfPalette; i++) {
                        palette[i] = buf.readVarInt();
                        if (Block.stateById(palette[i]).equals(grassBlock)) {
                            newbuf.writeVarInt(Block.getId(diamondBlock));
                        } else {
                            newbuf.writeVarInt(palette[i]);
                        }
                    }
                    long[] data = buf.readLongArray();
                    newbuf.writeLongArray(data);
                } else {
                    long[] data = buf.readLongArray();
                    BitStorage storage = new SimpleBitStorage(bitsPerBlock, 4096, data);
                    for (int pos = 0; pos < 4096; pos++) {
                        BlockState state = Block.stateById(storage.get(pos));
                        if (state.equals(grassBlock)) {
                            storage.set(pos, Block.getId(diamondBlock));
                        }
                    }
                    newbuf.writeLongArray(storage.getRaw());
                }
                int bitPerBiome = buf.readByte();
                newbuf.writeByte(bitPerBiome);
                if (bitPerBiome == 0) {
                    int sizeOfPalette = buf.readVarInt();
                    newbuf.writeVarInt(sizeOfPalette);
                    long[] data = buf.readLongArray();
                    newbuf.writeLongArray(data);
                } else if (bitPerBiome <= 3) {
                    int sizeOfPalette = buf.readVarInt();
                    newbuf.writeVarInt(sizeOfPalette);
                    int[] palette = new int[sizeOfPalette];
                    for (int i = 0; i < sizeOfPalette; i++) {
                        palette[i] = buf.readVarInt();
                        newbuf.writeVarInt(palette[i]);
                    }
                    long[] data = buf.readLongArray();
                    newbuf.writeLongArray(data);
                } else {
                    long[] data = buf.readLongArray();
                    newbuf.writeLongArray(data);
                }
            }
            ReflectionUtil.setField(chunkData, "buffer", newbuf.array());
        } else if (msg instanceof ClientboundSectionBlocksUpdatePacket packet) {
            BlockState[] states = (BlockState[]) ReflectionUtil.getField(packet, "states");
            for (int i = 0; i < states.length; i++) {
                if (states[i].equals(grassBlock)) {
                    states[i] = diamondBlock;
                }
            }
            ReflectionUtil.setField(packet, "states", states);
        }
        super.write(ctx, msg, promise);
    }
}
