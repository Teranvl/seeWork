package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public abstract class TemperatureProtocol extends Protocol{

    private double dmxc = 0.5; //оценка погрешности холодного спая


    public TemperatureProtocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    public double getDmxc() {
        return dmxc;
    }

}
