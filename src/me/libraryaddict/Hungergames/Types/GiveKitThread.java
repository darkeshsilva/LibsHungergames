package me.libraryaddict.Hungergames.Types;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GiveKitThread extends Thread {
    private Connection con = null;
    private String kitName;
    private String playerName;

    public GiveKitThread(String player, String kit) {
        kitName = kit;
        playerName = player;
    }

    public void mySqlConnect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String conn = "jdbc:mysql://" + HungergamesApi.getMySqlManager().SQL_HOST + "/"
                    + HungergamesApi.getMySqlManager().SQL_DATA;
            con = DriverManager.getConnection(conn, HungergamesApi.getMySqlManager().SQL_USER,
                    HungergamesApi.getMySqlManager().SQL_PASS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void mySqlDisconnect() {
        try {
            this.con.close();
        } catch (SQLException ex) {
        } catch (NullPointerException ex) {
        }
    }

    public void run() {
        if (!HungergamesApi.getConfigManager().getMainConfig().isMysqlEnabled())
            return;
        mySqlConnect();
        try {
            Statement stmt = con.createStatement();
            stmt.execute("INSERT INTO HGKits (Name, KitName) VALUES ('" + playerName + "', '" + kitName + "')");
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mySqlDisconnect();
    }
}
