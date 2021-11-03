package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.Main;

public class SubjectController {//контроллер дисциплины

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField controlPointsField;

    @FXML
    private Button createButton;

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
        closeButton.setDisable(true);//выключаем кнопку закрытия окна

        createButton.setOnAction(ActionEvent -> {//при нажатии на кнопку создания предмета
            String request = "add_subject_" + subjectName + "_" + controlPointsField.getText().trim();//формируем запрос
            main.getConnectToServer().getOutputConnect().sendRequestToServer(request);//устанавливаем запрос для сервера
            onGui = false;//завершаем работу потока графического интерфейса
            waitAnswerFromServer();//ожидаем ответа от сервера
        });

        closeButton.setOnAction(ActionEvent -> { //при нажатии на кнопку закрытия окна
            String pathToFXML = "/sample/filesFXML/TeacherMenu.fxml";
            TeacherController teacherController = main.openNewWindow(closeButton, pathToFXML, "Teacher Menu").getController();//закрываем текущее окно, открываем новое, при этом получаем объект контроллер
            teacherController.setMain(main);//передаем ссылку на main класс
        });

    }

    private void guiThreadStart(){

        Thread thread = new Thread(() ->{//создаем новый поток
            while(onGui){
                if(controlPointsField.getText().isEmpty()){ //если поле ввода пустое
                    createButton.setDisable(true);
                }else {

                    try {
                        Integer.parseInt(controlPointsField.getText());//пытаем преобразовать введенную строку в число, если удается преобразовать,
                        createButton.setDisable(false);//разрешаем создание в БД дисциплины
                    }catch (NumberFormatException e ){
                        createButton.setDisable(true);//если нет, не разрешаем
                    }
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
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("subject_create_ok")){//если дисциплина создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    closeButton.setDisable(false);//включаем кнопку
                    createButton.setDisable(true);///выключаем кнопку
                    infoLabel.setText("Дисциплина " + subjectName + " успешно добавлена в БД");

                    break; //выходим из цикла ожидания
                }
                if(main.getConnectToServer().getInputConnect().getAnswerFromServer().equals("subject_create_bad")){//если дисциплина не создана
                    main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
                    closeButton.setDisable(false);//включаем кнопку
                    createButton.setDisable(true);//выключаем кнопку
                    infoLabel.setText("При добавлении дисциплины в БД" + subjectName + " произошла ошибка");

                    break;//выходим из цикла ожидания
                }
                System.out.println("сообщение о ошибке создания дисциплины");
                main.getConnectToServer().getInputConnect().disableAnswerToReady();//снимаем готовность ответа от сервера
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
