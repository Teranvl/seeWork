package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public class TemperatureProtocol_CHPT extends ThermoResistanceProtocol {

    public TemperatureProtocol_CHPT(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void calculation() {
        for (int i = 0; i < protocolPointsTable.size(); i++) { //вычисляем необходимые столбцы и добавляем в конец таблицы
            protocolPointsTable.get(i).add(calculation.calculationDmppForTemperatureCHPT(getStandard(i)));
            protocolPointsTable.get(i).add(calculation.calculationDmcym(getDm(i), calculation.getDmpp()));
            protocolPointsTable.get(i).add(calculation.calculationDrcymForTemperatureCHPT(getStandard(i), calculation.getDmcym()));
        }
    }

    @Override
    public void setFormulaAndDecryptionAndError() {

        setDecryptionDmppi();
        double standard = protocolPointsTable.get(0).get(1);
        double Dmi = protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod());
        double Dmppi = protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod());
        double Drcymi = protocolPointsTable.get(0).get(9 + parserRTF.getCountDownMod());

        formula = "Drcyм = ([1.1•√(Dmi^2 + Dmппi^2)] * 100) / (Xi + 273.15) = ";

        formulaThisValues = String.format("\n= {([1.1•√(%1.4f ^2 + %1.4f^2)] * 100) / (%1.4f + 273.15)} = %1.4f %%", Dmi, Dmppi, standard, Drcymi);

        decryptionReductionsInFormula = decryptionDmppi + unitsInThisProtocol + "; " +
                "Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хi – значение в i-той точке, " + unitsInThisProtocol + "; ";

        firstError = String.format("Погрешность на всем диапазоне (Dmсум): %1.4f " + unitsInThisProtocol + ";", getMaxDmcym());
        secondError = String.format("Относительная погрешность (Drсум): %1.4f %%", getMaxDrcym());

        checkingOneError(getMaxDrcym());

    }

    @Override
    public void setDecryptionDmppi() {
        decryptionDmppi = "где Dmппi – погрешность первичного преобразователя в i-той точке (определяется по паспортным данным согласно формулам Δ = (0,15+0,002·|t|)), ";
    }

}
