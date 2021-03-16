package io.github.lucaargolo.structureworld;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class ModConfig {

    public static class StructureWorldConfig {

        private final String structureIdentifier;
        private final String biomeIdentifier;
        private final int[] structureOffset;
        private final int[] playerSpawnOffset;
        private final boolean overridingDefault;

        public StructureWorldConfig(String structureIdentifier, String biomeIdentifier, int[] structureOffset, int[] playerSpawnOffset) {
            this.structureIdentifier = structureIdentifier;
            this.biomeIdentifier = biomeIdentifier;
            this.structureOffset = structureOffset;
            this.playerSpawnOffset = playerSpawnOffset;
            this.overridingDefault = false;
        }

        public boolean isOverridingDefault() {
            return overridingDefault;
        }

        public String getStructureIdentifier() {
            return structureIdentifier;
        }

        public String getBiomeIdentifier() { return biomeIdentifier; }

        public BlockPos getStructureOffset() {
            return new BlockPos(structureOffset[0], structureOffset[1], structureOffset[2]);
        }

        public BlockPos getPlayerSpawnOffset() {
            return new BlockPos(playerSpawnOffset[0], playerSpawnOffset[1], playerSpawnOffset[2]);
        }
    }

    private final int createPlatformPermissionLevel = 0;
    private final int teleportToPlatformPermissionLevel = 0;
    private final int platformDistanceRadius = 1000;

    private final List<StructureWorldConfig> structureWorldConfigs = Arrays.asList(
            new StructureWorldConfig("structureworld:simple_tree", "minecraft:forest", new int[]{-2, 0, -2}, new int[]{0, 8, 0}),
            new StructureWorldConfig("structureworld:classic_skyblock", "minecraft:plains", new int[]{-3, 0, -1}, new int[]{0, 3, 0})
    );

    public int getCreatePlatformPermissionLevel() {
        return createPlatformPermissionLevel;
    }

    public int getTeleportToPlatformPermissionLevel() {
        return teleportToPlatformPermissionLevel;
    }

    public int getPlatformDistanceRadius() {
        return platformDistanceRadius;
    }

    public List<StructureWorldConfig> getStructureWorldConfigs() {
        return structureWorldConfigs;
    }

}
