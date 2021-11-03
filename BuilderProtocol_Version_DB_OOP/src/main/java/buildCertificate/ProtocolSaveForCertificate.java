package buildCertificate;

import protocol.Protocol;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProtocolSaveForCertificate implements Serializable {

    private String certificate; // номер сертификата
    private String nameMeasuringChannel; //наименование измерительного канала
    private String identifier; //идентификатор
    private String rangeThisUnits; //рабочий диапазон с единицами измерения
    private String firstConverter; //первичный преобразователь
    private String methodOfRationing;//метод калибровки(способ нормирования МХ)
    private String workStandard;//рабочий эталон
    private String protocolNumber; //номер протокола
    private String error; // допустимая погрешность
    private String errorHalfInnerLimit; // допустимая погрешность с 1/2
    private double errorValue; // значение допустимой погрешности
    private double errorHalfInnerLimitValue; // значение допустимой погрешности с 1/2
    private double actualErrorValue; // рассчитанное значение допустимой погрешности
    private double actualErrorHalfInnerLimitValue; // рассчитанное значение допустимой погрешности с 1/2

    public void setAllFields(Protocol protocol){
        certificate = protocol.getCertificate();
        identifier = protocol.getProtocolInformationTable().get(6);
        nameMeasuringChannel = protocol.getProtocolInformationTable().get(5);
        rangeThisUnits = protocol.getProtocolInformationTable().get(11);
        firstConverter = protocol.getProtocolInformationTable().get(7);
        methodOfRationing = protocol.getProtocolInformationTable().get(10);
        workStandard = protocol.getProtocolInformationTable().get(9);
        protocolNumber = protocol.getProtocolNumber();
        error =  protocol.getReadDataBase().getError().get(protocol.getProtocolScopeNumber());
        errorHalfInnerLimit = protocol.getReadDataBase().getErrorHalfInnerLimit().get(protocol.getProtocolScopeNumber());
        errorValue = protocol.getReadDataBase().getErrorValue().get(protocol.getProtocolScopeNumber());
        errorHalfInnerLimitValue = protocol.getReadDataBase().getErrorHalfInnerLimitValue().get(protocol.getProtocolScopeNumber());
        actualErrorValue = protocol.getActualErrorValue();
        actualErrorHalfInnerLimitValue = protocol.getActualErrorHalfInnerLimitValue();

        saveSerializableProtocol();
    }


    private void saveSerializableProtocol(){

        List<String> stringList = new ArrayList<>();
        stringList.add(certificate);
        stringList.add(nameMeasuringChannel);
        stringList.add(identifier);
        stringList.add(rangeThisUnits);
        stringList.add(firstConverter);
        stringList.add(methodOfRationing);
        stringList.add(workStandard);
        stringList.add(protocolNumber);
        stringList.add(error);
        stringList.add(errorHalfInnerLimit);

        List<Double> doubleList = new ArrayList<>();
        doubleList.add(errorValue);
        doubleList.add(errorHalfInnerLimitValue);
        doubleList.add(actualErrorValue);
        doubleList.add(actualErrorHalfInnerLimitValue);




        try {

            FileOutputStream fileOutputStream = new FileOutputStream("D:\\ser_protocols\\" + protocolNumber + ".pro");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(stringList);
            objectOutputStream.writeObject(doubleList);
            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
