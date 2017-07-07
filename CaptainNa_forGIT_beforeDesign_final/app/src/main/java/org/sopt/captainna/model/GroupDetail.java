package org.sopt.captainna.model;

/**
 * Created by KoJunHee on 2017-06-26.
 */

public class GroupDetail {
    public int eventImg;
    public int groupImg;
    public String groupName;
    public String eventName;
    public String eventMaker;
    public String eventIntro;
    public String eventDate;
    public String eventPlace;
    public String eventMoney;

    public GroupDetail(int eventImg, int groupImg, String groupName, String eventName, String eventMaker, String eventIntro, String eventDate, String eventPlace, String eventMoney) {
        this.eventImg = eventImg;
        this.groupImg = groupImg;
        this.groupName = groupName;
        this.eventName = eventName;
        this.eventMaker = eventMaker;
        this.eventIntro = eventIntro;
        this.eventDate = eventDate;
        this.eventPlace = eventPlace;
        this.eventMoney = eventMoney;
    }
}
