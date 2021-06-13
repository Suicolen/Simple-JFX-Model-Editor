package tutorial.rs.buffer.util;

import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ByteBufUtils {

    public static void writeUnsignedSmart(int value, ByteBuf buf) {
        if (value < 64 && value >= -64) {
            buf.writeByte(value + 64);
            return;
        }
        if (value < 16384 && value >= -16384) {

            buf.writeShort(value + 49152);
        } else {
            System.err.println("Error: out of range(writeUnsignedSmart): " + value);
        }
    }

    public static void writeSmart(int value, ByteBuf buf) {
        if (value >= 128) {
            buf.writeShort(value + 32768);
        } else {
            buf.writeByte(value);
        }
    }

}
