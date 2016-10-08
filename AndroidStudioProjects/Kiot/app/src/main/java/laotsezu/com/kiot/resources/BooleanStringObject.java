package laotsezu.com.kiot.resources;

/**
 * Created by Laotsezu on 9/19/2016.
 */
public class BooleanStringObject {
    boolean status;
    String message;
    public BooleanStringObject(boolean status,String message){
        this.status = status;
        this.message=  message;
    }
    public String getMessage() {
        return message;
    }
    public boolean getStatus(){
        return status;
    }
}
