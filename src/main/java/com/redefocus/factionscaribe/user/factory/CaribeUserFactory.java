package com.redefocus.factionscaribe.user.factory;

import com.google.common.collect.Lists;
import com.redefocus.common.shared.permissions.user.factory.AbstractUserFactory;
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
        return this.users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public U getUser(String name) {
        return this.users.stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public U getUser(UUID uuid) {
        return null;
    }
}
