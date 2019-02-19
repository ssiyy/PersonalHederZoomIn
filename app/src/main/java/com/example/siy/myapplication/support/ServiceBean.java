package com.example.siy.myapplication.support;


import java.io.Serializable;

/**
 * 技能
 *
 * Created by Siy on 2018/12/28.
 *
 * @author Siy
 */
public class ServiceBean implements Serializable {
    private static final long serialVersionUID = -5146155711343561246L;

    /**
     * 	技能code
     */
    private String  gameCode;

    /**
     * 	技能名称
     */
    private String  gameName;

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return gameName;
    }
}
