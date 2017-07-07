package org.sopt.captainna.model;

/**
 * Created by KoJunHee on 2017-06-26.
 */

public class MyGroup_List {
    public int groupImg;
    public String groupName;
    public String groupIntro;
    public String groupCaptain;
    public int groupJoinNumber;

    public MyGroup_List(int groupImg, String groupName, String groupIntro, String groupCaptain, int groupJoinNumber) {
        this.groupImg = groupImg;
        this.groupName = groupName;
        this.groupIntro = groupIntro;
        this.groupCaptain = groupCaptain;
        this.groupJoinNumber = groupJoinNumber;
    }

    public String getItem_text() {
        return groupName;
    }
}
