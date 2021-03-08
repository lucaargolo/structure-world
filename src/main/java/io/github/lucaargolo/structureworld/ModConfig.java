package io.github.lucaargolo.structureworld;

import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class ModConfig {

    public static class PosConfig {

        private final int x, y, z;

        public PosConfig(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public BlockPos asBlockPos() {
            return new BlockPos(this.x, this.y, this.z);
        }
    }

    public static class StructureWorldConfig {

        private final String structureIdentifier;
        private final PosConfig structureOffset;
        private final PosConfig playerSpawnOffset;
        private final boolean overridingDefault;

        public StructureWorldConfig(String structureIdentifier, PosConfig structureOffset, PosConfig playerSpawnOffset) {
            this.structureIdentifier = structureIdentifier;
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

        public BlockPos getStructureOffset() {
            return structureOffset.asBlockPos();
        }

        public BlockPos getPlayerSpawnOffset() {
            return playerSpawnOffset.asBlockPos();
        }
    }

    private final List<StructureWorldConfig> structureWorldConfigs = Arrays.asList(
            new StructureWorldConfig("structureworld:simple_tree", new PosConfig(-2, 0, -2), new PosConfig(0, 8, 0)),
            new StructureWorldConfig("structureworld:classic_skyblock", new PosConfig(-3, 0, -1), new PosConfig(0, 3, 0))
    );

    public List<StructureWorldConfig> getStructureWorldConfigs() {
        return structureWorldConfigs;
    }

}
