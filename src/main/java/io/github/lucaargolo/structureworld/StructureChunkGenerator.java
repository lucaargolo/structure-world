package io.github.lucaargolo.structureworld;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import java.util.Optional;

public class StructureChunkGenerator extends ChunkGenerator {

    private final String structure;
    private final BlockPos structureOffset;
    private final BlockPos playerSpawnOffset;

    public static final Codec<StructureChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource),
                    Codec.STRING.stable().fieldOf("structure").forGetter((generator) -> generator.structure),
                    BlockPos.CODEC.fieldOf("structureOffset").forGetter((generator) -> generator.structureOffset),
                    BlockPos.CODEC.fieldOf("playerSpawnOffset").forGetter((generator) -> generator.playerSpawnOffset)
            ).apply(instance, instance.stable(StructureChunkGenerator::new))
    );

    public StructureChunkGenerator(BiomeSource biomeSource, String structure, BlockPos structureOffset, BlockPos playerSpawnOffset) {
        super(biomeSource, new StructuresConfig(false));
        this.structure = structure;
        this.structureOffset = structureOffset;
        this.playerSpawnOffset = playerSpawnOffset;
    }

    public String getStructure() {
        return structure;
    }

    public BlockPos getStructureOffset() {
        return structureOffset;
    }

    public BlockPos getPlayerSpawnOffset() {
        return playerSpawnOffset;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return this;
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {
    }

    @Override
    public void populateNoise(WorldAccess world, StructureAccessor accessor, Chunk chunk) {
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmapType) {
        return 0;
    }

    @Override
    public BlockView getColumnSample(int x, int z) {
        return new VerticalBlockSample(new BlockState[0]);
    }

    @Override
    public StructuresConfig getStructuresConfig() {
        return new StructuresConfig(Optional.empty(), Maps.newHashMap());
    }


}
