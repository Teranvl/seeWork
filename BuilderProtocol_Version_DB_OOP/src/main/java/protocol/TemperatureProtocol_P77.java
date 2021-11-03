package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_P77 extends ThermoResistanceProtocol {

    public TemperatureProtocol_P77(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы

            protocolPointsTable.get(i).add(calculation.calculationDmppForTemperatureP77(getStandard(i)));
            protocolPointsTable.get(i).add(calculation.calculationDmcym(getDm(i), calculation.getDmpp()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));
        }

    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным согласно формулам Δ = (0,3+0,0045·|t|) при t > 0 °C,  Δ = (0,3+0,006·|t|) при t < 0 °C), ";
    }
}
