package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class BadProtocol extends Protocol{


    public BadProtocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {

    }

    @Override
    public void setFormulaAndDecryptionAndError() {

    }

    @Override
    public void print(){

        System.out.println("!!!! ПРОТОКОЛ ПОМЕЧЕН КАК BAD!");
        System.out.println("!!!! ИДЕНТИФИКАТОР В БАЗЕ ДАННЫХ:" + readDataBase.getIdentifiersFromDataBase().get(protocolScopeNumber));
        System.out.println("!!!! № БАЗЕ ДАННЫХ:" + readDataBase.getProtocolNumber().get(protocolScopeNumber));
        System.out.println("!!!! ИДЕНТИФИКАТОР В СЧИТАННОМ ПРОТОКОЛЕ:" + parserRTF.getIdentifiersListFromRTF().get(readDataBase.getNumbersProtocolInRecorderProtocolMass().get(protocolScopeNumber)));//!!!!!!!!!!!!!!
        System.out.println("!!!! ЕДИНИЦЫ ИЗМЕРЕНИЯ ПРОТОКОЛА:" + readDataBase.getUnits().get(protocolScopeNumber));


    }

}
