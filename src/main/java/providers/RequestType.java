package providers;

public enum RequestType {
    GET("GET"), POST("POST");

    private String type;
    RequestType(String code){
        this.type = code;
    }
    public String getType(){ return type;}
}
