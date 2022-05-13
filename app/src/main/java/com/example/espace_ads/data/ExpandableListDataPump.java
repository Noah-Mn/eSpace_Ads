package com.example.espace_ads.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String, List<String>> getData(){
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> africa = new ArrayList<String>();
        africa.add("- Malawi");
        africa.add("- Zambia");
        africa.add("- Namibia");

        List<String> america = new ArrayList<String>();
        africa.add("- Malawi");
        africa.add("- Zambia");
        africa.add("- Namibia");

        List<String> europe = new ArrayList<String>();
        africa.add("- Malawi");
        africa.add("- Zambia");
        africa.add("- Namibia");

        List<String> asia = new ArrayList<String>();
        africa.add("- Malawi");
        africa.add("- Zambia");
        africa.add("- Namibia");

        List<String> australia = new ArrayList<String>();
        africa.add("- Malawi");
        africa.add("- Zambia");
        africa.add("- Namibia");

        expandableListDetail.put("Africa", africa);
        expandableListDetail.put("America", america);
        expandableListDetail.put("Europe", europe);
        expandableListDetail.put("Asia", asia);
        expandableListDetail.put("Australia", australia);

        return expandableListDetail;
    }
}
