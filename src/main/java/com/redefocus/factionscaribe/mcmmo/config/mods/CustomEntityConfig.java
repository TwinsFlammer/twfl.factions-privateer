package com.redefocus.factionscaribe.mcmmo.config.mods;

import com.redefocus.factionscaribe.mcmmo.config.ConfigLoader;
import com.redefocus.factionscaribe.mcmmo.datatypes.mods.CustomEntity;
import java.util.HashMap;

import com.redefocus.factionscaribe.FactionsCaribe;
import org.apache.commons.lang.ClassUtils;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class CustomEntityConfig extends ConfigLoader {

    public HashMap<String, CustomEntity> customEntityClassMap = new HashMap<String, CustomEntity>();
    public HashMap<String, CustomEntity> customEntityTypeMap = new HashMap<String, CustomEntity>();

    protected CustomEntityConfig(String fileName) {
        super("mods", fileName);
        loadKeys();
    }

    @Override
    protected void loadKeys() {
        if (config.getConfigurationSection("Hostile") != null) {
            backup();
            return;
        }

        for (String entityName : config.getKeys(false)) {
            Class<?> clazz = null;
            String className = config.getString(entityName + ".Class", "");

            try {
                clazz = ClassUtils.getClass(className);
            } catch (ClassNotFoundException e) {
                FactionsCaribe.getInstance().getLogger().warning("Invalid class (" + className + ") detected for " + entityName + ".");
                FactionsCaribe.getInstance().getLogger().warning("This custom entity may not function properly.");
            }

            String entityTypeName = entityName.replace("_", ".");
            double xpMultiplier = config.getDouble(entityName + ".XP_Multiplier", 1.0D);

            boolean canBeTamed = config.getBoolean(entityName + ".Tameable");
            int tamingXp = config.getInt(entityName + ".Taming_XP");

            boolean canBeSummoned = config.getBoolean(entityName + ".CanBeSummoned");
            Material callOfTheWildMaterial = Material.matchMaterial(config.getString(entityName + ".COTW_Material", ""));
            byte callOfTheWildData = (byte) config.getInt(entityName + ".COTW_Material_Data");
            int callOfTheWildAmount = config.getInt(entityName + ".COTW_Material_Amount");

            if (canBeSummoned && (callOfTheWildMaterial == null || callOfTheWildAmount == 0)) {
                FactionsCaribe.getInstance().getLogger().warning("Incomplete Call of the Wild information. This entity will not be able to be summoned by Call of the Wild.");
                canBeSummoned = false;
            }

            CustomEntity entity = new CustomEntity(xpMultiplier, canBeTamed, tamingXp, canBeSummoned, (canBeSummoned ? new MaterialData(callOfTheWildMaterial, callOfTheWildData).toItemStack(1) : null), callOfTheWildAmount);

            customEntityTypeMap.put(entityTypeName, entity);
            customEntityClassMap.put(clazz == null ? null : clazz.getName(), entity);
        }
    }
}
