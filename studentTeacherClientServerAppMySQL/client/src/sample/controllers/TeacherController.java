package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Main;

public class TeacherController {//контроллер преподавателя

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonCreateSubject;

    @FXML
    private Button buttonAddStudent;

    @FXML
    private Button buttonPoints;

    @FXML
    private TextField subjectNameField;

    @FXML
    Label labelInfo;

    private Main main;
    private boolean onGui = true; //флаг выключения обработки графического интерфейса
    private boolean goodAnswer = true; //флаг правильного ответа от сервера

    @FXML
    void initialize() {
        guiThreadStart();//запускаем поток обработки гафического интерфейса

        buttonCreateSubject.setOnAction(ActionEvent -> {//нажатие на кнопку добавление предмета в БД
            onGui = false;//завершаем работу потока графического интерфейса
            String pathToFXML = "/sample/filesFXML/CreateSubject.fxml";
            SubjectController subjectController = main.openNewWindow(buttonCreateSubject, pathToFXML, "Create Subject").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            subjectController.setMain(main);//передаем ссылку на main класс
            subjectController.setSubjectName(subjectNameField.getText().trim()); //устанавливаем название дисциплины
        });

        buttonAddStudent.setOnAction(ActionEvent -> {//нажатие на кнопку добавление студента
            onGui = false;//завершаем работу потока графического интерфейса
            String pathToFXML = "/sample/filesFXML/AddStudentInSubject.fxml";
            AddStudentInSubjectController addStudentInSubjectController = main.openNewWindow(buttonAddStudent, pathToFXML, "Add Student").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            addStudentInSubjectController.setMain(main);//передаем ссылку на main класс
            addStudentInSubjectController.setSubjectName(subjectNameField.getText().trim());//устанавливаем название дисциплины
        });


        buttonPoints.setOnAction(ActionEvent -> {//нажатие на кнопку редактирование контрольных точек
            String request = "get_control_points_" + subjectNameField.getText().trim(); //формируем запрос
            main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
            waitAnswerFromServer();//ожидаем ответа от сервера

            if(goodAnswer) { //если получены от сервера контрольные точки
                onGui = false;//завершаем работу потока графического интерфейса
                String pathToFXML = "/sample/filesFXML/SetControlPoints.fxml";
                SetControlPointsController setControlPointsController = main.openNewWindow(buttonAddStudent, pathToFXML, "Control Points").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
                setControlPointsController.setMain(main);//передаем ссылку на main класс
                setControlPointsController.setSubjectName(subjectNameField.getText().trim());//устанавливаем название дисциплины
                setControlPointsController.setStructure();//вызываем метод setStructure
            }
        });
    }

    private void guiThreadStart(){

        Thread thread = new Thread(() ->{//создаем новый поток

            while(onGui) {
                if (subjectNameField.getText().isEmpty()) { //если в поле ввода нет названия
                    buttonAddStudent.setDisable(true);//выключаем кнопки
                    buttonCreateSubject.setDisable(true);//выключаем кнопки
                    buttonPoints.setDisable(true);//выключаем кнопки
                } else { // и наоборот
                    buttonAddStudent.setDisable(false);
                    buttonCreateSubject.setDisable(false);
                    buttonPoints.setDisable(false);
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



    private void waitAnswerFromServer(){ //ожидание ответа от сервера

        while (true){
            if(main.getConnectToServer().getInputConnect().isAnswerIsReady()){ //если ответ от сервера получен

                if(main.getConnectToServer().getInputConnect().getMapAnswerFromServer() != null){//если получена структура контрольных точек
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    goodAnswer = true;
                    break; //выходим из цикла ожидания
                }
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("control_points_bad")){//если контрольные точки не получены
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    labelInfo.setText("Произошла ошибка при выполнении запроса в БД");
                    goodAnswer = false;
                    break;//выходим из цикла ожидания
                }

                System.out.println("сообщение о ошибке при получении контрольных точек из БД");
                main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                break;//выходим из цикла ожидания
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке ожидания ответа при получении контрольных точек из БД");
                e.printStackTrace();
            }
        }
    }

    public void setMain(Main main){
        this.main = main;
    }




}
