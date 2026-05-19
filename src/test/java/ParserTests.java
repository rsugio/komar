import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ParserTests {
    static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    final DocumentBuilder db;

    ParserTests() throws ParserConfigurationException {
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        db = dbf.newDocumentBuilder();
    }

    @Test
    public void runAllTests() throws Exception {
        Path testDir = Paths.get("./src/test/resources");
        try (Stream<Path> pathStream = Files.walk(testDir, 10)) {
            pathStream.forEach(path -> {
                if (Files.isRegularFile(path) && path.toString().endsWith(".xml")) {
                    parseXml(path);
                }
            });
        }
    }

    void parseXml(Path path) {
        String name = path.getFileName().toString();
        try {
            db.parse(path.toFile());
            switch (name) {
                case "ra.xml":
                    resourceAdapter(path);
                    break;
                case "connector-j2ee-engine.xml":
                    connectorj2eeengine(path);
                    break;
                default:
            }
        } catch (Exception e) {
            System.err.println(path.toAbsolutePath());
            e.printStackTrace();
        }
    }

    void resourceAdapter(Path path) throws JAXBException, IOException {
        // connector_1_5.xsd, пакет connector15
        connector15.ObjectFactory cof = new connector15.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(connector15.ConnectorType.class);
        connector15.ConnectorType ct = new connector15.ConnectorType();
        JAXBElement<connector15.ConnectorType> result = cof.createConnector(ct);
        // result можем писать в файл через marshaller

        StreamSource source = new StreamSource(Files.newInputStream(path));
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        result = unmarshaller.unmarshal(source, connector15.ConnectorType.class);
        ct = result.getValue();
        Assertions.assertEquals(new BigDecimal("1.5"), ct.getVersion());
    }

    void connectorj2eeengine(Path path) throws JAXBException, IOException {
        // connector-j2ee-engine.xsd, пакет connectorj2eeengine
        connectorj2eeengine.ObjectFactory cof = new connectorj2eeengine.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(connectorj2eeengine.ConnectorType.class);
        connectorj2eeengine.ConnectorType ct = new connectorj2eeengine.ConnectorType();
        JAXBElement<connectorj2eeengine.ConnectorType> result = cof.createConnector(ct);
        // result можем писать в файл через marshaller

        StreamSource source = new StreamSource(Files.newInputStream(path));
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        result = unmarshaller.unmarshal(source, connectorj2eeengine.ConnectorType.class);
        ct = result.getValue();
        Assertions.assertNotEquals(null, ct.getDescription());
    }



}
