package org.sopt.captainna.model;

import android.graphics.drawable.Drawable;

public class Contacts {

    public String name;
    public String phone;
    public Drawable check;
    public boolean aBoolean;
    public int background;
    public int textColor;


    public Contacts(String name, String phone, Drawable check, boolean aBoolean, int background, int textColor) {
        this.name = name;
        this.phone = phone;
        this.check = check;
        this.aBoolean = aBoolean;
        this.background = background;
        this.textColor = textColor;
    }

    public String getItem_text() {
        return name;
    }
}
