package io.github.lucaargolo.structureworld.mixin;

import io.github.lucaargolo.structureworld.StructureChunkGenerator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(at = @At("TAIL"), method = "setupSpawn")
    private static void setupSpawn(ServerWorld world, ServerWorldProperties worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo ci) {
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        if(chunkGenerator instanceof StructureChunkGenerator structureChunkGenerator) {
            BlockPos offsetedSpawnPos = new BlockPos(8, 64, 8).add(structureChunkGenerator.getPlayerSpawnOffset());
            world.setSpawnPos(offsetedSpawnPos, world.getSpawnAngle());
        }
    }

}
