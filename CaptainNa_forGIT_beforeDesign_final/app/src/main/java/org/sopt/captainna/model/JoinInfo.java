package org.sopt.captainna.model;

/**
 * Created by minjeong on 2017-07-05.
 */

public class JoinInfo {
    public String email;
    public String pw;
    public String name;
    public String ph;
    public String photo;

    public JoinInfo(String email, String pw, String name, String ph, String photo){
        this.email = email;
        this.pw = pw;
        this.ph = ph;
        this.name = name;
        this.photo = photo;

    }
}
