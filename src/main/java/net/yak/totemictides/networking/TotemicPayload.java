package net.yak.totemictides.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.yak.totemictides.TotemicTides;

public record TotemicPayload(int entityId, int totemType) implements CustomPayload {

    public static final Identifier PACKET_ID = TotemicTides.id("totemic");
    public static final CustomPayload.Id<TotemicPayload> ID = new CustomPayload.Id<>(PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, TotemicPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, TotemicPayload::entityId, PacketCodecs.INTEGER, TotemicPayload::totemType, TotemicPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

}
