package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public abstract class ThermoResistanceProtocol extends TemperatureProtocol{

    protected String decryptionDmppi; // в методе decryptionReductionsInFormula погрешность определяется по формулам, определенных для каждого термосопротивления

    public ThermoResistanceProtocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void setFormulaAndDecryptionAndError() {

        setDecryptionDmppi();
        double Dmi = protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod());
        double Dmppi = protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod());
        double Xdiap = parserRTF.getXdiap();
        double Decym = protocolPointsTable.get(0).get(9 + parserRTF.getCountDownMod());

        formula = "Dе=max{[1.1•√ (Dmi^2 + Dmппi^2)/|Хдиап|] • 100%} = ";

        formulaThisValues = String.format("\n= {[1.1•√ (%1.4f^2 + %1.4f^2) / |%1.4f|] • 100%%} = %1.4f %%", Dmi, Dmppi, Xdiap, Decym);

        decryptionReductionsInFormula = decryptionDmppi + unitsInThisProtocol + "; " +
                "Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";

        firstError = String.format("Погрешность на всем диапазоне (Dmсум): %1.4f " + unitsInThisProtocol + ";", getMaxDmcym());
        secondError = String.format("Приведенная погрешность (Dесум): %1.4f %%", getMaxDecym());

        checkingOneError(getMaxDecym());

    }

    public abstract void setDecryptionDmppi();

}
