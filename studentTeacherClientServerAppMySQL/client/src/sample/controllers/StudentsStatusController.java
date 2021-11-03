package sample.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.structure.Student;

public class StudentsStatusController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, String> idColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, Double> totalScoreColumn;

    @FXML
    private TableColumn<Student, List<Integer>> controlPointsColumn;

    @FXML
    private Button sendButton;

    @FXML
    private TextField subjectNameField;

    @FXML
    private Label labelInfo;

    private Main main;
    private String subjectName;//название предмета
    private boolean onGui = true;//флаг выключения обработки графического интерфейса
    private boolean goodAnswer = true; //флаг правильного ответа от сервера

    private Map<String, List<Integer>> studentsControlPointsLoadFromServer;//структура контрольных точек и студентов, полученная от сервера
    private ObservableList<Student> observableListThisStudents;//специальный контейнер для отображение информации в таблице в окне программы

    @FXML
    void initialize() {
        guiThreadStart();//запускаем поток обработки гафического интерфейса

        closeButton.setOnAction(ActionEvent -> {
            onGui = false;//завершаем работу потока графического интерфейса
            String pathToFXML = "/sample/filesFXML/sample.fxml";
            SampleController sampleController = main.openNewWindow(closeButton, pathToFXML, "Main Window").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            sampleController.setMain(main);//передаем ссылку на main класс
        });

        sendButton.setOnAction(ActionEvent -> {//при нажатии на кнопку отправки
            subjectName = subjectNameField.getText().trim();//получаем название дисциплины
            tableView.setItems(null);//сбрасыаем таблицу
            setStructure();//устанавливаем структуру таблицы
        });

    }

    private void guiThreadStart(){

        Thread thread = new Thread(() ->{//создаем новый поток
           while(onGui){
                if(subjectNameField.getText().isEmpty()){//если поле ввода пустое
                    sendButton.setDisable(true);//выключаем кнопку
                }else { //и наоборот
                    sendButton.setDisable(false);
                }
               try {
                   Thread.sleep(100);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });

        thread.start();

    }

    private void setStructure(){//метод устанвки структуры таблицы

        String request = "get_control_points_" + subjectName; //формируем запрос

        main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
        waitAnswerFromServer();//ожидаем ответа от сервера

        if(goodAnswer){ //если получен правильный ответ
            studentsControlPointsLoadFromServer = main.getConnectToServer().getInputConnect().getMapAnswerFromServer();//получаем структуру
            main.getConnectToServer().getInputConnect().clearAnswer();//очищаем ответ от сервера
            observableListThisStudents = FXCollections.observableArrayList();//выделяем память под структруру данных

            for(Map.Entry<String, List<Integer>> entry : studentsControlPointsLoadFromServer.entrySet()){ //цикл по полученным данным с сервера
                observableListThisStudents.add(new Student(entry.getKey().split("_")[2],
                                                           entry.getKey().split("_")[0],
                                                           entry.getKey().split("_")[1],
                                                           entry.getValue())); // добавлям все полученные записи студентов с сервера
            }

            //устанавливаем фабрики для столбуов таблицы:
            idColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
            controlPointsColumn.setCellValueFactory(new PropertyValueFactory<Student, List<Integer>>("controlPoints"));
            totalScoreColumn.setCellValueFactory(new PropertyValueFactory<Student, Double>("totalScore"));

            tableView.setItems(observableListThisStudents);//загружаем данные в таблицу
        }
    }

    private void waitAnswerFromServer(){ //ожидание ответа от сервера

        while (true){
            if(main.getConnectToServer().getInputConnect().isAnswerIsReady()){ //если ответ от сервера получен

                if(main.getConnectToServer().getInputConnect().getMapAnswerFromServer() != null){//если получена структура контрольных точек
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    labelInfo.setText("Данные успешно загружены из БД");
                    goodAnswer = true;
                    break; //выходим из цикла ожидания
                }

                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("control_points_bad")){//если дисциплина не создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    goodAnswer = false;
                    labelInfo.setText("При загрузке данных из БД произошла ошибка");

                    break; //выходим из цикла ожидания
                }
                System.out.println("сообщение о ошибке при получении контрольных точек из БД");
                main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                break; //выходим из цикла ожидания

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке ожидания ответа при получении контрольных точек из БД");
                e.printStackTrace();
            }
        }
    }

    public void setMain(Main main) {
        this.main = main;
    }

}
