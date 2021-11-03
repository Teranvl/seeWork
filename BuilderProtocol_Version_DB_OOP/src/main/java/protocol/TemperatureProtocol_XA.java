package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_XA extends TemperatureThermocoupleProtocol{

    public TemperatureProtocol_XA(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDmppForTemperatureFirstClass_XA(getStandard(i)));
            protocolPointsTable.get(i).add(getDmxc());
            protocolPointsTable.get(i).add(calculation.calculationDmcymForTemperatureFirstClass_XA(getDm(i), calculation.getDmpp(), getDmxc()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));
        }
    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным согласно формулам Δ = 0,004·|t| при t > 375 °C,  Δ = 1,5 при  -40 < t < 375 °C";
    }
}
