package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_P109 extends ThermoResistanceProtocol {

    public TemperatureProtocol_P109(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы

            protocolPointsTable.get(i).add(calculation.calculationDmppForTemperatureP109(getStandard(i)));
            protocolPointsTable.get(i).add(calculation.calculationDmcym(getDm(i), calculation.getDmpp()));
            protocolPointsTable.get(i).add(calculation.calculationDecym(calculation.getDmcym()));
        }

    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным согласно формулам Δ = (0,5 + 0,005·|t|)), ";
    }
}
