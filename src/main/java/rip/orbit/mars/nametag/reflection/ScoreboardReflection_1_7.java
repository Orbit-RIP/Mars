package rip.orbit.mars.nametag.reflection;

import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardScore;
import net.minecraft.server.v1_7_R4.PacketPlayOutScoreboardTeam;
import rip.orbit.mars.nametag.util.ReflectionUtils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class ScoreboardReflection_1_7 {

    public static class PacketPlayOutScoreboardTeamWrapper {

        private static MethodHandle NAME_SETTER;
        private static MethodHandle DISPLAY_NAME_SETTER;
        private static MethodHandle PREFIX_SETTER;
        private static MethodHandle SUFFIX_SETTER;
        private static MethodHandle ACTION_SETTER;

        static {
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();

                NAME_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "a"));
                DISPLAY_NAME_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "b"));
                PREFIX_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "c"));
                SUFFIX_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "d"));
                ACTION_SETTER = lookup.unreflectSetter(ReflectionUtils.setAccessibleAndGet(PacketPlayOutScoreboardTeam.class, "f"));
            } catch(Throwable t) {
                t.printStackTrace();
            }
        }

        public static PacketPlayOutScoreboardTeam updateTeam(String name, String prefix, String suffix) {
            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

            try {
                NAME_SETTER.invokeExact(packet, name);
                DISPLAY_NAME_SETTER.invokeExact(packet, name);
                PREFIX_SETTER.invokeExact(packet, prefix);
                SUFFIX_SETTER.invokeExact(packet, suffix);
                ACTION_SETTER.invokeExact(packet, 2);
            } catch(Throwable t) {
                t.printStackTrace();
            }

            return packet;
        }
    }
}
