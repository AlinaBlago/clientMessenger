package exceptions;

public class ValidationException extends Exception{
    private String propertyName;
    private String propertyError;

    public String GetPropertyName(){
        return propertyName;
    }
    public String GetPropertyError(){
        return propertyError;
    }

    public ValidationException(String message , String propertyName , String propertyError){
        super(message);
        this.propertyError = propertyError;
        this.propertyName = propertyName;
    }
}
