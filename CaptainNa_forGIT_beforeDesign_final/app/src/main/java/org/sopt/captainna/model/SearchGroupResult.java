package org.sopt.captainna.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by minjeong on 2017-07-04.
 */

public class SearchGroupResult {
    public ArrayList<SearchResult> groups;

    public class SearchResult{
        public int id;
        public String title;
        public String text;
        public String photo;
        public String chairman_name;
        public int count;
    }
}
