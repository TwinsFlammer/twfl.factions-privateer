package br.com.twinsflammer.factionsprivateer.mcmmo.skills.salvage.salvageables;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface SalvageableManager {

    /**
     * Register a salvageable with the SalvageManager
     *
     * @param salvageable Salvageable to register
     */
    public void registerSalvageable(Salvageable salvageable);

    /**
     * Register a list of salvageables with the SalvageManager
     *
     * @param salvageables List<Salvageable> to register
     */
    public void registerSalvageables(List<Salvageable> salvageables);

    /**
     * Checks if an item is salvageable
     *
     * @param type Material to check if salvageable
     *
     * @return true if salvageable, false if not
     */
    public boolean isSalvageable(Material type);

    /**
     * Checks if an item is salvageable
     *
     * @param itemStack Item to check if salvageable
     *
     * @return true if salvageable, false if not
     */
    public boolean isSalvageable(ItemStack itemStack);

    /**
     * Gets the salvageable with this type
     *
     * @param type Material of the salvageable to look for
     *
     * @return the salvageable, can be null
     */
    public Salvageable getSalvageable(Material type);
}
