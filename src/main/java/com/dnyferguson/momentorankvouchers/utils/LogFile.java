package com.dnyferguson.momentorankvouchers.utils;

import com.dnyferguson.momentorankvouchers.MomentoRankVouchers;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class LogFile {
    PrintWriter pw;

    public LogFile(MomentoRankVouchers plugin) {
        try {
            File dataFolder = plugin.getDataFolder();
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File saveTo = new File(plugin.getDataFolder(), "log.txt");
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }

            FileWriter fw = new FileWriter(saveTo, true);
            pw = new PrintWriter(fw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String msg) {
        pw.println(msg);
        pw.flush();
    }

    public void close() {
        pw.close();
    }
}
