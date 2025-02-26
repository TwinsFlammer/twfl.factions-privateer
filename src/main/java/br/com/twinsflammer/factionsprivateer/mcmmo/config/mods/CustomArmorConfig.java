package br.com.twinsflammer.factionsprivateer.mcmmo.config.mods;

import br.com.twinsflammer.factionsprivateer.FactionsPrivateer;
import br.com.twinsflammer.factionsprivateer.mcmmo.config.ConfigLoader;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.ItemType;
import br.com.twinsflammer.factionsprivateer.mcmmo.datatypes.skills.MaterialType;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.repair.repairables.Repairable;
import br.com.twinsflammer.factionsprivateer.mcmmo.skills.repair.repairables.RepairableFactory;
import br.com.twinsflammer.factionsprivateer.mcmmo.util.skills.SkillUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomArmorConfig extends ConfigLoader {

    private boolean needsUpdate = false;

    public List<Material> customBoots = new ArrayList<Material>();
    public List<Material> customChestplates = new ArrayList<Material>();
    public List<Material> customHelmets = new ArrayList<Material>();
    public List<Material> customLeggings = new ArrayList<Material>();

    public List<Repairable> repairables = new ArrayList<Repairable>();

    protected CustomArmorConfig(String fileName) {
        super("mods", fileName);
        loadKeys();
    }

    @Override
    protected void loadKeys() {
        loadArmor("Boots", customBoots);
        loadArmor("Chestplates", customChestplates);
        loadArmor("Helmets", customHelmets);
        loadArmor("Leggings", customLeggings);

        if (needsUpdate) {
            needsUpdate = false;
            backup();
        }
    }

    private void loadArmor(String armorType, List<Material> materialList) {
        if (needsUpdate) {
            return;
        }

        ConfigurationSection armorSection = config.getConfigurationSection(armorType);

        if (armorSection == null) {
            return;
        }

        Set<String> armorConfigSet = armorSection.getKeys(false);

        for (String armorName : armorConfigSet) {
            if (config.contains(armorType + "." + armorName + "." + ".ID")) {
                needsUpdate = true;
                return;
            }

            Material armorMaterial = Material.matchMaterial(armorName);

            if (armorMaterial == null) {
                FactionsPrivateer.getInstance().getLogger().warning("Invalid material name. This item will be skipped. - " + armorName);
                continue;
            }

            boolean repairable = config.getBoolean(armorType + "." + armorName + ".Repairable");
            Material repairMaterial = Material.matchMaterial(config.getString(armorType + "." + armorName + ".Repair_Material", ""));

            if (repairable && (repairMaterial == null)) {
                FactionsPrivateer.getInstance().getLogger().warning("Incomplete repair information. This item will be unrepairable. - " + armorName);
                repairable = false;
            }

            if (repairable) {
                byte repairData = (byte) config.getInt(armorType + "." + armorName + ".Repair_Material_Data_Value", -1);
                int repairQuantity = SkillUtils.getRepairAndSalvageQuantities(new ItemStack(armorMaterial), repairMaterial, repairData);

                if (repairQuantity == 0) {
                    repairQuantity = config.getInt(armorType + "." + armorName + ".Repair_Material_Quantity", 2);
                }

                String repairItemName = config.getString(armorType + "." + armorName + ".Repair_Material_Pretty_Name");
                int repairMinimumLevel = config.getInt(armorType + "." + armorName + ".Repair_MinimumLevel", 0);
                double repairXpMultiplier = config.getDouble(armorType + "." + armorName + ".Repair_XpMultiplier", 1);

                short durability = armorMaterial.getMaxDurability();

                if (durability == 0) {
                    durability = (short) config.getInt(armorType + "." + armorName + ".Durability", 70);
                }

                repairables.add(RepairableFactory.getRepairable(armorMaterial, repairMaterial, repairData, repairItemName, repairMinimumLevel, repairQuantity, durability, ItemType.ARMOR, MaterialType.OTHER, repairXpMultiplier));
            }

            materialList.add(armorMaterial);
        }
    }
}
