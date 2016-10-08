package laotsezu.com.kiot.trade;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laotsezu on 17/09/2016.
 */
public class Agency {
    String agency_id;
    String agency_ten;
    String agency_dia_chi;
    String agency_sdt;
    int agency_status;
    public Agency(JSONObject input){
        try {
            this.agency_id = input.getString("agency_id");
            this.agency_ten = input.getString("agency_ten");
            this.agency_dia_chi = input.getString("agency_dia_chi");
            this.agency_sdt = input.getString("agency_sdt");
            this.agency_status = input.getInt("agency_status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Agency(){
        this.agency_id = "0";
        this.agency_ten = "Chi Nhánh Trung Tâm";
        this.agency_dia_chi = "Mặc định";
        this.agency_sdt = "Mặc định";
        agency_status = 1;
    }
    public static Agency getDefautAgency(){
        return new Agency();
    }
    public String getAgency_id() {
        return agency_id;
    }

    public String getAgency_ten() {
        return agency_ten;
    }

    public String getAgency_dia_chi() {
        return agency_dia_chi;
    }

    public String getAgency_sdt() {
        return agency_sdt;
    }

    public int getAgency_status() {
        return agency_status;
    }
    public static String getDefaultId(){
        return "0";
    }
    public static String getDefaultTen(){
        return "Chi nhánh trung tâm";
    }
}
