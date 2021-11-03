package mainPackage;

import controllers.Controller;
import dataBase.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private Controller controller;
    private SaverAndReaderSettingsForm saverAndReaderSettingsForm;
    private ConnectingDataBase connectingDataBase;
    private boolean badProtocol = false; // индикатор плохого протокола
    private Thread thread;//для отображения точек
    private boolean stopThread = false;

    private void createRootStage() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/filesFXML/sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("form_From_MRV");
        primaryStage.setScene(new Scene(root,731,588));

        primaryStage.setResizable(false);
        primaryStage.show();

        controller = loader.getController();
        controller.setMain(this);

        controller.read(saverAndReaderSettingsForm);
        controller.dragAndDropEvents(); //запускаем drag&drop
        controller.addStendNamesInMenu();
    }

    public void startBuilding(){
        ParserRTF parserRTF = new ParserRTF(controller.getPathToFile());
        parserRTF.parsing();

        DistributionChannelsToProtocols distributionChannelsToProtocols = new DistributionChannelsToProtocols(parserRTF, connectingDataBase,this);
        distributionChannelsToProtocols.distribution();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        createRootStage();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void stop(){
        //все что происходит после  закрытия окна
        saverAndReaderSettingsForm.save(controller);
        controller.setWindowsIsClose(true);
        connectingDataBase.close();

    }

    public void init(){
        //все что происходи до момента запуска

        System.out.println("------------------------------------------------------------------------");

        System.out.println(
                "  o          o        o__ __o          o              o  \n" +
                " <|\\        /|>      <|     v\\        <|>            <|> \n" +
                " / \\\\o    o// \\      / \\     <\\       < >            < > \n" +
                " \\o/ v\\  /v \\o/      \\o/     o/        \\o            o/  \n" +
                "  |   <\\/>   |        |__  _<|          v\\          /v   \n" +
                " / \\        / \\       |       \\          <\\        />    \n" +
                " \\o/        \\o/      <o>       \\o          \\o    o/      \n" +
                "  |          |        |         v\\          v\\  /v       \n" +
                " / \\        / \\      / \\         <\\          <\\/>    ");

        System.out.println("------------------------------------------------------------------------");

        System.out.println("+-++-++-++-+ +-++-++-++-+ +-++-++-+\n" +
                "|F||O||R||M| |F||R||O||M| |M||R||V|\n" +
                "+-++-++-++-+ +-++-++-++-+ +-++-++-+" + "\n");

        System.out.println("+-++-++-++-++-++-++-+ +-++-++-++-++-++-++-+ +-+ +-++-++-+\n" +
                "|p||r||o||g||r||a||m| |v||e||r||s||i||o||n| |:| |1||.||0|\n" +
                "+-++-++-++-++-++-++-+ +-++-++-++-++-++-++-+ +-+ +-++-++-+");


        System.out.print("Загрузка базы данных");

        thread = new Thread(() -> {

            while(true){

                if(stopThread)
                    break;

                System.out.print(".");

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        connectingDataBase = new ConnectingDataBase();

        saverAndReaderSettingsForm = new SaverAndReaderSettingsForm(this);

        System.out.print("Загрузка настроек");
    }

    public ConnectingDataBase getConnectingDataBase(){
        return connectingDataBase;
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }

    public Controller getController(){
        return controller;
    }

    public boolean isBadProtocol() {
        return badProtocol;
    }

    public void setBadProtocol(boolean badProtocol) {
        this.badProtocol = badProtocol;
    }

    public void stopThreadForPrintPoints(){
       stopThread = true;
    }


}
