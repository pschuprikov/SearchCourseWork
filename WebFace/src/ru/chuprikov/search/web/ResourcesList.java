package ru.chuprikov.search.web;

import ru.chuprikov.search.misc.ProblemSetName;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ResourcesList {
    public ResourcesList() {
        values =  new String[ProblemSetName.values().length];
        for (int i = 0; i < values.length; i++)
            values[i] = ProblemSetName.values()[i].name();
    }

    public String[] getValues() {
        return values;
    }

    private final String[] values;
}
