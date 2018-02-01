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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhargav.h on 09-Mar-17.
 */

@Path("/feedback")
public class GetFeedback {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAllFeedback(){
        return getFeedbackMessage();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getFeedbackForPost(@PathParam("id") String id){
        return getFeedbackMessage(id);
    }

    private Message getFeedbackMessage()  {
        SuccessMessage successMessage = new SuccessMessage();

        List<Feedback> feedbacks;
        try {
            feedbacks = getFeedbacks();
        } catch (PropertiesNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        } catch (FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }
        for(Feedback feedback: feedbacks)
            successMessage.addItem(feedback);
        //feedbacks.forEach(successMessage::addItem);
        successMessage.setMessage("success");
        return successMessage;
    }

    private Message getFeedbackMessage(String id)  {
        SuccessMessage successMessage = new SuccessMessage();

        Feedback feedback;
        try {
            feedback = getFeedback(id);
        } catch (PropertiesNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        } catch (FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }

        successMessage.addItem(feedback);
        //feedbacks.forEach(successMessage::addItem);
        successMessage.setMessage("success");
        return successMessage;
    }

    private List<Feedback> getFeedbacks() throws PropertiesNotAvailableException, FileNotAvailableException {
        List<Feedback> feedbacks = new ArrayList<>();
        String outputFilePath = PropertyUtils.getProperty("logsPath")+ FilePathUtils.outputLogFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                Feedback feedback = CsvUtils.getFeedbackFromLine(word);
                if (feedback!=null)
                    feedbacks.add(feedback);
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }
        return feedbacks;
    }

    private Feedback getFeedback(String id) throws PropertiesNotAvailableException, FileNotAvailableException {
        Feedback feedback = null;
        String outputFilePath = PropertyUtils.getProperty("logsPath")+ FilePathUtils.outputLogFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                String feedbackId = CsvUtils.getPostIdFromOutput(word);
                if(feedbackId.trim().equals(id)) {
                    feedback = CsvUtils.getFeedbackFromLine(word);
                    break;
                }
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }

        if (feedback==null){

            String reportsFilePath = PropertyUtils.getProperty("logsPath")+ FilePathUtils.reportsLogFile;
            if(outputFilePath.startsWith("Error")){
                throw new PropertiesNotAvailableException(reportsFilePath);
            }
            try {
                for (String word: Files.readAllLines(Paths.get(reportsFilePath))){
                    String feedbackId = CsvUtils.getPostIdFromReport(word);
                    if(feedbackId.trim().equals(id)) {
                        Report report = CsvUtils.getReportFromLine(word, "stackoverflow");
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
