package ru.chuprikov.search.web.index;

import ru.chuprikov.search.database.datatypes.Datatypes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PostingInfo {
    private PostingPositionInfo[] positions = new PostingPositionInfo[0];

    public PostingInfo() {
    }

    public PostingInfo(Datatypes.Posting posting) {
        documentID = posting.getDocumentID();
        positions = new PostingPositionInfo[posting.getPositionsList().size()];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new PostingPositionInfo(posting.getPositions(i));
        }
    }

    public long getDocumentID() {
        return documentID;
    }

    public void setDocumentID(long documentID) {
        this.documentID = documentID;
    }

    public PostingPositionInfo[] getPositions() {
        return positions;
    }

    public void setPositions(PostingPositionInfo[] positions) {
        this.positions = positions;
    }

    private long documentID;
}
