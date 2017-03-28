package com.kevin_mic.aqua.model.schedule;

import lombok.Data;

@Data
public class OnOffTime {
    HourMinute on;
    HourMinute off;

    public boolean isValid() {
        if (on == null || off == null) {
            return false;
        }
        if (on.equals(off)) {
            return false;
        }

        return true;
    }
}
