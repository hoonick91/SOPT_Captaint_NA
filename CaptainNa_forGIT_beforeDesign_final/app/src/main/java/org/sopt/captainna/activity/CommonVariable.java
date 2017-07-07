package org.sopt.captainna.activity;

import android.app.Application;

import org.sopt.captainna.model.EventDetailResult;
import org.sopt.captainna.model.MoneyPerson;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by minjeong on 2017-07-02.
 */

public class CommonVariable extends Application{

    public static ArrayList<Map<String, String>> dataList;
    public static ArrayList<MoneyPerson> addGiverPersons = new ArrayList<>();
    public static ArrayList<MoneyPerson> addNonGiverPersons = new ArrayList<>();
    public static boolean isGiver;
    public static boolean isFrombutton;
    public static String chooseday;
    public static String chooseTime;
    public static String chooseday2;
    public static String chooseTime2;
    public static String token;
    public static EventDetailResult.Event pushEditDetail;
    public static ArrayList<EventDetailResult.SUb_event> subEditDetail = new ArrayList<>();
    public static String id_name;
    public static int givercount;
    public static int nongivercount;
}
