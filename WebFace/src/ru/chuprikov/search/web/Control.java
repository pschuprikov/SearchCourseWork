package ru.chuprikov.search.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class Control implements Serializable{
    private int activeIdx = 7;

    public int getActiveIdx() {
        return activeIdx;
    }

    public void setActiveIdx(int activeIdx) {
        this.activeIdx = activeIdx;
    }
}
