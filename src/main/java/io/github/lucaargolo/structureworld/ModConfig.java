package io.github.lucaargolo.structureworld;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal"})
public class ModConfig {

    public static class StructureWorldConfig {

        private final String structureIdentifier;
        private final String biomeIdentifier;
        private final int[] structureOffset;
        private final int[] playerSpawnOffset;

        private String fillmentBlockIdentifier;
        private boolean topBedrockEnabled;
        private boolean bottomBedrockEnabled;
        private boolean isBedrockFlat;

        private final boolean overridingDefault;

        public StructureWorldConfig(String structureIdentifier, String biomeIdentifier, int[] structureOffset, int[] playerSpawnOffset) {
            this.structureIdentifier = structureIdentifier;
            this.biomeIdentifier = biomeIdentifier;
            this.structureOffset = structureOffset;
            this.playerSpawnOffset = playerSpawnOffset;
            this.overridingDefault = false;
            this.fillmentBlockIdentifier = "minecraft:air";
            this.topBedrockEnabled = false;
            this.bottomBedrockEnabled = false;
            this.isBedrockFlat = false;
        }

        public StructureWorldConfig(String structureIdentifier, String biomeIdentifier, int[] structureOffset, int[] playerSpawnOffset, String fillmentBlockIdentifier, boolean topBedrockEnabled, boolean bottomBedrockEnabled) {
            this(structureIdentifier, biomeIdentifier, structureOffset, playerSpawnOffset);
            this.fillmentBlockIdentifier = fillmentBlockIdentifier;
            this.topBedrockEnabled = topBedrockEnabled;
            this.bottomBedrockEnabled = bottomBedrockEnabled;
            this.isBedrockFlat = false;
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

        public String getFillmentBlockIdentifier() {
            return fillmentBlockIdentifier;
        }

        public boolean isTopBedrockEnabled() {
            return topBedrockEnabled;
        }

        public boolean isBottomBedrockEnabled() {
            return bottomBedrockEnabled;
        }

        public boolean isBedrockFlat() {
            return isBedrockFlat;
        }
    }

    private int createPlatformPermissionLevel = 0;
    private int teleportToPlatformPermissionLevel = 0;
    private int platformDistanceRadius = 1000;

    private final List<StructureWorldConfig> structureWorldConfigs = Arrays.asList(
            new StructureWorldConfig("simple_tree", "minecraft:forest", new int[]{-2, 0, -2}, new int[]{0, 8, 0}),
            new StructureWorldConfig("classic_skyblock", "minecraft:plains", new int[]{-3, 0, -1}, new int[]{0, 3, 0}),
            new StructureWorldConfig("stoneblock", "minecraft:plains", new int[]{-5, -2, -5}, new int[]{0, 0, 0}, "minecraft:stone", true, true)
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
