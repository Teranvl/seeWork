package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Main;

public class AddStudentInSubjectController { //контроллер добавления нового студента в БД

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private Button addButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label infoLabel;

    private Main main;
    private String subjectName;//название предмета
    private boolean onGui = true;//флаг выключения обработки графического интерфейса

    @FXML
    void initialize() {
        guiThreadStart();//запускаем поток обработки гафического интерфейса

        addButton.setOnAction(ActionEvent -> {//при нажатии на кнопку добавить
            String request = "add_student_" + firstNameField.getText().trim() + "_" + lastNameField.getText().trim() + "_" + subjectName;//формируем запрос
            main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
            waitAnswerFromServer();//ожидаем ответа от сервера
        });

        closeButton.setOnAction(ActionEvent -> { // при нажатии на кнопку закрытия
            onGui = false;//завершаем работу потока графического интерфейса
            String pathToFXML = "/sample/filesFXML/TeacherMenu.fxml";
            TeacherController teacherController = main.openNewWindow(closeButton, pathToFXML, "teacher_menu").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            teacherController.setMain(main);//передаем ссылку на main класс
        });

    }

    private void guiThreadStart(){

        Thread thread = new Thread(() ->{//создаем новый поток
            while(onGui){

                if(firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty()){//если поля ввода пароля или логина пустые
                    addButton.setDisable(true);//выключаем кнопку
                }else {
                    addButton.setDisable(false);
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
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("student_add_ok")){//если дисциплина создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    infoLabel.setText(firstNameField.getText() + " " + lastNameField.getText() + ", добавлен к дисциплине " + subjectName);

                    break; //выходим из цикла ожидания
                }
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("student_add_bad")){//если дисциплина не создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();
                    infoLabel.setText(firstNameField.getText() + " " + lastNameField.getText() + ", НЕ добавлен к дисциплине " + subjectName);

                    break;//выходим из цикла ожидания
                }
                System.out.println("сообщение о ошибке добавление студента к дисциплине");
                main.getConnectToServer().getInputConnect().disableAnswerToReady();
                break;//выходим из цикла ожидания

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Ошибка в потоке ожидания ответа создания дисциплины от сервера");
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
