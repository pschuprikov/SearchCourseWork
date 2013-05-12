package ru.chuprikov.search.web;

import org.primefaces.event.TabChangeEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@ManagedBean
@SessionScoped
public class Control implements Serializable{
    private int activeIdx = 7;

    private final Map<String, Integer> tabs = new HashMap<>();

    public Control() {
        tabs.put("fetch control", 0);
        tabs.put("fetch data", 1);
        tabs.put("parse data", 2);
        tabs.put("parse control", 3);
        tabs.put("terms data", 4);
        tabs.put("index control", 5);
        tabs.put("document data", 6);
        tabs.put("search", 7);
    }

    public void onTabChange(TabChangeEvent ev) {
        setActiveIdx(tabs.get(ev.getTab().getTitle()));
    }

    public int getActiveIdx() {
        return activeIdx;
    }

    public void setActiveIdx(int activeIdx) {
        this.activeIdx = activeIdx;
    }
}
