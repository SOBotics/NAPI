package in.bhargavrao.napi.utils;

import in.bhargavrao.napi.objects.Feedback;
import in.bhargavrao.napi.objects.Reason;
import in.bhargavrao.napi.objects.Report;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargav.h on 13-Mar-17.
 */
public class CsvUtils {

    public static String getPostIdFromOutput(String word){
        String parts[] = word.split(",");
        if (parts.length!=7)
            return null;
        return parts[1];
    }
    public static String getPostIdFromReport(String word){
        String parts[] = word.split(",");
        if (parts.length!=6)
            return null;
        return parts[0];
    }

    public static Feedback getFeedbackFromLine(String word, String sitename) {
        Feedback feedback = new Feedback();
        String parts[] = word.split(",");
        if (parts.length!=7)
            return null;

        switch (parts[0]){
            case "tp":
                feedback.setType("True Positive");
                break;
            case "fp":
                feedback.setType("False Positive");
                break;
            case "tn":
                feedback.setType("True Negative");
                break;
            case "ne":
                feedback.setType("Needs Edit");
                break;
        }

        feedback.setName(parts[1]);
        feedback.setLink("https://"+sitename+".com/a/"+parts[1]);
        feedback.setTimestamp(Instant.parse(parts[2]).toEpochMilli());
        feedback.setNaaValue(Double.parseDouble(parts[3]));
        feedback.setBodyLength(Integer.parseInt(parts[4]));
        feedback.setReputation(Integer.parseInt(parts[5]));
        List<Reason> reasons = getReasons(parts[6]);
        for (Reason reason:reasons)
            feedback.addReason(reason);
        //reasons.forEach(feedback::addReason);
        return feedback;
    }

    public static Report getReportFromLine(String word, String site) {
        Report report = new Report();
        String parts[] = word.split(",");
        if (parts.length!=6)
            return null;
        report.setName(parts[0]);
        report.setLink("https://"+site+".com/a/"+parts[0]);
        report.setTimestamp(Instant.parse(parts[1]).toEpochMilli());
        report.setNaaValue(Double.parseDouble(parts[2]));
        if(report.getNaaValue()>7.0)
            report.setType("Report - Autoflagged");
        else
            report.setType("Report");
        report.setBodyLength(Integer.parseInt(parts[3]));
        report.setReputation(Integer.parseInt(parts[4]));
        List<Reason> reasons = getReasons(parts[5]);
        for (Reason reason: reasons)
            report.addReason(reason);
        //reasons.forEach(report::addReason);
        return report;
    }

    private static List<Reason> getReasons(String part) {
        List<Reason> reasons = new ArrayList<>();
        String reasonStrings[] = part.split(";");

        for (String reasonString: reasonStrings){
            Reason reason = new Reason();

            if (reasonString.contains("-")){
                reason.setReasonName(reasonString.split(" - ")[0]);
                reason.setSubReason(reasonString.split(" - ")[1]);
            }
            else {
                reason.setReasonName(reasonString);
            }
            reasons.add(reason);
        }
        return reasons;
    }
}
