package io.github.lucaargolo.structureworld.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.lucaargolo.structureworld.Mod;
import io.github.lucaargolo.structureworld.StructureChunkGenerator;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class StructureWorldCommand {

    private static final SimpleCommandExceptionType INVALID_CHUNK_GENERATOR = new SimpleCommandExceptionType(new TranslatableText("commands.structureworld.invalid_chunk_generator"));
    private static final SimpleCommandExceptionType ISLAND_FOR_UUID_ALREADY_EXISTS = new SimpleCommandExceptionType(new TranslatableText("commands.structureworld.island_for_uuid_already_exists"));
    private static final SimpleCommandExceptionType NO_ISLAND_FOR_UUID = new SimpleCommandExceptionType(new TranslatableText("commands.structureworld.no_island_for_uuid"));

    private static final LiteralArgumentBuilder<ServerCommandSource> create = CommandManager
            .literal("create")
            .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(Mod.CONFIG.getCreatePlatformPermissionLevel()))
            .executes(context -> {
                if(isNotStructureWorld(context)) {
                    throw INVALID_CHUNK_GENERATOR.create();
                }
                ServerWorld serverWorld = context.getSource().getWorld();
                ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                if(islandPos != null) {
                    throw ISLAND_FOR_UUID_ALREADY_EXISTS.create();
                }
                islandPos = structureWorldState.generateIsland(serverWorld, playerEntity);
                playerEntity.teleport(islandPos.getX(), islandPos.getY(), islandPos.getZ());
                context.getSource().sendFeedback(new TranslatableText("commands.structureworld.created_island", playerEntity.getDisplayName()), false);
                return 1;
            })
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .executes(context -> {
                        if(isNotStructureWorld(context)) {
                            throw INVALID_CHUNK_GENERATOR.create();
                        }
                        ServerWorld serverWorld = context.getSource().getWorld();
                        ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "player");
                        StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                        BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                        if(islandPos != null) {
                            throw ISLAND_FOR_UUID_ALREADY_EXISTS.create();
                        }
                        islandPos = structureWorldState.generateIsland(serverWorld, playerEntity);
                        playerEntity.teleport(islandPos.getX(), islandPos.getY(), islandPos.getZ());
                        context.getSource().sendFeedback(new TranslatableText("commands.structureworld.created_island", playerEntity.getDisplayName()), true);
                        return 1;
                    })
            );

    private static final LiteralArgumentBuilder<ServerCommandSource> delete = CommandManager
            .literal("delete")
            .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(Mod.CONFIG.getCreatePlatformPermissionLevel()))
            .executes(context -> {
                if(isNotStructureWorld(context)) {
                    throw INVALID_CHUNK_GENERATOR.create();
                }
                ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                if(islandPos == null) {
                    throw NO_ISLAND_FOR_UUID.create();
                }
                structureWorldState.deleteIsland(playerEntity);
                context.getSource().sendFeedback(new TranslatableText("commands.structureworld.deleted_island", playerEntity.getDisplayName()), false);
                return 1;
            })
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .executes(context -> {
                        if(isNotStructureWorld(context)) {
                            throw INVALID_CHUNK_GENERATOR.create();
                        }
                        ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "player");
                        StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                        BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                        if(islandPos == null) {
                            throw NO_ISLAND_FOR_UUID.create();
                        }
                        structureWorldState.deleteIsland(playerEntity);
                        context.getSource().sendFeedback(new TranslatableText("commands.structureworld.deleted_island", playerEntity.getDisplayName()), true);
                        return 1;
                    })
            );

    private static final LiteralArgumentBuilder<ServerCommandSource> teleport = CommandManager
            .literal("teleport")
            .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(Mod.CONFIG.getTeleportToPlatformPermissionLevel()))
            .executes(context -> {
                if(isNotStructureWorld(context)) {
                    throw INVALID_CHUNK_GENERATOR.create();
                }
                ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                if(islandPos == null) {
                    throw NO_ISLAND_FOR_UUID.create();
                }
                playerEntity.teleport(islandPos.getX(), islandPos.getY(), islandPos.getZ());
                context.getSource().sendFeedback(new TranslatableText("commands.structureworld.teleported_to_island", playerEntity.getDisplayName()), false);
                return 1;
            })
            .then(CommandManager.argument("player", EntityArgumentType.player())
                    .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                    .executes(context -> {
                        if(isNotStructureWorld(context)) {
                            throw INVALID_CHUNK_GENERATOR.create();
                        }
                        ServerPlayerEntity playerEntity = EntityArgumentType.getPlayer(context, "player");
                        StructureWorldState structureWorldState = context.getSource().getWorld().getPersistentStateManager().getOrCreate(StructureWorldState::createFromNbt, StructureWorldState::new, "structureIslands");
                        BlockPos islandPos = structureWorldState.getIsland(playerEntity);
                        if(islandPos == null) {
                            throw NO_ISLAND_FOR_UUID.create();
                        }
                        playerEntity.teleport(islandPos.getX(), islandPos.getY(), islandPos.getZ());
                        context.getSource().sendFeedback(new TranslatableText("commands.structureworld.teleported_to_island", playerEntity.getDisplayName()), true);
                        return 1;
                    })
            );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralCommandNode<ServerCommandSource> mainNode = dispatcher.register(
                CommandManager.literal("structureworld")
                        .then(create)
                        .then(delete)
                        .then(teleport)
        );
        dispatcher.register(
                CommandManager.literal("sw").redirect(mainNode)
        );
    }

    private static boolean isNotStructureWorld(CommandContext<ServerCommandSource> context) {
        return !(context.getSource().getWorld().getChunkManager().getChunkGenerator() instanceof StructureChunkGenerator);
    }

}
