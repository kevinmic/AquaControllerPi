package com.kevin_mic.aqua.model.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnOffTime implements Comparable<OnOffTime> {
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

    @Override
    public int compareTo(OnOffTime obj) {
        return getOn().compareTo(obj.getOn());
    }
}
