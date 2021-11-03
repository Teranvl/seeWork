package mainPackage;

import controllers.Controller;

import java.io.*;


public class SaverAndReaderSettingsForm {
//класс для сохранения и восстановления состояния главное формы программы
    private boolean readFileIsOk = false;
    private Main main;

    public SaverAndReaderSettingsForm(Main main){
        this.main = main;
    }

    public void save(Controller controller){

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\BuilderProtocol\\form_setting\\form_setting.dat");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(controller.getTextFieldCertificate().getText());
            objectOutputStream.writeObject(controller.getPathToSave());

            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void read(Controller controller){
        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\BuilderProtocol\\form_setting\\form_setting.dat");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            controller.getTextFieldCertificate().setText((String) objectInputStream.readObject());
            controller.setPathToSave((String) objectInputStream.readObject());

            objectInputStream.close();
            readFileIsOk = true;
            System.out.println("Настройки загружены!");
            main.stopThreadForPrintPoints();


        } catch (FileNotFoundException | EOFException e) {
            readFileIsOk = false;
            System.out.println("Файл настроек не наден.");
        } catch (IOException | ClassNotFoundException e) {
            readFileIsOk = false;
            e.printStackTrace();
        }


    }

    public boolean isReadFileIsOk() {
        return readFileIsOk;
    }

}
