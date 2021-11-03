package calculation;

import mainPackage.ParserRTF;
import protocol.Protocol;

public class Calculation {

    private Protocol protocol;
   // private int protocolScopeNumber;
    private ParserRTF parserRTF ;

    private double drcym;
    private double dmpp;
    private double dmcym;
    private double decym;
    private double drcym12VP;
    private double decym12VP;
    private double valueHalfInnerLimit;
    private double drcym12VPCompleteMethod;
    private double decym12VPCompleteMethod;
    private double decymCompleteMethod;


    public Calculation(Protocol protocol) {
        this.protocol = protocol;
      //  this.protocolScopeNumber = protocol.getProtocolScopeNumber();
        this.parserRTF = protocol.getParserRTF();
        valueHalfInnerLimit = Math.abs(protocol.getRealValueHalfInnerLimit());
    }


    public double calculationDmppForTemperatureP148(double standard){
        //П-148 Δ = (0,3 + 0,005 * |t|)
        dmpp = 0.3 + (0.005 * Math.abs(standard));
        return dmpp;
    }


    public double calculationDmppForTemperatureP109(double standard){
        //П-109 Δ = (0,5 + 0,005 * |t|)
        dmpp = 0.5 + (0.005 * Math.abs(standard));
        return dmpp;
    }


    public double calculationDmppForTemperatureTC1388AndTR10C(double standard){
        //ТС1388/6/100П   Δ = (0,15+0,002·|t|)
        dmpp = 0.15 + (0.002 * Math.abs(standard));
        return dmpp;
    }

    public double calculationDmppForTemperatureP77(double standard){
        //П-77   Δ = (0,3+0,0045·|t|) при t > 0 °C,  Δ = (0,3+0,006·|t|) при t < 0 °C
        if(standard < 0){

            dmpp = 0.3 + (0.006 * Math.abs(standard));

        }else{

            dmpp = 0.3 + (0.0045 * Math.abs(standard));

        }

        return dmpp;
    }


    public double calculationDmppForTemperatureCHPT(double standard){
        // ЧЭПТ-30 в К (Кельвинах)   Δ = (0,15+0,002·|t|)
        dmpp = 0.15 + (0.002 * Math.abs(standard));
        return dmpp;
    }

    public double calculationDrcymForTemperatureCHPT(double standard, double dmcym){

        drcym = dmcym * 100 / (standard + 273.15);

        if ( drcym > protocol.getMaxDrcym() ){
            protocol.setMaxDrcym(drcym);

        }

        return drcym;

    }

    public double calculationDecymForTemperatureCHPT(double dmcym){

        decym = (dmcym * 100) / (((Math.abs(parserRTF.getMaxOnRange() - parserRTF.getMinOnRange()))/2) + 273.15);

        if(decym > protocol.getMaxDecym()) //для вывода максимальной погрешности в протоколе выбираем самую большую погрешность
            protocol.setMaxDecym(decym);

        return decym;

    }

    public double calculationDmppForTemperatureTCP0313(double standard){
        //Платиновые, медные Класс В   Δ = (0,3+0,005·|t|)
        dmpp = 0.3 + (0.005 * Math.abs(standard));
        return dmpp;
    }

    public double calculationDmppForTemperatureTCP0131(double standard){
        //Платиновые, медные Класс В   Δ = (0,3+0,005·|t|)
        dmpp = 0.3 + (0.005 * Math.abs(standard));
        return dmpp;
    }

    public double calculationDmppForTemperatureTC1288(double standard){
        //Платиновые, медные Класс В   Δ = (0,3+0,005·|t|)
        dmpp = 0.3 + (0.005 * Math.abs(standard));
        return dmpp;
    }

    public double calculationDmppForTemperatureFirstClass_XA(double standard){
        //взяты значения для 1 класса храмель-алюмель

        if(standard < 375){
            dmpp = 1.5;
        }else{
            dmpp = Math.abs(standard) * 0.004;
        }

        return dmpp;

    }

    public double calculationDmcymForTemperatureFirstClass_XA(double dm, double dmpp, double dmxc){
        //взяты значения для 1 класса храмель-алюмель

        dmcym = 1.1 * Math.sqrt(Math.pow(dm, 2) + Math.pow(dmpp, 2) + Math.pow(dmxc, 2));

        if(dmcym > protocol.getMaxDmcym()) //для вывода максимальной погрешности в протоколе выбираем самую большую погрешность
            protocol.setMaxDmcym(dmcym);

        return dmcym;

    }

    public double calculationDmppForTemperatureSecondClass_XK(double standard){
        //взяты значения для 2 класса храмель-копель

        if(standard < 360){
            dmpp = 2.5;
        }else{
            dmpp = 0.7 + (Math.abs(standard) * 0.005);
        }

        return dmpp;

    }

    public double calculationDmcymForTemperatureSecondClass_XK(double dm, double dmpp, double dmxc){
        //взяты значения для 2 класса храмель-копель

        dmcym = 1.1 * Math.sqrt(Math.pow(dm, 2) + Math.pow(dmpp, 2) + Math.pow(dmxc, 2));

        if(dmcym > protocol.getMaxDmcym()) //для вывода максимальной погрешности в протоколе выбираем самую большую погрешность
            protocol.setMaxDmcym(dmcym);

        return dmcym;

    }



