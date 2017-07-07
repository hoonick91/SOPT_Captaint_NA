package org.sopt.captainna.model;

import java.util.ArrayList;

/**
 * Created by KoJunHee on 2017-07-03.
 */

public class MyGroupEventResult {
    public ArrayList<GRoup> group;
    public ArrayList<EVent> events;


    public class GRoup{
        public int id;
        public String title;
        public String text;
        public String photo;
        public int member_count;
        public int is_chairman;
        public String pw;
        public int is_new;
        public String chairman_name;
    }

    public class EVent{
        public int id;
        public String title;
        public String place;
        public String start_date;
        public String end_date;
        public String photo;
        public int amount;
        public int is_manager;

    }
}
