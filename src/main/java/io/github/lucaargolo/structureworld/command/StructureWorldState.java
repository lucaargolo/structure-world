package io.github.lucaargolo.structureworld.command;

import io.github.lucaargolo.structureworld.Mod;
import io.github.lucaargolo.structureworld.StructureCache;
import io.github.lucaargolo.structureworld.StructureChunkGenerator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.HashMap;
import java.util.UUID;

public class StructureWorldState extends PersistentState {

    private final HashMap<UUID, BlockPos> playerMap = new HashMap<>();
    private int x, y, dx, dy;

    public StructureWorldState(String key) {
        super(key);
        this.x = Mod.CONFIG.getPlatformDistanceRadius();
        this.y = 0;
        this.dx = Mod.CONFIG.getPlatformDistanceRadius();
        this.dy = 0;
    }

    public void deleteIsland(ServerPlayerEntity playerEntity) {
        playerMap.remove(playerEntity.getUuid());
    }

    public BlockPos getIsland(ServerPlayerEntity playerEntity) {
        return playerMap.get(playerEntity.getUuid());
    }

    public BlockPos generateIsland(ServerWorld world, ServerPlayerEntity playerEntity) {
        int x = this.x;
        int y = this.y;
        int dx = this.dx;
        int dy = this.dy;
        if (!playerMap.containsKey(playerEntity.getUuid())) {
            ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
            if (chunkGenerator instanceof StructureChunkGenerator) {
                MinecraftServer server = world.getServer();
                StructureManager structureManager = server.getStructureManager();
                StructureChunkGenerator structureChunkGenerator = (StructureChunkGenerator) chunkGenerator;
                Structure structure = StructureCache.getOrCreateCache(structureManager, structureChunkGenerator.getStructure());
                BlockPos playerSpawnOffset = structureChunkGenerator.getPlayerSpawnOffset();
                BlockPos structureOffset = structureChunkGenerator.getStructureOffset();

                BlockPos origin = new BlockPos(8, 64, 8);
                BlockPos island = origin.add(this.x, 0, this.y);

                if ((x == y) || (x < 0 && x == -y) || (x > 0 && x == Mod.CONFIG.getPlatformDistanceRadius()-y)) {
                    this.dx = -dy;
                    this.dy = dx;
                }
                this.x = x+this.dx;
                this.y = y+this.dy;

                if (structure != null) {
                    structure.place(world, island.add(structureOffset), new StructurePlacementData(), world.random);
                }
                playerMap.put(playerEntity.getUuid(), island.add(playerSpawnOffset));
            } else {
                playerMap.put(playerEntity.getUuid(), BlockPos.ORIGIN);
            }
            markDirty();
        }
        return playerMap.get(playerEntity.getUuid());
    }

    @Override
    public void fromTag(CompoundTag tag) {
        playerMap.clear();
        CompoundTag playerMapTag = tag.getCompound("playerMap");
        playerMapTag.getKeys().forEach( key -> {
            try {
                UUID uuid = UUID.fromString(key);
                BlockPos pos = BlockPos.fromLong(playerMapTag.getLong(key));
                playerMap.put(uuid, pos);
            }catch (IllegalArgumentException ignored) {}
        });
        this.x = tag.getInt("x");
        this.y = tag.getInt("y");
        this.dx = tag.getInt("dx");
        this.dy = tag.getInt("dy");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag playerMapTag = new CompoundTag();
        playerMap.forEach( (uuid, blockPos) -> playerMapTag.putLong(uuid.toString(), blockPos.asLong()));
        tag.put("playerMap", playerMapTag);
        tag.putInt("x", x);
        tag.putInt("y", y);
        tag.putInt("dx", dx);
        tag.putInt("dy", dy);
        return tag;
    }
}
