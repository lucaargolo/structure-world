package io.github.lucaargolo.structureworld;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import io.github.lucaargolo.structureworld.command.StructureWorldCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Mod implements ModInitializer {

    public static final String MOD_ID = "structureworld";
    public static final Logger LOGGER = LogManager.getLogger("Structure World");
    public static final HashMap<String, Structure> STRUCTURES = Maps.newHashMap();
    public static ModConfig CONFIG;

    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onInitialize() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(MOD_ID, "structure_chunk_generator"), StructureChunkGenerator.CODEC);

        Path configPath = FabricLoader.getInstance().getConfigDir();
        File structuresFolder = new File(configPath + File.separator + "structureworld" + File.separator + "structures");
        File configFile = new File(configPath + File.separator + "structureworld" + File.separator + "structureworld.json");

        LOGGER.info("Trying to read structures folder...");
        try {
            if(!structuresFolder.exists()) {
                LOGGER.info("No structures folder found, creating a new one...");
                if(structuresFolder.mkdirs()) {
                    Path builtinStructuresFolderPath = FabricLoader.getInstance().getModContainer("structureworld").orElseThrow(() -> new Exception("Couldn't find ModContainer")).getPath("structures");
                    List<Path> builtinStructuresPath = Files.walk(builtinStructuresFolderPath).filter(Files::isRegularFile).collect(Collectors.toList());
                    for(Path builtinStructurePath : builtinStructuresPath) {
                        InputStream builtinStructureInputStream = Files.newInputStream(builtinStructurePath);
                        File outputFile = new File(structuresFolder, builtinStructurePath.getFileName().toString());
                        if(outputFile.createNewFile()) {
                            FileOutputStream structureOutputStream = new FileOutputStream(outputFile);
                            IOUtils.copy(builtinStructureInputStream, structureOutputStream);
                        }
                    }
                    LOGGER.info("Successfully created structures folder.");
                }else{
                    throw new Exception("Failed while creating structures folder.");
                }
            }
            if(structuresFolder.exists()) {
                LOGGER.info("Found structures folder, loading structures...");
                File[] files = structuresFolder.listFiles(pathname -> pathname.exists() && pathname.isFile() && pathname.getName().endsWith(".nbt"));
                for (File file : files) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
                    NbtCompound structure = NbtIo.readCompressed(dataInputStream);
                    Structure loadedStructure = new Structure();
                    loadedStructure.readNbt(structure);
                    STRUCTURES.put(file.getName().replace(".nbt", ""), loadedStructure);
                }
                LOGGER.info("Successfully loaded structures folder with " + STRUCTURES.size() + " structure.");
            }

        }catch (Exception exception) {
            LOGGER.error("There was an error creating/loading the structures folder!", exception);
        }
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

    public static void generateStructureFeature(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos) {
        if(!blockPos.isWithinDistance(BlockPos.ORIGIN, 2) || !(chunkGenerator instanceof StructureChunkGenerator)) {
            return;
        }

        StructureChunkGenerator structureChunkGenerator = (StructureChunkGenerator) chunkGenerator;
        Structure structure = Mod.STRUCTURES.get(structureChunkGenerator.getStructure());
        BlockPos structureOffset = structureChunkGenerator.getStructureOffset();

        BlockPos offsetedPos = blockPos.add(structureOffset);

        if(structure != null) {
            structure.place(structureWorldAccess, offsetedPos.add(8, 64, 8), offsetedPos.add(8, 64, 8), new StructurePlacementData(), random, Block.NO_REDRAW);
        }
    }


}
