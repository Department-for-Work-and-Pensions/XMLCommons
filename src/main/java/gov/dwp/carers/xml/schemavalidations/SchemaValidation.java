package gov.dwp.carers.xml.schemavalidations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class SchemaValidation {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaValidation.class);
    private static final String XSD = "CarersAllowance_Schema.xsd";

    private Document doc;
    private static Restrictions RESTRICTIONS;
    private String schemaFile;

    public SchemaValidation(String version) {
        loadSchema(version);
    }

    public void loadSchema(String version) {
        if (RESTRICTIONS != null && RESTRICTIONS.getRestrictions() != null && RESTRICTIONS.getRestrictions().size() > 0) {
            LOG.debug("Schema for version:" + version + " is already loaded with " + RESTRICTIONS.getRestrictions().size());
        } else {
            String schemaFile = schemaDefaultFilename(version);
            LOG.info("Schema Validation loading schemafile:" + schemaFile);
            Document doc = getDocFromXmlFile(schemaFile);
            Restrictions r = new Restrictions();
            r.setVersion(version);
            if (doc != null) {
                r.setRestrictions(expandRestrictions(doc));
                if (r.getRestrictions().size() == 0) {
                    LOG.error("ERROR building restrictions from xsd, 0 restrictions found");
                }
            }
            RESTRICTIONS = r;
            this.doc = doc;
        }
    }

    // Since we cache the xsd schema into static we need to be able to clear it for tests.
    // so we can test different scenarios by loading different xsd files.
    public static void clearSchema() {
        LOG.info("Clearing schema ... this will cause a reload / reparse of xsd");
        RESTRICTIONS = null;
    }


    // Allow passing a fully pathed file in as version for testing
    public String schemaDefaultFilename(String version) {
        URL url = SchemaValidation.class.getClassLoader().getResource(version);
        if (url != null) {
            LOG.debug("Found xsd file at:" + url.getPath());
            return version;
        } else {
            String pathfile = "future/" + version + "/schema/ca/" + XSD;
            URL url2 = SchemaValidation.class.getClassLoader().getResource(pathfile);
            if (url2 != null) {
                LOG.debug("Found xsd file at:" + url2.getPath());
                return (pathfile);
            }
        }
        LOG.error("ERROR No xsd file found matching " + version);
        return null;
    }

    /* We need to exclude type mappings from the final paths.
    i.e. <xs:element minOccurs="0" name="Why" type="QuestionTextThreeThousandType"/>
    We have a mapping of Why->QuestionTextThreeThousandType so we can walk through the mappings to get the restriction.
    But when we build the path we need to exclude the type mappings.
    So build a list of type names so we can exclude from the path later.
     */
    public Set<String> getTypeNames(Document doc) {
        Set<String> typeNames = new HashSet<>();
        NodeList elements = doc.getElementsByTagName("xs:element");
        for (int n = 0; n < elements.getLength(); n++) {
            Node element = elements.item(n);
            Node type = element.getAttributes().getNamedItem("type");
            if (type != null) {
                typeNames.add(type.getNodeValue());
            }
        }
        return typeNames;
    }

    /* Build a hash with the restrictions, keyed by restriction name.
        SimpleType restrictions have a parent with the restriction name and child attribs with restrictions such as min and max
                <xs:simpleType name="ThreeThousandTextType">
                <xs:restriction base="RestrictedStringType">
                    <xs:minLength value="1"/>
                    <xs:maxLength value="3000"/>
                </xs:restriction>
                </xs:simpleType>
    */
    public Map<String, Restriction> getSimpleRestrictions(Document doc) {
        Map<String, Restriction> restrictions = new HashMap<>();
        NodeList restrictionNodes = doc.getElementsByTagName("xs:restriction");
        for (int n = 0; n < restrictionNodes.getLength(); n++) {
            Node node = restrictionNodes.item(n);

            Node parent = node.getParentNode();
            String parentName = null;
            if (parent.getAttributes().getNamedItem("name") != null) {
                parentName = parent.getAttributes().getNamedItem("name").getNodeValue();
            }

            // We are only looking for text strings with minValue and maxValue, or patterns
            // We currently ignore other types such as enums
            String restrictionBase = node.getAttributes().getNamedItem("base").getNodeValue();
            if (parentName != null && (restrictionBase.equals("RestrictedStringType") || restrictionBase.equals("xs:string"))) {
                Restriction restriction = new Restriction(parentName);
                NodeList children = node.getChildNodes();
                for (int x = 0; x < children.getLength(); x++) {
                    Node child = children.item(x);
                    if (child.getNodeName().equals("xs:minLength")) {
                        restriction.setMinlength(Integer.valueOf(child.getAttributes().getNamedItem("value").getNodeValue()));
                    }
                    if (child.getNodeName().equals("xs:maxLength")) {
                        restriction.setMaxlength(Integer.valueOf(child.getAttributes().getNamedItem("value").getNodeValue()));
                    }
                    if (child.getNodeName().equals("xs:pattern")) {
                        restriction.setPattern(child.getAttributes().getNamedItem("value").getNodeValue());
                    }
                }
                restrictions.put(restriction.getName(), restriction);
            }
        }

        return (restrictions);
    }


    /* The complex nodes are outside the elements so need to be pulled into a hash so we can walk down through elements and then
        down through th complex types until we get to the underlying simple type.
     */
    public Map<String, Node> getComplexNodes(Document doc) {
        Map<String, Node> nodes = new HashMap<>();
        NodeList complexNodes = doc.getElementsByTagName("xs:complexType");
        for (int n = 0; n < complexNodes.getLength(); n++) {
            Node node = complexNodes.item(n);
            if (node.getAttributes().getNamedItem("name") != null) {
                nodes.put(node.getAttributes().getNamedItem("name").getNodeValue(), node);
            }
        }
        return nodes;
    }


    /* To find all the validation mappings we need to recurse down for each element to lookup the underlying complex type validations
        until we get to the underlying simple restriction.
     */
    public Map<String, Restriction> expandRestrictions(Document doc) {
        // Get the simple restrictions, complex restrictions so we can look them up as we walk down the elements.
        Map<String, Restriction> simpleRestrictions = getSimpleRestrictions(doc);
        Map<String, Node> complexNodes = getComplexNodes(doc);


        // Type names will be excluded from the path such as QuestionTextThreeThousandType
        //
        Set<String> typeNames = getTypeNames(doc);


        // walk through all elements recursing through the mappings as needed, adding resolved restrictions to restrictions
        Map<String, Restriction> restrictions = new HashMap<>();
        NodeList allElements = doc.getElementsByTagName("xs:element");
        for (int n = 0; n < allElements.getLength(); n++) {
            Node node = allElements.item(n);
            if (node.getNodeName().equals("xs:element") && node.getAttributes().getNamedItem("name") != null) {
                String name = node.getAttributes().getNamedItem("name").getNodeValue();
                recurseComplexMappings(node, name, simpleRestrictions, complexNodes, typeNames, restrictions);
            }
        }

        return restrictions;
    }


    // Recurse complexNodeName till we get to the end .. hopefully find a simple node maybe with restriction
    private void recurseComplexMappings(Node node, String path, Map<String, Restriction> simpleRestrictions, Map<String, Node> complexNodes, Set<String> typeNames, Map<String, Restriction> restrictions) {
        // 4 possibilities for this node that we are recursing into ...
        //
        // a) It is an element with nested child elements that we need to recurse through, building path as we go down
        // <xs:element minOccurs="0" name="OtherInformation">
        //  <xs:complexType>
        //      <xs:sequence>
        //          <xs:element minOccurs="0" name="WelshCommunication" type="QuestionTextType"/>
        //          <xs:element minOccurs="0" maxOccurs="1" name="AdditionalInformation" type="QuestionYesNoWhyThreeThousandType"/>
        //
        // b) It is a complexType with nested child element(s) that we need to recurse through, not building path
        // <xs:complexType name="QuestionYesNoWhyThreeThousandType">
        // <xs:complexContent>
        // <xs:extension base="QuestionTextType">
        // <xs:sequence>
        // <xs:element minOccurs="0" name="Why" type="QuestionTextThreeThousandType"/>
        //
        // c) It is a single line element with attributes mapping to complexType that we need to recurse through, not building path
        // <xs:element minOccurs="0" maxOccurs="1" name="AdditionalInformation" type="QuestionYesNoWhyThreeThousandType"/>
        //
        // d) It is a simpleType with restriction
        // <xs:simpleType name="ThreeThousandTextType">
        // <xs:restriction base="RestrictedStringType">
        // <xs:minLength value="15"/>
        // <xs:maxLength value="3000"/>
        // Recurse down through children and attributes regardless of whether element or simple or whatever
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int x = 0; x < children.getLength(); x++) {
                Node child = children.item(x);
                if (child.getAttributes() != null && child.getAttributes().getNamedItem("name") != null) {
                    String childname = child.getAttributes().getNamedItem("name").getNodeValue();
                    recurseComplexMappings(child, path + "//" + childname, simpleRestrictions, complexNodes, typeNames, restrictions);
                } else {
                    recurseComplexMappings(child, path, simpleRestrictions, complexNodes, typeNames, restrictions);
                }
            }
        }
        if (node.hasAttributes()) {
            Node namenode = node.getAttributes().getNamedItem("name");
            Node typenode = node.getAttributes().getNamedItem("type");
            if (namenode != null && typenode != null) {
                String name = node.getAttributes().getNamedItem("name").getNodeValue();
                String type = node.getAttributes().getNamedItem("type").getNodeValue();
                if (simpleRestrictions.containsKey(type)) {
                    Restriction restriction = simpleRestrictions.get(type);
                    String cleanPath = deduplicatePath(path, typeNames);
                    LOG.debug("ADDING restriction for " + cleanPath + "->" + type + " with maxlength:" + restriction.getMaxlength());
                    restrictions.put(cleanPath, restriction);
                } else if (complexNodes.containsKey(type)) {
                    recurseComplexMappings(complexNodes.get(type), path + "//" + name, simpleRestrictions, complexNodes, typeNames, restrictions);
                } else {
                    // Maybe we are a simple type that does not have a maxen or pattern restriction. Such as enum.
                    LOG.debug("We cannot find a complex or simple lookup for node type:" + type);
                }
            }

            // If we based on another complex type we need to recurse down that path to see if any restrictions down there.
            // <xs:extension base="QuestionType">
            // we might find a generic restriction such as
            // <xs:element name="Answer" type="HundredTwentyTextType"/>
            Node basenode = node.getAttributes().getNamedItem("base");
            if (basenode != null) {
                String base = basenode.getNodeValue();
                if (complexNodes.containsKey(base)) {
                    recurseComplexMappings(complexNodes.get(base), path, simpleRestrictions, complexNodes, typeNames, restrictions);
                } else {
                    // We may have recursed down to a valiation such as enum or pattern which we have not included so ignore.
                }
            }
        }
    }


    // We may have duplicates on the path such as AccountDetailsType//AccountDetailsType//HolderName
    // And also we need to remove any typeNames from the path.
    // i.e. We may have OtherInformation//AdditionalInformation//QuestionYesNoWhyThreeThousandType//Why
    // So need to get rid of the typeName QuestionYesNoWhyThreeThousandType from the path
    static public String deduplicatePath(String path, Set<String> typeNames) {
        if (path == null) {
            return null;
        }
        StringBuffer newPath = new StringBuffer("");
        String[] p = path.split("//");
        for (int n = 0; n < p.length; n++) {
            if (!newPath.toString().endsWith(p[n]) && !typeNames.contains(p[n])) {
                if (!newPath.toString().equals("")) {
                    newPath.append("//");
                }
                newPath.append(p[n]);
            }
        }
        return newPath.toString();
    }

    public Document getDocFromXmlFile(String filename) {
        Document doc = null;
        try {
            InputStream in = SchemaValidation.class.getClassLoader().getResourceAsStream(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(in);
        } catch (Exception e) {
            LOG.error("Exception loading file:" + filename + " -> " + e.toString());
            LOG.error("Exception:", e);
        }
        return doc;
    }

    public Restriction getRestriction(String nodepath) {
        if (RESTRICTIONS != null && RESTRICTIONS.getRestrictions() != null && RESTRICTIONS.getRestrictions().containsKey(nodepath)) {
            return RESTRICTIONS.getRestrictions().get(nodepath);
        } else {
            return new Restriction("");
        }
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getSchemaFile() {
        return schemaFile;
    }

    public void setSchemaFile(String schemaFile) {
        this.schemaFile = schemaFile;
    }
}
