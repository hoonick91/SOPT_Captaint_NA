package org.sopt.captainna.model;

import java.util.ArrayList;

/**
 * Created by minjeong on 2017-07-07.
 */

public class UserId {
    public int target_deposit_status;
    public ArrayList<User_pk> user_list;

    public static class User_pk{
        public int user_pk;

        public User_pk(int user_pk){
            this.user_pk = user_pk;
        }
    }
}
