package io.github.lucaargolo.structureworld.mixin;

import io.github.lucaargolo.structureworld.Mod;
import io.github.lucaargolo.structureworld.StructureChunkGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public class BiomeMixin {

    @Inject(at = @At("HEAD"), method = "generateFeatureStep", cancellable = true)
    public void generateFeatureStep(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo info) {
        if(chunkGenerator instanceof StructureChunkGenerator) {
            Mod.generateStructureFeature(region, chunkGenerator, random, pos);
            info.cancel();
        }
    }

}
