package org.sopt.captainna.model;

/**
 * Created by KoJunHee on 2017-06-30.
 */

public class RecentEvent {
    public int eventIgm;
    public String eventName;
    public String groupName;
    public String eventDate;
    public String eventPlace;
    public String eventMoney;

    public RecentEvent(int eventIgm, String eventName, String groupName, String eventDate, String eventPlace,  String eventMoney) {
        this.eventIgm = eventIgm;
        this.eventName = eventName;
        this.groupName = groupName;
        this.eventPlace = eventPlace;
        this.eventDate = eventDate;
        this.eventMoney = eventMoney;
    }
}
