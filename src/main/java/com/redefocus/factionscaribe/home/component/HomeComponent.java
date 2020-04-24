package com.redefocus.factionscaribe.home.component;

import com.redefocus.api.spigot.util.jsontext.data.JSONText;
import com.redefocus.factionscaribe.user.data.CaribeUser;

/**
 * @author oNospher
 **/
public abstract class HomeComponent {
    private final JSONText jsonText;

    public HomeComponent(CaribeUser user) {
        JSONText jsonText;

        jsonText = new JSONText().text("\n")
                .next()
                .text("§cUtilize /home [player:]<home>.")
                .next()
                .text("\n\n")
                .next()
                .text("§6Homes públicas: §f" + (user.getPublicHomes().isEmpty() ? "Nenhuma." : ""));

        this.jsonText = jsonText;
    }

    public void send(CaribeUser caribeUser) {
        caribeUser.sendMessage(this.jsonText);
    }
}
