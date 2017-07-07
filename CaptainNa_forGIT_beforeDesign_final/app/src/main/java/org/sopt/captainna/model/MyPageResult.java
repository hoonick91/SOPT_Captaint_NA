package org.sopt.captainna.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by minjeong on 2017-07-04.
 */

public class MyPageResult {
    public MyProfile my_profile;
    public ArrayList<Participate_in> participate_in;
    public ArrayList<ImCaptian> my_event;
    public ArrayList<ImLeader> my_group;

    public class MyProfile{
        public String name;
        public String email;
        public String photo;
        public String ph;
    }

}
