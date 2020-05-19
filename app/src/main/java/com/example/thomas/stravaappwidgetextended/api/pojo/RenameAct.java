package com.example.thomas.stravaappwidgetextended.api.pojo;

import java.util.HashMap;

public class RenameAct {

    private HashMap<String, String> name;

    public RenameAct(String new_name){
        this.name = new HashMap<>();
        this.name.put("name", new_name);
    }
}
