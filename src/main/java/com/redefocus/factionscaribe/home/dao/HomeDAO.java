package com.redefocus.factionscaribe.home.dao;

import com.redefocus.common.shared.databases.mysql.dao.Table;

/**
 * @author oNospher
 **/
public class HomeDAO extends Table {

    @Override
    public String getDatabaseName() {
        return "general";
    }

    @Override
    public String getTableName() {
        return "server_factions_caribe_homes";
    }

    @Override
    public void createTable() {

    }
}
