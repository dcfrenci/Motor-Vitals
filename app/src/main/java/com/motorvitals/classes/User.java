package com.motorvitals.classes;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.motorvitals.jackson.UserDeserializer;
import com.motorvitals.jackson.UserSerializer;

import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = UserSerializer.class)
@JsonDeserialize(using = UserDeserializer.class)
public class User implements Parcelable {
    private String name;
    private final Map<String, Integer> stateColor;

    public User() {
        this.name = "";
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

    public Color getColor(String string) {
        int integer = getStateColor().containsKey(string) && getStateColor().get(string) != null ? getStateColor().get(string) : Color.WHITE;
        return Color.valueOf(integer);
    }


    //  ---------------------- Implementation Parcelable ----------------------
    protected User(Parcel in) {
        name = in.readString();
        int size = in.readInt();
        stateColor = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            int value = in.readInt();
            stateColor.put(key, value);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(stateColor.size());
        for (Map.Entry<String, Integer> entry : stateColor.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeInt(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
