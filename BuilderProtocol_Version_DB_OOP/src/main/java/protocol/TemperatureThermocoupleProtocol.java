package protocol;

import dataBase.ReadDataBase;
import mainPackage.ParserRTF;

public abstract class TemperatureThermocoupleProtocol extends TemperatureProtocol{
    //протоколы термопар
    protected String decryptionDmppi; // в методе decryptionReductionsInFormula погрешность определяется по формулам, определенных для каждой термопары

    public TemperatureThermocoupleProtocol(ReadDataBase readDataBase, ParserRTF parserRTF, int protocolScopeNumber) {
        super(readDataBase, parserRTF, protocolScopeNumber);
    }

    @Override
    public void setFormulaAndDecryptionAndError() {
        setDecryptionDmppi();

        double Dmi = protocolPointsTable.get(0).get(5 + parserRTF.getCountDownMod());
        double Dmppi = protocolPointsTable.get(0).get(7 + parserRTF.getCountDownMod());
        double Xdiap = parserRTF.getXdiap();
        double Decym = protocolPointsTable.get(0).get(10 + parserRTF.getCountDownMod());

        formula = "Dе=max{[1.1•√ (Dmi^2 + Dmппi^2 + Dmхс^2)/|Хдиап|] • 100%} = ";
        formulaThisValues = String.format("\n= {[1.1•√ (%1.4f^2 + %1.4f^2 + %1.4f^2) / |%1.4f|] • 100%%} = %1.4f %%", Dmi,
                Dmppi, getDmxc(), Xdiap, Decym);

        decryptionReductionsInFormula = decryptionDmppi + "; " +
                                     "Dmхс - оценка погрешности холодного спая, " + unitsInThisProtocol + "; " +
                                     "Dmi – погрешность ИВК в i-той точке, " + unitsInThisProtocol + "; " +
                                     "Хдиап – разница между значениями верхнего и нижнего предела измерения, " + unitsInThisProtocol + "; ";

        firstError = String.format("Погрешность на всем диапазоне (Dmсум): %1.4f " + unitsInThisProtocol + ";", getMaxDmcym());
        secondError = String.format("Приведенная погрешность (Dесум): %1.4f %%", getMaxDecym());

        checkingOneError(getMaxDecym());

    }

    public abstract void setDecryptionDmppi();


}
