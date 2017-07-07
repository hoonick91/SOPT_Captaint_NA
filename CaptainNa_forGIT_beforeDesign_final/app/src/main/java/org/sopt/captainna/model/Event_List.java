package org.sopt.captainna.model;

/**
 * Created by KoJunHee on 2017-06-26.
 */

public class Event_List {
    public int eventIgm;
    public String eventName;
    public String eventDate;
    public String eventPlace;
    public String eventMoney;

    public Event_List(int eventIgm, String eventName, String eventDate, String eventPlace,  String eventMoney) {
        this.eventIgm = eventIgm;
        this.eventName = eventName;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.eventMoney = eventMoney;
    }
}
