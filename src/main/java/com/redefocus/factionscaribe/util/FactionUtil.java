package com.redefocus.factionscaribe.util;

/**
 * @author oNospher
 **/
public class FactionUtil {

    public static Boolean isAllowed(/*Faction faction, Faction at, MPerm mPerm */) {
      /*  if (at == null || at.isNone()) return true;
        if(faction.equals(at)) return true;
        if(!(faction.getRelationWish(at) == Rel.ALLY)) return false;
        return at.getPerms().get(MPerm.getPermHome()).contains(Rel.ALLY); */
        return true;
    }
}
