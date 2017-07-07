package org.sopt.captainna.model;

public class MoneyPerson {


    public String name;
    public String money;
    public String phone;
    public boolean visibleBox;
    public int checkbox;

    public MoneyPerson(String name, String money, String phone, boolean visibleBox, int checkbox) {
        this.name = name;
        this.money = money;
        this.phone = phone;
        this.visibleBox = visibleBox;
        this.checkbox = checkbox;
    }
    public String getItem_text() {
        return name;
    }
}
