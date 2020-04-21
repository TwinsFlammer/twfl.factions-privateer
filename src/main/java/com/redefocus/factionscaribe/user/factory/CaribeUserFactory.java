package com.redefocus.factionscaribe.user.factory;

import com.google.common.collect.Lists;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.factory.AbstractUserFactory;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;

import java.util.List;
import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class CaribeUserFactory<U extends CaribeUser> extends AbstractUserFactory<U> {
    private List<U> users = Lists.newArrayList();

    @Override
    public U getUser(Integer id) {
        User user = UserManager.getUser(id);

        return this.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseGet(() -> (U) new CaribeUser(user));
    }

    @Override
    public U getUser(String name) {
        User user = UserManager.getUser(name);

        return this.users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> (U) new CaribeUser(user));
    }

    @Override
    public U getUser(UUID uuid) {
        User user = UserManager.getUser(uuid);

        return this.users.stream()
                .filter(u -> u.getUniqueId().equals(uuid))
                .findFirst()
                .orElseGet(() -> (U) new CaribeUser(user));
    }
}
