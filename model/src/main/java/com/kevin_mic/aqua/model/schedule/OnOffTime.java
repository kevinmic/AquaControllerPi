package com.kevin_mic.aqua.model.schedule;

import lombok.Data;

@Data
public class OnOffTime {
    HourMinute on;
    HourMinute off;
}
