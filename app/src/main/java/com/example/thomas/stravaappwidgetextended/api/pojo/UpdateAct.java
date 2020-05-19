package com.example.thomas.stravaappwidgetextended.api.pojo;

import java.util.HashMap;

public class UpdateAct {

    private HashMap<String, Boolean> trainer;

    public UpdateAct(){
        this.trainer = new HashMap<>();
        this.trainer.put("trainer", true);
    }
}
