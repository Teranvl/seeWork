package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public abstract class PressureProtocol extends Protocol{

    public PressureProtocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

}
