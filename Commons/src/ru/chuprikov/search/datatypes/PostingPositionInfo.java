package ru.chuprikov.search.datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostingPositionInfo {

    public PostingPositionInfo() {

    }

    public PostingPositionInfo(Datatypes.Posting.Position position) {
        type = position.getType();
        index = position.getIndex();
    }

    public Datatypes.Posting.PositionType getType() {
        return type;
    }

    public void setType(Datatypes.Posting.PositionType type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Datatypes.Posting.PositionType type;
    private int index;
}
