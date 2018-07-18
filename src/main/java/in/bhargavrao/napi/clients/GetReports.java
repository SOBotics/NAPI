package in.bhargavrao.napi.clients;

import in.bhargavrao.napi.exceptions.FileNotAvailableException;
import in.bhargavrao.napi.exceptions.PropertiesNotAvailableException;
import in.bhargavrao.napi.objects.*;
import in.bhargavrao.napi.utils.CsvUtils;
import in.bhargavrao.napi.utils.FilePathUtils;
import in.bhargavrao.napi.utils.PropertyUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static in.bhargavrao.napi.utils.FilePathUtils.getSitePathFromSiteName;

/**
 * Created by bhargav.h on 13-Mar-17.
 */

@Path("/reports")
public class GetReports {


    @GET
    @Path("/")
    @Produces("text/html")
    public Response getReportIndex(){
        return Response.status(200).entity("Use reports/all").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getSoReports(@DefaultValue("111111") @QueryParam("filter") String filter){
        return getReportsMessage(filter, "stackoverflow");
    }

    @GET
    @Path("/all/au")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getAuReports(@DefaultValue("111111") @QueryParam("filter") String filter){
        return getReportsMessage(filter, "askubuntu");
    }

    public Message getReportsMessage(String filter, String site ){
        SuccessMessage successMessage = new SuccessMessage();
        List<Report> reports;
        try {
            reports = getReports(site);
        } catch (PropertiesNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        } catch (FileNotAvailableException e) {
            return new ErrorMessage(e.getMessage());
        }
        reports = filterReports(reports,filter);
        for(Report report:reports)
            successMessage.addItem(report);
        //reports.forEach(successMessage::addItem);
        successMessage.setMessage("success");
        return successMessage;
    }

    private List<Report> filterReports(List<Report> reports, String filter){
        char filterParts[] =  filter.toCharArray();
        for(Report report : reports){
            for (int i = 0; i<filterParts.length; i++){
                if (filterParts[i]=='0'){
                    switch (i){
                        case 0: report.setBodyLength(null); break;
                        case 1: report.setLink(null); break;
                        case 2: report.setNaaValue(null); break;
                        case 3: report.setReasons(null); break;
                        case 4: report.setReputation(null); break;
                        case 5: report.setTimestamp(null); break;
                    }
                }
            }
        }
        return reports;
    }



    private List<Report> getReports(String site) throws PropertiesNotAvailableException, FileNotAvailableException {
        List<Report> reports = new ArrayList<>();

        String logsPath =  getSitePathFromSiteName(site);
        String outputFilePath = PropertyUtils.getProperty(logsPath)+ FilePathUtils.reportsLogFile;
        if(outputFilePath.startsWith("Error")){
            throw new PropertiesNotAvailableException(outputFilePath);
        }
        try {
            for (String word: Files.readAllLines(Paths.get(outputFilePath))){
                Report report = CsvUtils.getReportFromLine(word, site);
                if (report!=null)
                    reports.add(report);
            }
        } catch (IOException e) {
            throw  new FileNotAvailableException("Error 1: File not found");
        }
        return reports;
    }



}
