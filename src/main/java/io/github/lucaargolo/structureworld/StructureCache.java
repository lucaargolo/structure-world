package io.github.lucaargolo.structureworld;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class StructureCache {

    private static final HashMap<Identifier, Structure> structureHashMap = new HashMap<>();

    public static Structure getOrCreateCache(StructureManager manager, Identifier structure) {
        Structure cachedStructure = structureHashMap.get(structure);
        if(cachedStructure != null) {
            return cachedStructure;
        } else {
            Structure toCacheStructure = manager.getStructure(structure);
            structureHashMap.put(structure, toCacheStructure);
            return toCacheStructure;
        }
    }

}
