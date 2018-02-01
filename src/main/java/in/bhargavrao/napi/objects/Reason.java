package in.bhargavrao.napi.objects;


import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by bhargav.h on 10-Mar-17.
 */
public class Reason {
    private String reasonName;
    private String subReason;

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public String getSubReason() {
        return subReason;
    }

    public void setSubReason(String subReason) {
        this.subReason = subReason;
    }
}
