package io.github.lucaargolo.structureworld;

import io.github.lucaargolo.structureworld.mixin.GeneratorTypeAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public class ModClient implements ClientModInitializer {

    public static GeneratorType OVERRIDED_GENERATOR_TYPE = null;

    @Override
    public void onInitializeClient() {

        Mod.CONFIG.getStructureWorldConfigs().forEach(structureWorldConfig -> {
            String structure = structureWorldConfig.getStructureIdentifier();
            RegistryKey<Biome> biomeKey = RegistryKey.of(Registry.BIOME_KEY, new Identifier(structureWorldConfig.getBiomeIdentifier()));
            GeneratorType generatorType = new GeneratorType(structure) {
                protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                    return new StructureChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(biomeKey)), structure, structureWorldConfig.getStructureOffset(), structureWorldConfig.getPlayerSpawnOffset());
                }
            };

            if(structureWorldConfig.isOverridingDefault()) {
                GeneratorTypeAccessor.getValues().add(0, generatorType);
                OVERRIDED_GENERATOR_TYPE = generatorType;
                Mod.LOGGER.info("Successfully registered "+structure+" generator type. (Overriding default)");
            }else{
                GeneratorTypeAccessor.getValues().add(generatorType);
                Mod.LOGGER.info("Successfully registered "+structure+" generator type.");
            }

        });

    }
}
