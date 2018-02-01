package in.bhargavrao.napi.objects;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargav.h on 13-Mar-17.
 */
public class Report extends Item{


    private String link;
    private Long timestamp;
    private Double naaValue;
    private Integer bodyLength;
    private Integer reputation;
    private List<Reason> reasons;

    public Report() {
        reasons = new ArrayList<>();
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Double getNaaValue() {
        return naaValue;
    }

    public void setNaaValue(Double naaValue) {
        this.naaValue = naaValue;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Integer getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(Integer bodyLength) {
        this.bodyLength = bodyLength;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public List<Reason> getReasons() {
        return reasons;
    }

    public void addReason(Reason reason) {
        this.reasons.add(reason);
    }

    public void setReasons(List<Reason> reasons) {
        this.reasons = reasons;
    }
}
