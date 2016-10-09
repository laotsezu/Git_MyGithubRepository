package laotsezu.com.kiot.utilities;

/**
 * Created by laotsezu on 09/10/2016.
 */

public class StartAndLimit {
    int start,limit;
    public StartAndLimit(int start,int limit){
        this.start = start;
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public int getStart() {
        return start;
    }
}
