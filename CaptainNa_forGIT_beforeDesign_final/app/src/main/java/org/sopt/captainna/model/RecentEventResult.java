package org.sopt.captainna.model;

import java.util.ArrayList;

/**
 * Created by KoJunHee on 2017-06-30.
 */

public class RecentEventResult {
    public ArrayList<EVent> event;
    public String message;

    public class EVent {
        public String event_title;
        public String group_title;
        public String start_date;
        public String place;
        public int amount;
        public String photo;

    }

}
