package providers;

import controller.impl.ApplicationControllerImpl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationProvider {
    public static void NavigateToMainForm(Stage currentStage) throws IOException {
        Stage applStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationControllerImpl.class.getResource("/application.fxml"));
        Parent root = loader.load();
        applStage.setScene(new Scene(root, 620, 680));
        applStage.show();

        currentStage.close();
    }
}
