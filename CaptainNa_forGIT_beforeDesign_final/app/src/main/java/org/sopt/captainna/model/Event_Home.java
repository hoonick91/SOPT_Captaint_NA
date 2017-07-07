package org.sopt.captainna.model;

/**
 * Created by KoJunHee on 2017-06-28.
 */

public class Event_Home {
    public int eventImg;
    public String groupName;
    public String eventName;
    public String eventIntro;
    public String eventTime;
    public String eventPlace;

    public Event_Home(int eventImg, String groupName, String eventName, String eventIntro, String eventTime, String eventPlace) {
        this.eventImg = eventImg;
        this.groupName = groupName;
        this.eventName = eventName;
        this.eventIntro = eventIntro;
        this.eventTime = eventTime;
        this.eventPlace = eventPlace;
    }
}
