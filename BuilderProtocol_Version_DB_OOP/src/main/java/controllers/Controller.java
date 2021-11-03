package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import mainPackage.Main;
import mainPackage.SaverAndReaderSettingsForm;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonCreateProtocols;

    @FXML
    private TextField textFieldCertificate;

    @FXML
    private Label LabelCreateProtocols;

    @FXML
    private Label LabelPathToOpen;

    @FXML
    private Label LabelPathToSave;

    @FXML
    private Label LabelSave;

    @FXML
    private Label LabelTime;

    @FXML
    private TextArea textAreaInfo;

    @FXML
    private ProgressBar progressBarForProtocols;

    @FXML
    private Menu measuringInstrumentMenu;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem clearProtocolsListMenu;

    @FXML
    private Label LabelDataBase;

    @FXML
    private ComboBox<String> measuringInstrumentList;

    private String certificate; // номер сертификата
    private String pathToFile; // путь к файлу
    private String pathToSaveForGroupProtocols; // путь для сохранения - для каждого протокола будет изменяться
    private String pathToSave = ""; //путь сохранения, выбранный пользователем. Меняется только с кнопки на форме
    private String stendName; // название стенда, по которому будет загружаться БД
    private List<File> protocolsList; //список выбранных протоколов
  //  private ToggleGroup stendNamesGroup; //группа с названиями стендов, в меню главного экрана, нужна для возможности выделения только одного элемента из меню

    private boolean DBIsOk = false; // индикатор наличия БД
    private boolean isOpen = false; //индикатор выбран файл или нет
    private boolean isSave = false;//индикатор выбран каталог или нет
    private boolean protocolsIsReady = true; //индикатор готовности выбранных протоколов
    private boolean windowsIsClose = false; //индикатор закрытия приложения
    private int countSelectedProtocols = 0; //счетчик выбранных протоколов(из проводника)
    private int countGoodProtocols; //счетчик хороших протоколов(без ошибок)
    private int countBadProtocols; //счетчик плохих протоколов
    private Main main;


    @FXML
    void initialize() {

        LabelTime.setTextFill(Color.GREEN);
        buttonCreateProtocols.setDisable(true);
        textAreaInfo.setFont(new Font("Times New Roman", 14));

/*
        measuringInstrumentMenu.setOnAction(actionEvent -> {

            stendName = (((RadioMenuItem) stendNamesGroup.getSelectedToggle()).getText());

            if(main.getConnectingDataBase().checkFoundStendNameInDataBase(stendName)){
                System.out.println("Выбрана база данных для системы: " + stendName);
                LabelDataBase.setText("OK");
                LabelDataBase.setTextFill(Color.GREEN);
                DBIsOk = true;
                LabelNameDataBase.setText(stendName);

            }else{

                System.out.println("Не найдена база данных для : " + stendName);
                LabelNameDataBase.setText("not found");
                LabelDataBase.setText("ERROR");
                LabelDataBase.setTextFill(Color.RED);
                DBIsOk = false;
                buttonCreateProtocols.setDisable(true);
                LabelCreateProtocols.setText("ERROR");
                LabelCreateProtocols.setTextFill(Color.RED);
            }

            if (isOpen && isSave && DBIsOk) {
                LabelCreateProtocols.setText("OK");
                buttonCreateProtocols.setDisable(false);
                LabelCreateProtocols.setTextFill(Color.GREEN);
            }

        });*/

        measuringInstrumentList.setOnAction(actionEvent -> {

            stendName = measuringInstrumentList.getValue();

            if(main.getConnectingDataBase().checkFoundStendNameInDataBase(stendName)){
                System.out.println("Выбрана база данных для системы: " + stendName);
                LabelDataBase.setText("OK");
                LabelDataBase.setTextFill(Color.GREEN);
                DBIsOk = true;

            }else{

                System.out.println("Не найдена база данных для : " + stendName);
                LabelDataBase.setText("ОШИБКА");
                LabelDataBase.setTextFill(Color.RED);
                DBIsOk = false;
                buttonCreateProtocols.setDisable(true);
                LabelCreateProtocols.setText("ОШИБКА");
                LabelCreateProtocols.setTextFill(Color.RED);
            }

            if (isOpen && isSave && DBIsOk) {
                LabelCreateProtocols.setText("OK");
                buttonCreateProtocols.setDisable(false);
                LabelCreateProtocols.setTextFill(Color.GREEN);
            }

        });


        buttonSave.setOnAction(actionEvent -> {

            DirectoryChooser directoryChooser = new DirectoryChooser(); // выбор каталога в проводнике
            try {
                directoryChooser.setInitialDirectory(new File(pathToSave.isEmpty() ? "C:\\" : pathToSave));
                File dir = directoryChooser.showDialog(main.getPrimaryStage());
                pathToSave = dir.getAbsolutePath();
                LabelPathToSave.setText("OK");
                LabelPathToSave.setTextFill(Color.GREEN);
                isSave = true;
                LabelSave.setText(pathToSave);
                System.out.println("Путь к сохранению: " + pathToSave);

                if (isOpen && isSave && DBIsOk) {
                    LabelCreateProtocols.setText("OK");
                    buttonCreateProtocols.setDisable(false);
                    LabelCreateProtocols.setTextFill(Color.GREEN);
                }

            }catch (NullPointerException e){
                System.out.println("Каталог для сохранения не выбран!");
                LabelPathToSave.setText("ОШИБКА");
                LabelPathToSave.setTextFill(Color.RED);
                isSave = false;

                LabelSave.setText("не выбран");

                LabelCreateProtocols.setText("ОШИБКА");
                buttonCreateProtocols.setDisable(true);
                LabelCreateProtocols.setTextFill(Color.RED);

            }catch (IllegalArgumentException e){
                System.out.println("Каталог не найден");
                pathToSave = "";
            }


        });
        buttonCreateProtocols.setOnAction(ActionEvent -> {

            startBuildingProtocols();

        });
        clearProtocolsListMenu.setOnAction(ActionEvent ->{

            if(protocolsList != null) {
                protocolsIsReady = false;
                protocolsList.clear();
                progressBarForProtocols.setProgress(0);
                addTextOnTextArea();
                buttonCreateProtocols.setDisable(true);
                LabelCreateProtocols.setText("ОШИБКА");
                LabelCreateProtocols.setTextFill(Color.RED);
                LabelPathToOpen.setText("ОШИБКА");
                LabelPathToOpen.setTextFill(Color.RED);
            }

        });
        closeMenuItem.setOnAction(ActionEvent -> {
            main.getPrimaryStage().close();
            windowsIsClose = true;

        });

        liveUI(); //запуск потока для отображения прогресса

    }


    private void liveUI(){ //поток для отображения прогресса выполнения на UI

        Thread threadUI = new Thread(() -> {

        while(!windowsIsClose){ //пока окно не закрыто

            while(!protocolsIsReady && buttonCreateProtocols.isDisable()){ //пока все протоколы не сформированы

                if(protocolsList != null) {
                    progressBarForProtocols.setProgress((double)countSelectedProtocols / (double) protocolsList.size());
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("error in UI");
                    e.printStackTrace();
                }

            }



            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("error in UI");
                e.printStackTrace();
            }
        }
    });



        threadUI.start();

    }

    private void startBuildingProtocols() { //основной поток программы

        Thread thread = new Thread(() -> {

            long firstTime = System.currentTimeMillis();
            buttonCreateProtocols.setDisable(true);
            textAreaInfo.clear();
            countSelectedProtocols = 0;
            protocolsIsReady = false;
            progressBarForProtocols.setProgress(0);
            countBadProtocols = 0;
            countGoodProtocols = 0;

            certificate = textFieldCertificate.getText();

            System.out.println("Выбрано протоколов для обработки: " + protocolsList.size());
            System.out.println("Выбранные протоколы:");

            for (int i = 0; i < protocolsList.size(); i++) { //выводим в консоль информацию о выбранных протоколах
                System.out.println("Протокол № " + (i + 1) + " - " + protocolsList.get(i));
            }

            for (File file : protocolsList) { // по очереди обрабатываем каждый протокол

                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                pathToFile = file.getAbsolutePath();
                //thePathToSaveForGroupProtocols = thePathToSave + "\\" + file.getName().split(".rtf")[0];
                pathToSaveForGroupProtocols = pathToSave;

                try {
                    System.out.println("-----------------------------------------------");
                    System.out.println("Обработка протокола: " + file.getName().split(".rtf")[0]);
                    main.startBuilding();


                    if(main.isBadProtocol()){
                        System.out.println("Обработка протокола: " + file.getName().split(".rtf")[0] + " не завершена !!!!");
                        textAreaInfo.appendText(file.getName() + " ----> BAD !!!" + "\n");
                        countBadProtocols++;

                    }else{
                        System.out.println("Обработка протокола: " + file.getName().split(".rtf")[0] + " завершена =)");
                        textAreaInfo.appendText(file.getName() + " ----> OK!" + "\n");
                        countGoodProtocols++;
                    }

                } catch (Exception e) {
                    textAreaInfo.appendText(file.getName() + " ----> BAD !!!" + "\n");
                    countBadProtocols++;
                    System.out.println("Ошибка обрабоки протокола");
                    e.printStackTrace();

                } finally {
                    countSelectedProtocols++;
                    main.setBadProtocol(false);
                }

            }

            textAreaInfo.appendText("-------------------------------------------------------------------------" + "\n");
            textAreaInfo.appendText("Работа программы завершена" + "\n");
            textAreaInfo.appendText("Всего протоколов: " + countSelectedProtocols + "\n");
            textAreaInfo.appendText("Завершилось успехом: " + countGoodProtocols + "\n");
            textAreaInfo.appendText("Завершилось неудачей:" + countBadProtocols + "\n");

            protocolsIsReady = true;

            long secondTime = System.currentTimeMillis();
            double time = (secondTime - firstTime);

            buttonCreateProtocols.setDisable(false);
            progressBarForProtocols.setProgress(1);

            Platform.runLater(() -> {
                LabelTime.setText("Время выполнения: " + time / 1000 + " сек.");
            });


        });


        thread.start();

    }

    public void addStendNamesInMenu(){

        //stendNamesGroup = new ToggleGroup();

        for(XSSFSheet sheet : main.getConnectingDataBase().getSheetsInDataBase()){
      //      measuringInstrumentMenu.getItems().add(new RadioMenuItem(sheet.getSheetName()));
            measuringInstrumentList.getItems().add(sheet.getSheetName());
        }

     /*   for(MenuItem menuItem : measuringInstrumentMenu.getItems()){
            stendNamesGroup.getToggles().add((Toggle) menuItem);
        }*/



    }


    public void dragAndDropEvents(){

        main.getPrimaryStage().getScene().getRoot().setOnDragOver(evt -> { //при перетаскивании файла в область программы
            evt.acceptTransferModes(TransferMode.LINK);
        });

        main.getPrimaryStage().getScene().getRoot().setOnDragDropped(evt -> { // при отпускании файла в область программы

            progressBarForProtocols.setProgress(0);

            if(protocolsList == null){
                protocolsList = new ArrayList<>();
            }

            if(protocolsIsReady){//если протоколы сформированы, можно очистить список
                protocolsIsReady = false;
                protocolsList.clear();
            }


            for(File file : evt.getDragboard().getFiles()){ //перебираем перенесенные файлы
                if(file.getName().contains(".rtf")){ //если имя файла содержит
                    if(!protocolsList.contains(file)){ //если файла нет в списке протоколов
                        protocolsList.add(file);//то добавляем его в список
                    }
                }
            }

            if(!protocolsList.isEmpty()){
                LabelPathToOpen.setText("OK");
                LabelPathToOpen.setTextFill(Color.GREEN);
                isOpen = true;
            }

            if(protocolsList.isEmpty()){
                LabelPathToOpen.setText("ОШИБКА");
                LabelPathToOpen.setTextFill(Color.RED);
                isOpen = false;
                buttonCreateProtocols.setDisable(true);
            }

            if (isOpen && isSave && DBIsOk) {
                LabelCreateProtocols.setText("OK");
                buttonCreateProtocols.setDisable(false);
                LabelCreateProtocols.setTextFill(Color.GREEN);
            }


            //System.out.println(protocolsList);
            addTextOnTextArea();


        });


    }


    private void addTextOnTextArea(){

        textAreaInfo.clear();
        for(File file : protocolsList){
            textAreaInfo.appendText(file.getName() + "\n");
        }
    }


    public void read(SaverAndReaderSettingsForm saverAndReaderSettingsForm){ //после запуска восстановить информацию из файла

        saverAndReaderSettingsForm.read(this);
    }

    public TextField getTextFieldCertificate() {
        return textFieldCertificate;
    }

    public void setPathToSave(String pathToSave) {
        this.pathToSave = pathToSave;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public String getPathToSaveForGroupProtocols() {
        return pathToSaveForGroupProtocols;
    }

    public String getPathToSave() {
        return pathToSave;
    }

    public void setWindowsIsClose(boolean windowsIsClose) {
        this.windowsIsClose = windowsIsClose;
    }

}