    public double calculationDrcym12VPCompleteMethod(double standard, double Dm){// вычисление Drcym для комплектного метода 1/2 ВП

        if(Math.abs(standard) < valueHalfInnerLimit){ //абс - добавил от себя, если значение в столбике эталона с минусом, тогда дрсум  и десум 1/2 не будет правильно считаться
            return drcym12VPCompleteMethod = -123.456; //на самом деле тут получается 0, но мы возвращаем такое значение, чтобы при формировании rtf отфильтровать по этому значению
        }else{
            drcym12VPCompleteMethod = (Math.abs(Dm) * 100) / Math.abs(standard);

            if(drcym12VPCompleteMethod > protocol.getMaxDrcym12VPCompleteMethod()){
                protocol.setMaxDrcym12VPCompleteMethod(drcym12VPCompleteMethod);
            }

            return drcym12VPCompleteMethod;
        }

    }

    public double calculationDecym12VPCompleteMethod(double standard, double Dm){

        if(Math.abs(standard) > valueHalfInnerLimit){ //абс - добавил от себя, если значение в столбике эталона с минусом, тогда дрсум  и десум 1/2 не будет правильно считаться
            return decym12VPCompleteMethod = -123.456; //на самом деле тут получается 0, но мы возвращаем такое значение, чтобы при формировании rtf отфильтровать по этому значению
        }else{
            decym12VPCompleteMethod = (Math.abs(Dm) * 100) / valueHalfInnerLimit;

            if(decym12VPCompleteMethod > protocol.getMaxDecym12VPCompleteMethod()){
                protocol.setMaxDecym12VPCompleteMethod(decym12VPCompleteMethod);
            }

            return decym12VPCompleteMethod;
        }

    }

    public double calculationDecymCompleteMethod(double Dm){
        // System.out.println("dm = " +Dm);
        decymCompleteMethod = (Math.abs(Dm) * 100) / (Math.abs(parserRTF.getMaxOnRange() - parserRTF.getMinOnRange()));

        if(decymCompleteMethod > protocol.getMaxDecymCompleteMethod()){
            protocol.setMaxDecymCompleteMethod(decymCompleteMethod);
        }

        if(Math.abs(Dm) > Math.abs(protocol.getMaxDm())){ // используется при комплектном методе без 1/2
            protocol.setMaxDm(Dm);
        }

        //System.out.println("---> " + decymCompleteMethod);
        return decymCompleteMethod;

    }

    public double calculationDrcym12VP(double standard, double dmcym){ // на вход подаем значение из столбика эталон и результат вычисления 1/2ВП

        if(Math.abs(standard) < valueHalfInnerLimit){ //абс - добавил от себя, если значение в столбике эталона с минусом, тогда дрсум  и десум 1/2 не будет правильно считаться
            return drcym12VP = -123.456; //на самом деле тут получается 0, но мы возвращаем такое значение, чтобы при формировании rtf отфильтровать по этому значению

        }else{
            drcym12VP = (dmcym * 100) / Math.abs(standard);

            if(drcym12VP > protocol.getMaxDrcym12VP()){ // установка максимального значения
                protocol.setMaxDrcym12VP(drcym12VP);
            }

            return drcym12VP;
        }
    }

    public double calculationDecym12VP(double standard, double dmcym){

        if(Math.abs(standard) > valueHalfInnerLimit){ //абс - добавил от себя, если значение в столбике эталона с минусом, тогда дрсум  и десум 1/2 не будет правильно считаться
            return decym12VP = -123.456; //на самом деле тут получается 0, но мы возвращаем такое значение, чтобы при формировании rtf отфильтровать по этому значению
        }else{
            decym12VP = (dmcym * 100) / valueHalfInnerLimit;

            if(decym12VP > protocol.getMaxDecym12VP()){
                protocol.setMaxDecym12VP(decym12VP);
            }

            return decym12VP;
        }
    }

    public double calculationDmpp(){ //расчет Dmpp

        dmpp = (Math.abs(parserRTF.getMaxOnRange() - parserRTF.getMinOnRange()) * protocol.getReadDataBase().getValueY().get(protocol.getProtocolScopeNumber())) / 100;

        return dmpp;

    }

    public double calculationDmcym(double dm, double dmpp){ //Расчет Dmсум

        dmcym = 1.1 * Math.sqrt(Math.pow(dm, 2) + Math.pow(dmpp, 2));

        if(dmcym > protocol.getMaxDmcym()) //для вывода максимальной погрешности в протоколе выбираем самую большую погрешность
            protocol.setMaxDmcym(dmcym);

        return dmcym;

    }


    public double calculationDecym(double dmcym){ // расчет Deсум

        decym = (dmcym * 100) / Math.abs(parserRTF.getMaxOnRange() - parserRTF.getMinOnRange());

        if(decym > protocol.getMaxDecym()) //для вывода максимальной погрешности в протоколе выбираем самую большую погрешность
            protocol.setMaxDecym(decym);

        return decym;

    }



    public double getDmpp() {
        return dmpp;
    }

    public double getDmcym() {
        return dmcym;
    }

    public double getDecym() {
        return decym;
    }



}
