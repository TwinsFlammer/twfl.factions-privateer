package br.com.twinsflammer.factionsprivateer.user.factory;

import br.com.twinsflammer.common.shared.permissions.user.data.User;
import br.com.twinsflammer.common.shared.permissions.user.factory.AbstractUserFactory;
import br.com.twinsflammer.common.shared.permissions.user.manager.UserManager;
import br.com.twinsflammer.factionsprivateer.user.data.PrivateerUser;
import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class PrivateerUserFactory<U extends PrivateerUser> extends AbstractUserFactory<U> {
    @Getter
    private List<U> users = Lists.newArrayList();

    @Override
    public U getUser(Integer id) {
        User user = UserManager.getUser(id);

        if (user == null) return null;

        return this.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseGet(() -> {
                    U u = (U) new PrivateerUser(user);

                    this.users.add(u);

                    return u;
                });
    }

    @Override
    public U getUser(String name) {
        User user = UserManager.getUser(name);

        if (user == null) return null;

        return this.users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    U u = (U) new PrivateerUser(user);

                    this.users.add(u);

                    return u;
                });
    }

    @Override
    public U getUser(UUID uuid) {
        User user = UserManager.getUser(uuid);

        if (user == null) return null;

        return this.users.stream()
                .filter(u -> u.getUniqueId().equals(uuid))
                .findFirst()
                .orElseGet(() -> {
                    U u = (U) new PrivateerUser(user);

                    this.users.add(u);

                    return u;
                });
    }
}
