package com.kevin_mic.aqua.model.dbobj;

import com.kevin_mic.aqua.model.types.ActionType;
import lombok.Data;

@Data
public class ActionDB {
    public static final String TABLE_NAME = "action";
    private int actionId;
    private ActionType actionType;
    private String name;
    private String actionJson;
}
