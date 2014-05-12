package com.dwp.carers.s2.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public abstract class XmlTestBase {

    private final Logger logger = LoggerFactory.getLogger(XmlTestBase.class);
    /**
     * Look for XML file in test resources, load it and convert it into an immutable string.
     *
     * @param fileName Name of file to read from test resources.
     * @return A string representing the content of the XML file read.
     * @throws java.io.IOException Thrown if an error occurred while reading the XML file.
     */
    protected String readXMLFile(final String fileName) throws IOException {
        final InputStream is = fileInputStream(fileName);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        final StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }

    protected InputStream fileInputStream(final String fileName){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}