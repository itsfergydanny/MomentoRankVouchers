package com.dnyferguson.momentorankvouchers.objects;

public class Rankup {
    private String rankFrom;
    private String rankFromName;
    private String rankTo;
    private String rankToName;
    private String unique;

    public Rankup(String rankFrom, String rankFromName, String rankTo, String rankToName, String unique) {
        this.rankFrom = rankFrom;
        this.rankFromName = rankFromName;
        this.rankTo = rankTo;
        this.rankToName = rankToName;
        this.unique = unique;
    }

    public String getRankFrom() {
        return rankFrom;
    }

    public void setRankFrom(String rankFrom) {
        this.rankFrom = rankFrom;
    }

    public String getRankTo() {
        return rankTo;
    }

    public void setRankTo(String rankTo) {
        this.rankTo = rankTo;
    }

    public String getRankFromName() {
        return rankFromName;
    }

    public void setRankFromName(String rankFromName) {
        this.rankFromName = rankFromName;
    }

    public String getRankToName() {
        return rankToName;
    }

    public void setRankToName(String rankToName) {
        this.rankToName = rankToName;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }
}
