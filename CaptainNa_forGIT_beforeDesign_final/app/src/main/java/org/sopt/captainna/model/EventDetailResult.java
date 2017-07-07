package org.sopt.captainna.model;

import java.util.ArrayList;

/**
 * Created by KoJunHee on 2017-07-03.
 */

public class EventDetailResult {

    public Event event;
    public ArrayList<SUb_event> sub_event;


    public class Event {
        public String group_id;
        public String group_title;
        public String group_photo;
        public String title;
        public String text;
        public String place;
        public String manager_name;
        public String start_date;
        public String end_date;
        public String photo;
        public int amount;
        public int is_manager;
        public int is_participated;
        public int is_paid;
    }

    public class SUb_event {
        public int event_count;
        public String place;
        public int price;
        public String is_participated;
    }

}
