package org.sopt.captainna.model;

import java.util.ArrayList;

/**
 * Created by KoJunHee on 2017-07-03.
 */

public class EventResult {

    public Result result;
    public String message;

    public class Result{
        public EVent event;
        public ArrayList<Sub_event> sub_event;
    }

    public class EVent{
        public String group_title;
        public String group_photo;
        public String title;
        public String text;
        public String place;
        public String start_date;
        public String manager_name;
        public String end_date;
        public String photo;
        public int is_manager;
        public int amount;
    }

    public class Sub_event{
        public int event_count;
        public String place;
        public int price;
    }
}
