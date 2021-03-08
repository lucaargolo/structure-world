package io.github.lucaargolo.structureworld;

import io.github.lucaargolo.structureworld.mixin.GeneratorOptionsMixin;
import io.github.lucaargolo.structureworld.mixin.GeneratorTypeAccessor;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Properties;

public class ModServer implements DedicatedServerModInitializer {

    public static String OVERRIDED_LEVEL_TYPE = null;

    @Override
    public void onInitializeServer() {

        Mod.CONFIG.getStructureWorldConfigs().forEach(structureWorldConfig -> {
            Identifier structureIdentifier = new Identifier(structureWorldConfig.getStructureIdentifier());

            if(structureWorldConfig.isOverridingDefault()) {
                OVERRIDED_LEVEL_TYPE = "structure_"+structureIdentifier.getPath();
                Mod.LOGGER.info("Overridden default level-type to "+OVERRIDED_LEVEL_TYPE+" generator type.");
            }

        });

    }

    public static void fromPropertiesHook(DynamicRegistryManager dynamicRegistryManager, Properties properties, CallbackInfoReturnable<GeneratorOptions> info) {
        String levelType = properties.getProperty("level-type");
        if(levelType != null && levelType.startsWith("structure_")) {
            Mod.CONFIG.getStructureWorldConfigs().forEach(structureWorldConfig -> {
                Identifier structureIdentifier = new Identifier(structureWorldConfig.getStructureIdentifier());

                if(levelType.equals("structure_"+structureIdentifier.getPath())) {
                    Registry<DimensionType> dimensionTypeRegistry = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
                    Registry<Biome> biomeRegistry = dynamicRegistryManager.get(Registry.BIOME_KEY);
                    Registry<ChunkGeneratorSettings> noiseSettingsRegistry = dynamicRegistryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
                    SimpleRegistry<DimensionOptions> simpleRegistry = DimensionType.createDefaultDimensionOptions(dimensionTypeRegistry, biomeRegistry, noiseSettingsRegistry, 0L);

                    StructureChunkGenerator structureChunkGenerator = new StructureChunkGenerator(new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.FOREST)), structureIdentifier, structureWorldConfig.getStructureOffset(), structureWorldConfig.getPlayerSpawnOffset());
                    SimpleRegistry<DimensionOptions> finalRegistry = GeneratorOptions.method_28608(dimensionTypeRegistry, simpleRegistry, structureChunkGenerator);
                    info.setReturnValue(new GeneratorOptions(0L, false, false, finalRegistry));
                }
            });
        }
    }
}
