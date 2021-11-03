package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controllers.SampleController;

import java.io.IOException;

public class Main extends Application {

    private SampleController sampleController;//контроллер окна авторизации
    private ConnectToServer connectToServer;//соединение с сервером

    @Override
    public void start(Stage primaryStage) throws Exception{ //переопределенный метод для запуска приложения
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/filesFXML/sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Main Window");
        primaryStage.setScene(new Scene(root));

        primaryStage.setResizable(false);
        primaryStage.show();

        sampleController = loader.getController();
        sampleController.setMain(this);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void init(){ //то, что происходит до запуска основной части программы
        connectToServer = new ConnectToServer(); //создаем новое соединение
        connectToServer.startClientConnect();//запуск сетевого соединения клиента
    }

    public FXMLLoader openNewWindow(Button button, String pathToFXML, String windowName){ //метод открытия текущего окна и запуска нового
        try {
            Stage stage = (Stage) button.getScene().getWindow();
            stage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFXML));
            Parent root = (Parent) loader.load();

            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.setScene(newScene);
            newStage.setTitle(windowName);
            newStage.show();
            newStage.setResizable(false);


            newStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });

            return loader;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ConnectToServer getConnectToServer() {
        return connectToServer;
    }
}
