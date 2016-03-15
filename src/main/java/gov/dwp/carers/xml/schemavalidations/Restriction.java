package gov.dwp.carers.xml.schemavalidations;

public class Restriction{
    public Restriction( String name ){
        this.name=name;
    }
    private String name;
    private Integer minlength;
    private Integer maxlength;
    private String pattern;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinlength() {
        return minlength;
    }

    public void setMinlength(Integer minlength) {
        this.minlength = minlength;
    }

    public Integer getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
