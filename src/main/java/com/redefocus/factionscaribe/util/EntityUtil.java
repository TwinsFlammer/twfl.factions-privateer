package com.redefocus.factionscaribe.util;

import org.bukkit.entity.EntityType;

/**
 * @author SrGutyerrez
 */
public class EntityUtil {
    public static String translate(String name) {
        switch (name) {
            case "Spider":
                return "Aranha";
            case "Blaze":
                return "Blaze";
            case "CaveSpider":
                return "Aranha ven.";
            case "PigZombie":
                return "Porco Zumbi";
            case "VillagerGolem":
                return "Golem de Ferro";
            case "Pig":
                return "Porco";
            case "Sheep":
                return "Ovelha";
            case "Zombie":
                return "Zumbi";
            case "Cow":
                return "Vaca";
            case "Skeleton":
                return "Esqueleto";
            case "Slime":
                return "Slime";
            case "Mooshroom":
                return "Vaca Cogu.";
            case "Enderman":
                return "Enderman";
            case "Chicken":
                return "Galinha";
            case "Creeper":
                return "Creeper";
            case "WitherBoss":
                return "Wither";
            case "Witch":
                return "Bruxa";
            case "LavaSlime":
                return "Magma";
            default:
                return name;
        }
    }

    public static String head(EntityType entityType) {
        if (entityType == null) return "";

        switch (entityType) {
            case BLAZE:
                return "MHF_BLAZE";
            case CAVE_SPIDER:
                return "MHF_CAVESPIDER";
            case CHICKEN:
                return "MHF_CHICKEN";
            case COW:
                return "MHF_COW";
            case CREEPER:
                return "MHF_CREEPER";
            case GHAST:
                return "MHF_GHAST";
            case IRON_GOLEM:
                return "MHF_GOLEM";
            case MAGMA_CUBE:
                return "MHF_LavaSlime";
            case OCELOT:
                return "MHF_OCELOT";
            case PIG:
                return "MHF_PIG";
            case PIG_ZOMBIE:
                return "MHF_PIGZOMBIE";
            case SHEEP:
                return "MHF_SHEEP";
            case SKELETON:
                return "MHF_SKELETON";
            case SLIME:
                return "MHF_SLIME";
            case SPIDER:
                return "MHF_SPIDER";
            case SQUID:
                return "MHF_SQUID";
            case WITHER:
                return "MHF_WITHER";
            case ZOMBIE:
                return "MHF_ZOMBIE";
            case ENDERMAN:
                return "MHF_ENDERMAN";
            case WOLF:
                return "MHF_WOLF";
            case GUARDIAN:
                return "MHF_GUARDIAN";
            default:
                return "MHF_" + entityType.toString();
        }
    }
}
