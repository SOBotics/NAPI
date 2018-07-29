package in.bhargavrao.napi.objects;

/**
 * Created by bhargav.h on 10-Mar-17.
 */
public class ErrorMessage extends Message {

    private String error;

    public ErrorMessage(String error) {
        this.setMessage("error");
        this.error = error;
    }
}
