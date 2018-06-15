package com.alexbros.opidlubnyi.allfootball.models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class League extends ExpandableGroup {
    public League(String title, List items) {
        super(title, items);
    }
}
