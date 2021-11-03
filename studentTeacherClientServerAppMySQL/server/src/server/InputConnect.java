package server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputConnect implements Runnable{

    private Socket socket;
    private String inputCommand;//принятая команда
    private Object inputObject;//объект, получаемый от клиента
    private WriteAndReaderDataBase writeAndReaderDataBase;//класс работы с БД
    private ServerCreate serverCreate;
    private ObjectInputStream objectInputStream;//входной поток объектов
    private InputStream inputStream;//входной поток

    public InputConnect(Socket socket, WriteAndReaderDataBase writeAndReaderDataBase, ServerCreate serverCreate) {//конструктор
        this.socket = socket;
        this.writeAndReaderDataBase = writeAndReaderDataBase;
        this.serverCreate = serverCreate;
    }

    @Override
    public void run() {//запуск потока
        System.out.println("Поток чтения конманд запущен!");
        readClientCommand();
    }

    private void readClientCommand(){ //метод чтения запросов от клиента
        try {
            inputStream = socket.getInputStream(); //открываем входящий поток
            objectInputStream = new ObjectInputStream(inputStream);//входящий поток объектов

            while(true){
                inputObject = objectInputStream.readObject();//ожидание команды от клиента

                if(inputObject.getClass().equals(String.class)) {//если запрос от сервера является строкой
                    inputCommand = (String) inputObject;

                    if (inputCommand.contains("login_teacher")) { //команда на авторизацию преподавателя
                        System.out.println("Авторизация преподавателя");
                        String[] tmpCommandMass = inputCommand.split("_");
                        String tmpLogin = tmpCommandMass[2]; //вытаскиваем из команды логин
                        String tmpPassword = tmpCommandMass[3];//вытаскиваем из команды пароля

                        if (writeAndReaderDataBase.authorisationTeacher(tmpLogin, tmpPassword)) { //если в БД найдено совпадение логин/пароль
                            System.out.println("Авторизация преподавателя прошла успешно");
                            String answer = "teacher_authorization_ok";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        } else {
                            System.out.println("Авторизация преподавателя завершилась неудачей");
                            String answer = "teacher_authorization_bad";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        }

                        serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку
                    }

                    if (inputCommand.contains("login_student")) { //команда на авторизацию студента
                        System.out.println("Авторизация студента");
                        String[] tmpCommandMass = inputCommand.split("_");
                        String tmpLogin = tmpCommandMass[2];//вытаскиваем из команды логин
                        String tmpPassword = tmpCommandMass[3];//вытаскиваем из команды пароля

                        if (writeAndReaderDataBase.authorisationStudent(tmpLogin, tmpPassword)) {
                            System.out.println("Авторизация студента прошла успешно");
                            String answer = "student_authorization_ok";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        } else {
                            System.out.println("Авторизация студента завершилась неудачей");
                            String answer = "student_authorization_bad";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        }
                        serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку
                    }

                    if (inputCommand.contains("add_subject")) { //команда добавления нового предмета
                        System.out.println("Добавление предмета в БД");
                        String[] tmpSubjectMass = inputCommand.split("_");
                        String tmpSubjectName = tmpSubjectMass[2];
                        int tmpCountControlPoints = Integer.parseInt(tmpSubjectMass[3]);

                        if (writeAndReaderDataBase.createSubjectInDataBase(tmpSubjectName, tmpCountControlPoints)) {
                            String answer = "subject_create_ok";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        } else {
                            String answer = "subject_create_bad";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        }

                        serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку

                    }

                    if (inputCommand.contains("add_student")) {//команда добавления студента к дисциплине
                        System.out.println("Добавление студента к дисциплине");
                        String[] tmpStudentInfo = inputCommand.split("_");
                        String tmpFirstName = tmpStudentInfo[2];
                        String tmpLastName = tmpStudentInfo[3];
                        String tmpSubjectName = tmpStudentInfo[4];

                        if (writeAndReaderDataBase.addStudentToSubject(tmpFirstName, tmpLastName, tmpSubjectName)) {
                            String answer = "student_add_ok";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        } else {
                            String answer = "student_add_bad";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        }

                        serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку
                    }

                    if(inputCommand.contains("get_control_points")){ //запрос контрольных точек по дисциплине
                        System.out.println("Запрос контрольных точек дисциплины");
                        String tmpSubjectName = inputCommand.split("_")[3];
                        Map<String, List<Integer>> answerMap;

                        if((answerMap = writeAndReaderDataBase.getStudentsControlPoints(tmpSubjectName)) != null){
                            serverCreate.getOutput().setObjectToSend(answerMap);//подготавливаем ответ к отправке на клиент
                        }else{
                            String answer = "control_points_bad";
                            serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                        }

                        serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку
                    }
                }

                if(inputObject.getClass().equals(HashMap.class)){//если запрос является структорой HashMap
                    System.out.println("запрос от клинта отпределен как структура Map");
                    Map<String, List<Integer>> inputMap = (HashMap) inputObject;
                    String tmpSubjectName = "";

                    for(String tmp : inputMap.keySet()){
                        if(tmp.contains("subject_name_")){
                            tmpSubjectName = tmp.split("_")[2];//ищем по ключу в структуре название дисциплины
                        }
                    }

                    inputMap.remove("subject_name_" + tmpSubjectName);//удаляем значение по ключу из структуры

                    if(writeAndReaderDataBase.setStudentsPointsTable(inputMap, tmpSubjectName)){
                        String answer = "control_points_ok";
                        serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                    }else{
                        String answer = "control_points_bad";
                        serverCreate.getOutput().setObjectToSend(answer);//подготавливаем ответ к отправке на клиент
                    }

                    serverCreate.getOutput().setReadyToSend(true);//разрешаем отправку

                }

            }

        }
        catch (IOException | ClassNotFoundException e ) {
            System.out.println("Произошло отключение клиента от сервера");
        }finally {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                System.out.println("Ошибка закрытия подключения для клиента");
                e.printStackTrace();
            }
        }
    }

}
