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
import java.util.Arrays;
import java.util.List;

@Path("/stored")
public class GetStoredReports {

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getSoReports(){
        return getItems("stackoverflow");
    }

    @GET
    @Path("/all/au")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAuReports(){
        return getItems("askubuntu");
    }

    @GET
    @Path("/{ids}")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getReportsByIds(@PathParam("ids") String ids) {
        return getItems("stackoverflow", ids.split(","));
    }

    @GET
    @Path("/{ids}/au")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getReportsByIds(@PathParam("ids") String ids) {
        return getItems("askubuntu", ids.split(","));
    }

    private Message getItems(String site, String[] ids) {
        SuccessMessage successMessage = new SuccessMessage();
        List<Item> items;
        try {
            items = getItem(site, ids);
        } catch (PropertiesNotAvailableException | FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }
        for(Item item:items)
            successMessage.addItem(item);
        successMessage.setMessage("success");
        return successMessage;
    }

    private Message getItems(String site) {
        String[] fakeArray = new String[0];
        return getItems(site, fakeArray);
    }


    private List<Item> getItem (String site, String[] ids) throws PropertiesNotAvailableException, FileNotAvailableException {
        List<Item> items = new ArrayList<>();

        String logsPath;
        switch (site) {
            case "askubuntu": logsPath = "auLogsPath"; break;
            default: logsPath = "logsPath"; break;
        }
        String outputFilePath = PropertyUtils.getProperty(logsPath)+ FilePathUtils.reportsFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                Item item = new Item();
                item.setName(word.trim());
                item.setType("Stored post");
                if (ids.length == 0 || Arrays.asList(ids).contains(word.trim())) {
                    items.add(item);
                }
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }
        String outputCsvFilePath = PropertyUtils.getProperty(logsPath)+ FilePathUtils.outputLogFile;
        if(outputCsvFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputCsvFilePath))){
                Item item = new Item();
                String postId = CsvUtils.getPostIdFromOutput(word);
                item.setName(postId);
                item.setType("Stored post");
                if (ids.length == 0 || Arrays.asList(ids).contains(postId)) {
                    items.add(item);
                }
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }
        return items;
    }

}
