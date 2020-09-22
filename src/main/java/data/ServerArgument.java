package data;

public class ServerArgument {
    public String Name;
    public String Value;

    public ServerArgument(String name, String value) {
        Name = name;
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String toServerStyleString(){
        return getName() + "=" + getValue();
    }
}
