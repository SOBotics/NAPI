package in.bhargavrao.napi.objects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargav.h on 10-Mar-17.
 */
public class Feedback extends Item{
    String link;
    long timestamp;
    double naaValue;
    int bodyLength;
    int reputation;
    List<Reason> reasons;

    public Feedback() {
        reasons = new ArrayList<>();
    }

    public Feedback(String link, long timestamp, double naaValue, int bodyLength, int reputation, List<Reason> reasons) {
        this.link = link;
        this.timestamp = timestamp;
        this.naaValue = naaValue;
        this.bodyLength = bodyLength;
        this.reputation = reputation;
        this.reasons = reasons;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getNaaValue() {
        return naaValue;
    }

    public void setNaaValue(double naaValue) {
        this.naaValue = naaValue;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public List<Reason> getReasons() {
        return reasons;
    }

    public void addReason(Reason reason) {
        this.reasons.add(reason);
    }
}
