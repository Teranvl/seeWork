package sample.controllers;

import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import sample.Main;
import sample.structure.Student;
import sample.structure.StudentControlPoints;

public class SetControlPointsController { //контроллер установки контрольных точек для студентов

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, String> idColumn;

    @FXML
    private TableColumn<Student, String> firstNameColumn;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, List<Integer>> controlPointsColumn;

    @FXML
    private TableView<StudentControlPoints> tableViewControlPoints;

    @FXML
    private TableColumn<StudentControlPoints, Integer> controlPointNumberColumn;

    @FXML
    private TableColumn<StudentControlPoints, Integer> controlPointValueColumn;

    @FXML
    private Button sendButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label infoLabel;

    private ObservableList<Student> observableListThisStudents;//специальный контейнер для отображение информации в таблице в окне программы
    private ObservableList<StudentControlPoints> studentControlPoints;//специальный контейнер для отображение информации в таблице в окне программы
    private Map<String, List<Integer>> studentsControlPointsForSendToServer;//структура контрольных точек и студентов, для отправления на сервер
    private Map<String, List<Integer>> studentsControlPointsLoadFromServer;//структура контрольных точек и студентов, полученная от сервера
    private Main main;
    private String subjectName;//название предмета

    @FXML
    void initialize() {

        tableViewControlPoints.setEditable(true);//разрешаем редактирование таблицы с контрольными точками
        controlPointValueColumn.setEditable(true); //разрешаем редактирование ячеек значения контрольной точки

        //устанавливаем фабрику для столбца значений контрольных точек:
        controlPointValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return String.valueOf(object);
            }

            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }
        }));

        controlPointValueColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudentControlPoints, Integer>>() {//собыытие при редактировании значений контрольных точек
            @Override
            public void handle(TableColumn.CellEditEvent<StudentControlPoints, Integer> event) {
                int tmpPointNumber = event.getTableView().getFocusModel().getFocusedItem().getNumber() - 1;//получаем номер контрольной точки
                int tmpPointValue = event.getNewValue();//получаем ее значение

                tableView.getFocusModel().getFocusedItem().getControlPoints().remove(tmpPointNumber);//удаляем из основной таблицы старое значение контрольной точки
                tableView.getFocusModel().getFocusedItem().getControlPoints().add(tmpPointNumber, tmpPointValue);//устанавливаем новое

                tableView.refresh();//обновляем основную таблицу

            }
        });

        sendButton.setOnAction(ActionEvent -> {//при нажатии кнопки записать в БД

            for(Student student : tableView.getItems()){ //цикл по записям в таблице
                String tmpKey = student.getFirstName() + "_" + student.getLastName() + "_" + student.getId();
                studentsControlPointsForSendToServer.put(tmpKey, student.getControlPoints());//добавляем в структуру отпраки на сервер всех студентов с контрольными точками
            }
            studentsControlPointsForSendToServer.put("subject_name_" + subjectName, null);//добавляем предмет в структуру

            main.getConnectToServer().getOutputConnect().sendRequestToServer(studentsControlPointsForSendToServer);//устанавливаем запрос для сервера
            waitAnswerFromServer();//ожидаем ответа от сервера

        });

        closeButton.setOnAction(ActionEvent -> { // при нажатии кнопки закрытия
            String pathToFXML = "/sample/filesFXML/TeacherMenu.fxml";
            TeacherController teacherController = main.openNewWindow(closeButton, pathToFXML, "Teacher Menu").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            teacherController.setMain(main);//передаем ссылку на main класс
        });

        tableView.setOnMouseClicked(ActionEvent -> {//при выделении строки в таблице

            studentControlPoints = FXCollections.observableArrayList();//выделяем память под структруру данных

            List<Integer> points = tableView.getFocusModel().getFocusedItem().getControlPoints();//создаем спиок из контрольных точек

            for(int i = 0; i < points.size(); i++){ //цикл по контрольным точкам
                studentControlPoints.add(new StudentControlPoints(i + 1, points.get(i)));//добавляем контрольные точки в стуктуру
            }
            //устанавливаем фабрики для столбуов таблицы:
            controlPointNumberColumn.setCellValueFactory(new PropertyValueFactory<StudentControlPoints, Integer>("number"));
            controlPointValueColumn.setCellValueFactory(new PropertyValueFactory<StudentControlPoints, Integer>("value"));

            tableViewControlPoints.setItems(studentControlPoints);//загружаем данные в таблицу

        });
    }

    public void setStructure(){//установка структуры
        studentsControlPointsForSendToServer = new HashMap<>();//выделяем память для структуры контрольных точек и студентов, для отправления на сервер
        studentsControlPointsLoadFromServer = main.getConnectToServer().getInputConnect().getMapAnswerFromServer(); //получаем ответ от сервера

        main.getConnectToServer().getInputConnect().clearAnswer();//очищаем ответ от сервера
        observableListThisStudents = FXCollections.observableArrayList();//выделяем память под структруру данных

        for(Map.Entry<String, List<Integer>> entry : studentsControlPointsLoadFromServer.entrySet()){//цикл по записям в структуре
            observableListThisStudents.add(new Student(entry.getKey().split("_")[2],
                                                       entry.getKey().split("_")[0],
                                                       entry.getKey().split("_")[1],
                                                       entry.getValue()));// добавлям все полученные записи студентов с сервера
        }
        //устанавливаем фабрики для столбуов таблицы:
        idColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        controlPointsColumn.setCellValueFactory(new PropertyValueFactory<Student, List<Integer>>("controlPoints"));

        tableView.setItems(observableListThisStudents);//загружаем данные в таблицу

    }

    private void waitAnswerFromServer(){ //ожидание ответа от сервера

        while (true){
            if(main.getConnectToServer().getInputConnect().isAnswerIsReady()){ //если ответ от сервера получен
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("control_points_ok")){//если дисциплина создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    sendButton.setDisable(true);
                    infoLabel.setText("Изменения успешно внесены в БД");

                    break; //выходим из цикла ожидания
                }
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("control_points_bad")){//если дисциплина не создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();
                    sendButton.setDisable(true);
                    infoLabel.setText("При внесении изменений в БД произошла ошибка");

                    break;
                }
                System.out.println("сообщение о ошибке при записи контрольных точек в БД");
                main.getConnectToServer().getInputConnect().disableAnswerToReady();
                break;

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке ожидания ответа ри записи контрольных точек в БД");
                e.printStackTrace();
            }
        }
    }



    public void setMain(Main main){
        this.main = main;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
