package io.github.lucaargolo.structureworld;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.github.lucaargolo.structureworld.command.StructureWorldCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Mod implements ModInitializer {

    public static final String MOD_ID = "structureworld";
    public static final Logger LOGGER = LogManager.getLogger("Structure World");
    public static ModConfig CONFIG;

    public static RegistryKey<ConfiguredFeature<?, ?>> CONFIGURED_STRUCTURE_PLATFORM_KEY = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(MOD_ID, "structure_platform_feature"));

    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @SuppressWarnings("deprecation")
    @Override
    public void onInitialize() {
        Feature<DefaultFeatureConfig> rawFeature = Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "structure_feature"), new StructurePlatformFeature(DefaultFeatureConfig.CODEC));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, CONFIGURED_STRUCTURE_PLATFORM_KEY.getValue(), rawFeature.configure(FeatureConfig.DEFAULT));
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(MOD_ID, "structure_chunk_generator"), StructureChunkGenerator.CODEC);
        BiomeModifications.addFeature(BiomeSelectors.all(), GenerationStep.Feature.TOP_LAYER_MODIFICATION, CONFIGURED_STRUCTURE_PLATFORM_KEY);

        Path configPath = FabricLoader.getInstance().getConfigDir();
        File configFile = new File(configPath + File.separator + "structureworld.json");

        LOGGER.info("Trying to read config file...");
        try {
            if (configFile.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...");
                String json = gson.toJson(parser.parse(gson.toJson(new ModConfig())));
                try (PrintWriter out = new PrintWriter(configFile)) {
                    out.println(json);
                }
                CONFIG = new ModConfig();
                LOGGER.info("Successfully created default config file with "+CONFIG.getStructureWorldConfigs().size()+" custom structure worlds.");
            } else {
                LOGGER.info("A config file was found, loading it..");
                CONFIG = gson.fromJson(new String(Files.readAllBytes(configFile.toPath())), ModConfig.class);
                if(CONFIG == null) {
                    throw new NullPointerException("The config file was empty.");
                }else{
                    LOGGER.info("Successfully loaded config file with "+CONFIG.getStructureWorldConfigs().size()+" custom structure worlds.");
                }
            }
        }catch (Exception exception) {
            LOGGER.error("There was an error creating/loading the config file!", exception);
            CONFIG = new ModConfig();
            LOGGER.warn("Defaulting to original config with "+CONFIG.getStructureWorldConfigs().size()+" custom structure worlds.");
        }

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> StructureWorldCommand.register(dispatcher)));

    }


}
