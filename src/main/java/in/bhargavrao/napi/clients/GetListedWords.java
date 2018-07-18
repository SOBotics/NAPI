package in.bhargavrao.napi.clients;


import in.bhargavrao.napi.objects.ErrorMessage;
import in.bhargavrao.napi.objects.Item;
import in.bhargavrao.napi.objects.Message;
import in.bhargavrao.napi.objects.SuccessMessage;
import in.bhargavrao.napi.utils.FilePathUtils;
import in.bhargavrao.napi.utils.PropertyUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by bhargav.h on 09-Mar-17.
 */

@Path("/list")
public class GetListedWords {

    @GET
    @Path("/blacklistedWords")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getBlacklistedWords(){
        return getListedWords(FilePathUtils.blacklistedFile, "blacklisted word");
    }

    @GET
    @Path("/whitelistedWords")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getWhitelistedWords(){
        return getListedWords(FilePathUtils.whitelistedFile, "whitelisted word");
    }

    @GET
    @Path("/salutations")
    @Produces(MediaType.APPLICATION_JSON)
    public Message getSalutations(){
        return getListedWords(FilePathUtils.salutationsFile, "salutation");
    }

    private Message getListedWords(String fileName, String returnType) {
        SuccessMessage successMessage = new SuccessMessage();

        String listedFilePath = PropertyUtils.getProperty("dataPath")+fileName;

        if(listedFilePath.startsWith("Error")){
            return new ErrorMessage(listedFilePath);
        }

        try {
            for (String word: Files.readAllLines(Paths.get(listedFilePath))){
                Item item = new Item();
                item.setName(word);
                item.setType(returnType);
                successMessage.addItem(item);
            }
            successMessage.setMessage("success");
        } catch (IOException e) {
            return new ErrorMessage(returnType + " file not found");
        }
        return successMessage;
    }
}
