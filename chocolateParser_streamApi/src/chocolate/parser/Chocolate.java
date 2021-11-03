package chocolate.parser;

public class Chocolate {
    // поля класса
    private String exportCountry;//страна-экспортер какао-бобов
    private String exportCompany;//название фирмы экспортера
    private String brandChocolate;//название марки шоколада
    private int expertiseIndex;//индекс экспертизы
    private int expertiseYear;//год экспертизы
    private int contentCacaoInPercent;//процентное содержание какао
    private String produceCountry;//страна, в которой выпускается сам шоколад указанной марки
    private float expertRating;//экспертная оценка

    //гетеры и сетеры:

    public String getExportCountry() {
        return exportCountry;
    }

    public void setExportCountry(String exportCountry) {
        this.exportCountry = exportCountry;
    }

    public String getExportCompany() {
        return exportCompany;
    }

    public void setExportCompany(String exportCompany) {
        this.exportCompany = exportCompany;
    }

    public String getBrandChocolate() {
        return brandChocolate;
    }

    public void setBrandChocolate(String brandChocolate) {
        this.brandChocolate = brandChocolate;
    }

    public int getExpertiseIndex() {
        return expertiseIndex;
    }

    public void setExpertiseIndex(int expertiseIndex) {
        this.expertiseIndex = expertiseIndex;
    }

    public int getExpertiseYear() {
        return expertiseYear;
    }

    public void setExpertiseYear(int expertiseYear) {
        this.expertiseYear = expertiseYear;
    }

    public int getContentCacaoInPercent() {
        return contentCacaoInPercent;
    }

    public void setContentCacaoInPercent(int contentCacaoInPercent) {
        this.contentCacaoInPercent = contentCacaoInPercent;
    }

    public String getProduceCountry() {
        return produceCountry;
    }

    public void setProduceCountry(String produceCountry) {
        this.produceCountry = produceCountry;
    }

    public float getExpertRating() {
        return expertRating;
    }

    public void setExpertRating(float expertRating) {
        this.expertRating = expertRating;
    }

    @Override
    public String toString() {//переопределенный метод вывода на экран
        return "Chocolate{" +
                "exportCountry='" + exportCountry + '\'' +
                ", exportCompany='" + exportCompany + '\'' +
                ", brandChocolate='" + brandChocolate + '\'' +
                ", expertiseIndex=" + expertiseIndex +
                ", expertiseYear=" + expertiseYear +
                ", contentCacaoInPercent='" + contentCacaoInPercent + '\'' +
                ", produceCountry='" + produceCountry + '\'' +
                ", expertRating=" + expertRating +
                '}';
    }
}
