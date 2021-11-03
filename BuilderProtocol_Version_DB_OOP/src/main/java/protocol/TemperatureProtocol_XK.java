package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_XK extends TemperatureThermocoupleProtocol {

    public TemperatureProtocol_XK(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDmppForTemperatureSecondClass_XK(getStandard(i)));
            protocolPointsTable.get(i).add(getDmxc());
            protocolPointsTable.get(i).add(calculation.calculationDmcymForTemperatureSecondClass_XK(getDm(i), calculation.getDmpp(), getDmxc()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));
        }
    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным согласно формулам Δ = (0,7+0,005·|t|) при t > 360 °C,  Δ = 2,5 при  -40 < t < 360 °C";
    }
}
