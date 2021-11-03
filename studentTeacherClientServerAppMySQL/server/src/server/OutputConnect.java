package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class OutputConnect implements Runnable{

    private Socket socket;
    private ObjectOutputStream objectOutputStream;//выходной поток
    private Object objectToSend; //объект для отпрваки
    private boolean readyToSend = false;//готовность отпраки

    public OutputConnect(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {//запуск потока
        System.out.println("Поток отправки информации на клиент запущен!");
        sentToClient();
    }

    private void sentToClient(){
        try {
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); //получаем исходящий поток

            while(true){
                if(readyToSend){ //если есть готовность
                    objectOutputStream.writeObject(objectToSend);//записываем объект для отправки
                    objectOutputStream.flush();//отправляем
                    readyToSend = false;//снимаем разрешение
                }
                Thread.sleep(100);
            }


        } catch (IOException | InterruptedException e) {
            System.out.println("Произошло отключение клиента от сервера");
        }finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                System.out.println("Ошибка закрытия подключения для клиента");
                e.printStackTrace();
            }
        }
    }

    public void setObjectToSend(Object objectToSend){
        this.objectToSend = objectToSend;
    }

    public void setReadyToSend(boolean readyToSend) {
        this.readyToSend = readyToSend;
    }
}
