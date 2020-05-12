package com.redefocus.factionscaribe.user.factory;

import com.google.common.collect.Lists;
import com.redefocus.common.shared.permissions.user.data.User;
import com.redefocus.common.shared.permissions.user.factory.AbstractUserFactory;
import com.redefocus.common.shared.permissions.user.manager.UserManager;
import com.redefocus.factionscaribe.user.data.CaribeUser;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author SrGutyerrez
 */
public class CaribeUserFactory<U extends CaribeUser> extends AbstractUserFactory<U> {
    @Getter
    private List<U> users = Lists.newArrayList();

    @Override
    public U getUser(Integer id) {
        return null;
//        User user = UserManager.getUser(id);
//
//        if (user == null) return null;
//
//        return this.users.stream()
//                .filter(u -> u.getId().equals(id))
//                .findFirst()
//                .orElseGet(() -> {
//                    U u = (U) new CaribeUser(user);
//
//                    this.users.add(u);
//
//                    return u;
//                });
    }

    @Override
    public U getUser(String name) {
        return null;
//        User user = UserManager.getUser(name);
//
//        if (user == null) return null;
//
//        return this.users.stream()
//                .filter(u -> u.getName().equalsIgnoreCase(name))
//                .findFirst()
//                .orElseGet(() -> {
//                    U u = (U) new CaribeUser(user);
//
//                    this.users.add(u);
//
//                    return u;
//                });
    }

    @Override
    public U getUser(UUID uuid) {
        return null;
//        User user = UserManager.getUser(uuid);
//
//        if (user == null) return null;
//
//        return this.users.stream()
//                .filter(u -> u.getUniqueId().equals(uuid))
//                .findFirst()
//                .orElseGet(() -> {
//                    U u = (U) new CaribeUser(user);
//
//                    this.users.add(u);
//
//                    return u;
//                });
    }
}
