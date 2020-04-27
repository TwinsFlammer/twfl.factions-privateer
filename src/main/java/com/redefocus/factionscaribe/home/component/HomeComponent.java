package com.redefocus.factionscaribe.home.component;

import com.massivecraft.factions.entity.Faction;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author oNospher
 **/
public abstract class HomeComponent {
    private final JSONText jsonText;

    public HomeComponent(List<Home> homes, Home.State state) {
        this.jsonText = new JSONText()
                .text("§6Homes " + state.getName() + ": §f" + (homes.isEmpty() ? "Nenhuma." : ""))
                .next();

        AtomicInteger index = new AtomicInteger(0);

        homes.stream()
                .filter(home -> home.getState().equals(state))
                .forEach(home -> {
                    Faction faction = Faction.get(home.getFactionId());

                    Integer newIndex = index.getAndIncrement();

                    this.jsonText
                            .text(home.getName())
                            .hoverText(
                                    "§6Nome: §f" + home.getName() +
                                    "\n" +
                                    "§6Tipo: §f" + state.getName() +
                                    "\n" +
                                    "§6Facção: §f" + faction.getName() +
                                    "\n" +
                                    "§6Localização: §f" + home.getFancyLocation() +
                                    "\n" +
                                    "§6Mundo: §f" + home.getWorldName()
                            )
                            .next()
                            .text(newIndex == homes.size() ? "" : ", ")
                            .next();
                });
    }

    public void send(CaribeUser caribeUser) {
        caribeUser.sendMessage(this.jsonText);
    }
}
