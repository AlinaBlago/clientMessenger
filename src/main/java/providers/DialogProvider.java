package providers;

import javafx.scene.control.Alert;

import javax.swing.text.AbstractDocument;

public class DialogProvider {
    public static void ShowDialog(String Title , String content){
        ConfigureAlert(Title , content).show();
    }

    public static void ShowDialog(String Title , String content , Alert.AlertType type){
        Alert alert = ConfigureAlert(Title , content);
        alert.setAlertType(type);
        alert.show();
    }

    private static Alert ConfigureAlert(String Title , String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(Title);
        alert.setHeaderText(content);
        return alert;
    }
}
