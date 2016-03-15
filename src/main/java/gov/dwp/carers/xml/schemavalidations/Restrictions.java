package gov.dwp.carers.xml.schemavalidations;

import java.util.Map;

/**
 * Contains a map of restrictions keyed by element name. And the schema version that they relate to.
 */
public class Restrictions {
    String version;
    Map<String,Restriction> restrictions;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Restriction> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Map<String, Restriction> restrictions) {
        this.restrictions = restrictions;
    }
}
