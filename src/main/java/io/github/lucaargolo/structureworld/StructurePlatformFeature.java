package io.github.lucaargolo.structureworld;

import com.mojang.serialization.Codec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class StructurePlatformFeature extends Feature<DefaultFeatureConfig> {

    public StructurePlatformFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig) {
        if(!blockPos.isWithinDistance(BlockPos.ORIGIN, 2) || !(chunkGenerator instanceof StructureChunkGenerator)) {
            return false;
        }

        MinecraftServer server = structureWorldAccess.toServerWorld().getServer();
        StructureManager structureManager = server.getStructureManager();
        StructureChunkGenerator structureChunkGenerator = (StructureChunkGenerator) chunkGenerator;
        Structure structure = StructureCache.getOrCreateCache(structureManager, structureChunkGenerator.getStructure());
        BlockPos structureOffset = structureChunkGenerator.getStructureOffset();

        BlockPos offsetedPos = blockPos.add(structureOffset);

        if(structure != null) {
            structure.place(structureWorldAccess, offsetedPos.add(8, 64, 8), new StructurePlacementData(), random);
            return true;
        }

        return false;
    }

}