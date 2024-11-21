package com.motorvitals.classes;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private Map<String, Integer> stateColor;

    public User(String name) {
        this.name = name;
        this.stateColor = new HashMap<>(Map.of("min", Color.GREEN, "med", Color.YELLOW, "max", Color.RED));
    }

    public User(Map<String, Integer> stateColor, String name) {
        this.stateColor = stateColor;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getStateColor() {
        return stateColor;
    }

    public void setStateColor(Map<String, Integer> stateColor) {
        this.stateColor = stateColor;
    }

    public Color getColor(String string) {
        int integer = getStateColor().containsKey(string) && getStateColor().get(string) != null ? getStateColor().get(string) : Color.WHITE;
        return Color.valueOf(integer);
    }
}
