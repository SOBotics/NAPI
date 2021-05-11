package in.bhargavrao.napi.clients;


import in.bhargavrao.napi.exceptions.FileNotAvailableException;
import in.bhargavrao.napi.exceptions.PropertiesNotAvailableException;
import in.bhargavrao.napi.objects.*;
import in.bhargavrao.napi.utils.CsvUtils;
import in.bhargavrao.napi.utils.FilePathUtils;
import in.bhargavrao.napi.utils.PropertyUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static in.bhargavrao.napi.utils.FilePathUtils.getSitePathFromSiteName;

/**
 * Created by bhargav.h on 09-Mar-17.
 */

@Path("/feedback")
public class GetFeedback {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAllFeedback(){
        return getFeedbackMessage("stackoverflow");
    }

    @GET
    @Path("/all/au")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAllAUFeedback(){
        return getFeedbackMessage("askubuntu");
    }

    @GET
    @Path("/{ids}")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getFeedbackForPost(@PathParam("ids") String ids) {
        return getFeedbackMessage(ids.split(","), "stackoverflow");
    }

    @GET
    @Path("/{ids}/au")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getFeedbackForAUPost(@PathParam("ids") String ids) {
        return getFeedbackMessage(ids.split(","), "askubuntu");
    }

    private Message getFeedbackMessage(String sitename)  {
        SuccessMessage successMessage = new SuccessMessage();

        List<Feedback> feedbacks;
        try {
            feedbacks = getFeedbacks(sitename);
        } catch (PropertiesNotAvailableException | FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }
        for(Feedback feedback: feedbacks)
            successMessage.addItem(feedback);
        //feedbacks.forEach(successMessage::addItem);
        successMessage.setMessage("success");
        return successMessage;
    }

    private Message getFeedbackMessage(String[] ids, String sitename)  {
        SuccessMessage successMessage = new SuccessMessage();

        try {
            for (String postId : ids) {
                Feedback feedback = getFeedback(postId, sitename);
                successMessage.addItem(feedback);
            }
        } catch (PropertiesNotAvailableException | FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }

        successMessage.setMessage("success");
        return successMessage;
    }

    private List<Feedback> getFeedbacks(String sitename) throws PropertiesNotAvailableException, FileNotAvailableException {
        List<Feedback> feedbacks = new ArrayList<>();
        String outputFilePath = PropertyUtils.getProperty(sitename)+ FilePathUtils.outputLogFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                Feedback feedback = CsvUtils.getFeedbackFromLine(word, sitename);
                if (feedback!=null)
                    feedbacks.add(feedback);
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }
        return feedbacks;
    }

    private Feedback getFeedback(String id, String sitename) throws PropertiesNotAvailableException, FileNotAvailableException {
        Feedback feedback = null;
        String logsPath =  getSitePathFromSiteName(sitename);
        String outputFilePath = PropertyUtils.getProperty(logsPath)+ FilePathUtils.outputLogFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                String feedbackId = CsvUtils.getPostIdFromOutput(word);
                if (feedbackId != null && feedbackId.trim().equals(id)) {
                    feedback = CsvUtils.getFeedbackFromLine(word, sitename);
                    break;
                }
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }

        if (feedback==null){

            String reportsFilePath = PropertyUtils.getProperty(logsPath)+ FilePathUtils.reportsLogFile;
            if(outputFilePath.startsWith("Error")){
                throw new PropertiesNotAvailableException(reportsFilePath);
            }
            try {
                for (String word: Files.readAllLines(Paths.get(reportsFilePath))){
                    String feedbackId = CsvUtils.getPostIdFromReport(word);
                    if (feedbackId != null && feedbackId.trim().equals(id)) {
                        Report report = CsvUtils.getReportFromLine(word, sitename);
                        if (report != null) {
                            feedback = new Feedback(
                                    report.getLink(),
                                    report.getTimestamp(),
                                    report.getNaaValue(),
                                    report.getBodyLength(),
                                    report.getReputation(),
                                    report.getReasons()
                            );
                            feedback.setName(report.getName());
                            feedback.setType("None");
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                throw  new FileNotAvailableException("Error 1: File not found");
            }
        }

        return feedback;
    }

}
