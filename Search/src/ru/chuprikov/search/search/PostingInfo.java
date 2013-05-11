package ru.chuprikov.search.search;

import ru.chuprikov.search.datatypes.Datatypes;

import java.util.ArrayList;
import java.util.List;

public class PostingInfo implements Comparable<PostingInfo> {
    private final long documentID;
    private final Datatypes.Posting.PositionType positionType;

    private final ArrayList<PositionInfo> positions = new ArrayList<>();

    PostingInfo(long documentID, Datatypes.Posting.PositionType positionType) {
        this.documentID = documentID;
        this.positionType = positionType;
    }

    List<PositionInfo> positions() {
        return positions;
    }

    long getDocumentID() {
        return documentID;
    }

    Datatypes.Posting.PositionType getPositionType() {
        return positionType;
    }

    @Override
    public int compareTo(PostingInfo o) {
        if (documentID != o.documentID) {
            return Long.compare(documentID, o.documentID);
        } else {
            return positionType.compareTo(o.positionType);
        }
    }
}
