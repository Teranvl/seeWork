package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;

public class SampleController {//контроллер начального окна

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button buttonEnter;

    @FXML
    private RadioButton radioTeacher;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField loginField;

    @FXML
    private RadioButton radioStudent;

    @FXML
    private Label errorLabel;

    private boolean isTeacher = false;//флаг учителя
    private boolean isStudent = false;//флаг студента
    private Main main;
    private boolean offGui = true;//флаг выключения обработки графического интерфейса

    @FXML
    void initialize() {

        guiThreadStart();//запускаем поток обработки гафического интерфейса

        buttonEnter.setDisable(true); //выключаем кнопку входа


        radioStudent.setOnAction(actionEvent -> { //при выборе радио-кнопки студент
            radioStudent.setSelected(true);
            radioTeacher.setSelected(false);
            isStudent = true;
            isTeacher = false;
        });

        radioTeacher.setOnAction(actionEvent -> {//при выборе радио-кнопки преподавателя
            radioTeacher.setSelected(true);
            radioStudent.setSelected(false);
            isStudent = false;
            isTeacher = true;
        });


        buttonEnter.setOnAction((actionEvent) -> { //при нажатии на кнопку войти

            String request; //переменнная для запроса

            if(isTeacher){//если учитель
                request = "login_teacher_" + loginField.getText() + "_" + passwordField.getText();//формируем запрос
                main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
            }

            if(isStudent){//если студент
                request = "login_student_" + loginField.getText() + "_" + passwordField.getText();//формируем запрос
                main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
            }
            waitAnswerFromServer();//ждем ответ сервера

        });
    }

    private void guiThreadStart(){
        Thread thread = new Thread(() ->{//создаем новый поток
            while (offGui){
                //если выбран учитель или учение и поля для ввода логина и пароля не пустые
                if(((radioStudent.isSelected() || radioTeacher.isSelected()) && !loginField.getText().isEmpty() && !passwordField.getText().isEmpty())){
                    buttonEnter.setDisable(false);//включаем кнопку
                }else {
                    buttonEnter.setDisable(true);
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
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("teacher_authorization_ok")){//если авторизация преподавателя прошла успешно
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    System.out.println("Открываем новое окно преподавателя");
                    offGui = false;//завершаем работу потока графического интерфейса

                    String pathToFXML = "/sample/filesFXML/TeacherMenu.fxml";
                    TeacherController teacherController = main.openNewWindow(buttonEnter, pathToFXML, "Teacher Menu").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
                    teacherController.setMain(main);//передаем ссылку на main класс

                    break; //выходим из цикла ожидания
                }

                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("student_authorization_ok")){//если авторизация студента прошла успешно
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    System.out.println("Открываем новое окно студента");
                    offGui = false;//завершаем работу потока графического интерфейса

                    String pathToFXML = "/sample/filesFXML/StudentsStatus.fxml";

                    StudentsStatusController studentsStatusController = main.openNewWindow(buttonEnter, pathToFXML, "Students Status").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
                    studentsStatusController.setMain(main);//передаем ссылку на main класс

                    break;//выходим из цикла ожидания
                }
                System.out.println("сообщение о ошибке авторизации");
                errorLabel.setText("Ошибка авторизации");

                main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                break;

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке ожидания ответа авторизации от сервера");
                e.printStackTrace();
            }


        }

    }



    public void setMain(Main main){
        this.main = main;
    }


}
