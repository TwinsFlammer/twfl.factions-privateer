package com.redefocus.factionscaribe.home.component;

import com.massivecraft.factions.entity.Faction;
import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.factionscaribe.home.data.Home;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author oNospher
 **/
public abstract class HomeComponent {
    private final JSONText jsonText;

    public HomeComponent(List<Home> homeList, Home.State state) {
        List<Home> homes = homeList.stream()
                .filter(home -> home.getState().equals(state))
                .collect(Collectors.toList());

        this.jsonText = new JSONText()
                .text("§6Homes " + state.getName() + ": §f" + (homes.isEmpty() ? "Nenhuma." : ""))
                .next();

        AtomicInteger index = new AtomicInteger(0);

        homes.forEach(home -> {
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
                    .execute("/home " + home.getName())
                    .next()
                    .text(newIndex + 1 == homes.size() ? "." : ", ")
                    .next();
        });
    }

    public void send(CaribeUser caribeUser) {
        Player player = caribeUser.getPlayer();

        this.jsonText.send(player);
    }
}
