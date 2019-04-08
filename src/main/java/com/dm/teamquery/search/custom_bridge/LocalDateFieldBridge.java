package com.dm.teamquery.search.custom_bridge;

import org.hibernate.search.bridge.TwoWayStringBridge;

import java.time.LocalDateTime;
import java.util.UUID;

public class LocalDateFieldBridge implements TwoWayStringBridge {



    @Override
    public String objectToString(Object object) {
        String g = ((LocalDateTime) object).toString();
        return g.split("T")[0];//.replace("-", " ");
    }


    @Override
    public Object stringToObject(String stringValue) {
        return LocalDateTime.parse(stringValue);
    }
}
