package mainPackage;

import buildCertificate.ProtocolSaveForCertificate;
import dataBase.ConnectingDataBase;
import dataBase.ReadDataBase;
import protocol.*;
import java.util.ArrayList;

public class DistributionChannelsToProtocols {

    private ParserRTF parserRTF;
    private ReadDataBase readDataBase;
    private ConnectingDataBase connectingDataBase;
    private String thePathToTheFile; // путь к файлу
    private Main main;

    public DistributionChannelsToProtocols(ParserRTF parserRTF, ConnectingDataBase connectingDataBase ,Main main) {
        this.parserRTF = parserRTF;
        this.main = main;
        this.connectingDataBase = connectingDataBase;

        initialization();
    }

    private void initialization(){
        thePathToTheFile = main.getController().getPathToFile();
        connectingDataBase.connectToDataBaseStend();
        readDataBase = new ReadDataBase(connectingDataBase.getStendList(), parserRTF, main);
        readDataBase.read(parserRTF.getIdentifiersListFromRTF(), thePathToTheFile);
    }


    public void distribution() {

        int protocolScopeNumber = 0; // переменная, хранящая порядковый номер обрабатываемого протокола(имеется ввиду уже финальный протокол) в данный момент.
        int index = 0; // индекс, который отвечает за добавление в индивидуальную таблицу канала из общей, пересчитанной таблицы

        while (protocolScopeNumber < readDataBase.getIdentifiersFromDataBase().size()) { // пока есть каналы в протоколе // цикл по количеству найденых каналов(идентификаторов) в базе данных

            Protocol protocol = definitionTypeProtocol(protocolScopeNumber);

            if(!(protocol instanceof BadProtocol)){ //если тип протокола определен т.е. протокол не плохой

                protocol.initialize();

                protocol.setCertificate(main.getController().getCertificate()); //установка сертификата
                protocol.setThePathToSaveForGroupProtocols(main.getController().getPathToSaveForGroupProtocols());

                index = settingProtocolPointsTable(protocol, index);

                protocol.calculation();
                protocol.setFormulaAndDecryptionAndError();
            }

            protocol.print();
         //   new ProtocolSaveForCertificate().setAllFields(protocol); //для формирования сертификата
            protocolScopeNumber++;
            
        }
    }

    private int settingProtocolPointsTable(Protocol protocol, int index){ //метод заполнения таблицы ProtocolPointsTable

        //заполнение таблицы значений
        if(parserRTF.getDirectStrokeInTable() || parserRTF.getReverseMoveInTable() || parserRTF.getDeInTable()) { //если есть ненужные столбцы
            for (int i = 0; i < parserRTF.getQuantityControlPoints(); i++) { // заполнение таблицы значений для одного протокола
                protocol.getProtocolPointsTable().add(new ArrayList<>());
                for(int j =0; j < parserRTF.getValuesTable().get(index).size(); j++){

                    if(parserRTF.getDirectStrokeInTable() && parserRTF.getReverseMoveInTable() && parserRTF.getDeInTable()){

                        if(!(j == 3 || j ==4 || j == parserRTF.getValuesTable().get(index).size() - 1)){
                            protocol.getProtocolPointsTable().get(i).add(parserRTF.getValuesTable().get(index).get(j));
                        }

                    }else{

                        if(parserRTF.getDirectStrokeInTable() && parserRTF.getReverseMoveInTable()){

                            if(!(j == 3 || j ==4)){
                                protocol.getProtocolPointsTable().get(i).add(parserRTF.getValuesTable().get(index).get(j));
                            }

                        }else{

                            if(parserRTF.getDeInTable()){
                                if(j != parserRTF.getValuesTable().get(index).size() - 1){
                                    protocol.getProtocolPointsTable().get(i).add(parserRTF.getValuesTable().get(index).get(j));
                                }
                            }
                        }
                    }
                }
                index++;
            }

        }else{

            for (int i = 0; i < parserRTF.getQuantityControlPoints(); i++) { // заполнение таблицы значений для одного протокола
                protocol.getProtocolPointsTable().add(new ArrayList<>());
                for(int j =0; j < parserRTF.getValuesTable().get(index).size(); j++){
                    protocol.getProtocolPointsTable().get(i).add(parserRTF.getValuesTable().get(index).get(j));
                }

                index++;
            }
        }

        return index;
    }


    private Protocol definitionTypeProtocol(int protocolScopeNumber) {

        String unit = readDataBase.getUnits().get(protocolScopeNumber);

        if (unit.equals("кгс/см2") || unit.equals("мм вод.ст.") || unit.equals("мм рт.ст.")) {

            if (readDataBase.getHalfInnerLimit().get(protocolScopeNumber)) {

                if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("поэлементный")) {
                    return new PressureProtocolElementalThisHalfInner(readDataBase, parserRTF, protocolScopeNumber);
                }

                if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("комплектный")) {
                    return new PressureProtocolCompleteThisHalfInner(readDataBase, parserRTF, protocolScopeNumber);
                }

            } else {

                if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("поэлементный")) {
                    return new PressureProtocolElementalWithoutHalfInner(readDataBase, parserRTF, protocolScopeNumber);
                }

                if (readDataBase.getMethodOfRationing().get(protocolScopeNumber).equals("комплектный")) {
                    return new PressureProtocolCompleteWithoutHalfInner(readDataBase, parserRTF, protocolScopeNumber);
                }
            }
        }

        if (unit.equals("°C") || unit.equals("°С")) {

            if (thePathToTheFile.contains("_ХК.rtf") || thePathToTheFile.contains("_ХА.rtf")) {

                if (thePathToTheFile.contains("_ХА.rtf")) {
                    return new TemperatureProtocol_XA(readDataBase, parserRTF, protocolScopeNumber);
                }

                if (thePathToTheFile.contains("_ХК.rtf")) {
                    return new TemperatureProtocol_XK(readDataBase, parserRTF, protocolScopeNumber);
                }

            } else {

                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TXA.getStringSensor())) {
                    System.out.println("Это протокол БС?");
                    return new TemperatureProtocol_XA(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TXK.getStringSensor())) {
                    System.out.println("Это протокол БС?");
                    return new TemperatureProtocol_XK(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TC1388.getStringSensor())) {
                    return new TemperatureProtocol_TC1388(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.P109.getStringSensor())) {
                    return new TemperatureProtocol_P109(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.P77.getStringSensor())) {
                    return new TemperatureProtocol_P77(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.CHPT.getStringSensor())) {
                    return new TemperatureProtocol_CHPT(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TCP0313.getStringSensor())) {
                    return new TemperatureProtocol_TCP0313(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TCP0131.getStringSensor())) {
                    return new TemperatureProtocol_TCP0131(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TC1288.getStringSensor())) {
                    return new TemperatureProtocol_TC1288(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TCPY0104.getStringSensor())) {
                    return new TemperatureProtocol_TCPY0104(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.TR10C.getStringSensor())) {
                    return new TemperatureProtocol_TR10C(readDataBase, parserRTF, protocolScopeNumber);
                }
                if (readDataBase.getTypeFirstConverter().get(0).contains(TemperatureSensors.P148.getStringSensor())) {
                    return new TemperatureProtocol_P148(readDataBase, parserRTF, protocolScopeNumber);
                }
            }
        }

        main.setBadProtocol(true);
        return new BadProtocol(readDataBase, parserRTF, protocolScopeNumber);
    }

}