package gov.dwp.carers.xml.validation;

import org.junit.Test;


/**
 * Created with IntelliJ IDEA.
 * User: Jorge Migueis
 * Date: 27/06/2013
 * To change this template use File | Settings | File Templates.
 */
public class S2LocalResourceResolverTest {

    @Test
    public void testSuccessfulResolveResource() throws Exception {
        S2LocalResourceResolver resolver = new S2LocalResourceResolver("/legacy/");
        resolver.resolveResource("http://www.w3.org/2001/XMLSchema",
                "http://www.govtalk.gov.uk/dwp/ca/claim",
                null,
                "schema/ca/dwp-ca-702-v1_10.xsd",
                null);
    }

    @Test(expected = org.w3c.dom.ls.LSException.class)
    public void testFailResolveResourceIfCannotFindResource() throws Exception {
        S2LocalResourceResolver resolver = new S2LocalResourceResolver("InvalidPath");
        resolver.resolveResource("http://www.w3.org/2001/XMLSchema",
                "http://www.govtalk.gov.uk/dwp/ca/claim",
                null,
                "schema/ca/dwp-ca-702-v1_10.xsd",
                null);
    }

    @Test
    public void testSuccessfulResolveResourceFutureV1() throws Exception {
        S2LocalResourceResolver resolver = new S2LocalResourceResolver("/future/0.1/");
        resolver.resolveResource("http://www.w3.org/2001/XMLSchema",
                "http://www.govtalk.gov.uk/dwp/ca/claim",
                null,
                "schema/ca/CarersAllowance_Schema.xsd",
                null);
    }

    @Test
    public void testSuccessfulResolveResourceFutureV2() throws Exception {
        S2LocalResourceResolver resolver = new S2LocalResourceResolver("/future/0.2/");
        resolver.resolveResource("http://www.w3.org/2001/XMLSchema",
                "http://www.govtalk.gov.uk/dwp/ca/claim",
                null,
                "schema/ca/CarersAllowance_Schema.xsd",
                null);
    }
}
