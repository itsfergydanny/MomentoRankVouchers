package com.dnyferguson.momentorankvouchers.mysql;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MySQL {

    private MomentoRankVouchers plugin;
    private HikariDataSource ds;

    public MySQL(MomentoRankVouchers plugin) {
        this.plugin = plugin;
        ConfigurationSection config = plugin.getConfig().getConfigurationSection("mysql");

        HikariConfig hikari = new HikariConfig();
        hikari.setJdbcUrl("jdbc:mysql://" + config.getString("ip") + ":" + config.getString("port") + "/" + config.getString("db"));
        hikari.setUsername(config.getString("user"));
        hikari.setPassword(config.getString("pass"));
        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikari.setMaximumPoolSize(config.getInt("max-connections"));

        ds = new HikariDataSource(hikari);
        createTables(config.getString("db"));
    }

    private void createTables(String db) {
        executeStatementAsync("CREATE TABLE IF NOT EXISTS `" + db + "`.`vouchers` ( `id` INT NOT NULL AUTO_INCREMENT , `unique` VARCHAR(36) NOT NULL, `ign` VARCHAR(16) NOT NULL , `uuid` VARCHAR(36) NOT NULL , `rankFrom` VARCHAR(100) NOT NULL , `rankTo` VARCHAR(100) NOT NULL , `rankFromName` VARCHAR(100) NOT NULL , `rankToName` VARCHAR(100) NOT NULL , `used` BOOLEAN NOT NULL, `createdAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
    }

    public void getResultAsync(String stmt, FindResultCallback callback) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try (Connection con = ds.getConnection()) {
                    PreparedStatement pst = con.prepareStatement(stmt);
                    ResultSet rs = pst.executeQuery();
                    callback.onQueryDone(rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getResultSync(String stmt, FindResultCallback callback) {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                try (Connection con = ds.getConnection()) {
                    PreparedStatement pst = con.prepareStatement(stmt);
                    ResultSet rs = pst.executeQuery();
                    callback.onQueryDone(rs);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void executeStatementSync(String stmt) {
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                try (Connection con = ds.getConnection()) {
                    PreparedStatement pst = con.prepareStatement(stmt);
                    pst.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void executeStatementAsync(String stmt) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                try (Connection con = ds.getConnection()) {
                    PreparedStatement pst = con.prepareStatement(stmt);
                    pst.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    public Future<Boolean> executeAndReturnResultSet(String stmt) {
//        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
//
//        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
//            @Override
//            public void run() {
//                try (Connection con = ds.getConnection()) {
//                    PreparedStatement pst = con.prepareStatement(stmt);
//                    pst.execute();
//                    completableFuture.complete(true);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        return completableFuture;
//    }

    public void close() {
        ds.close();
    }
}