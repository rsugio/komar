import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.bind.*;
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
            Document doc = db.parse(path.toFile());
            switch (name) {
                case "ra.xml":
                    resourceAdapter(path, doc);
                    break;
                case "connector-j2ee-engine.xml":
                    connectorj2eeengine(path, doc);
                    break;
                case "application-j2ee-engine.xml":
                    applicationj2eeengine(path, doc);
                    break;
                case "provider.xml":
                    provider(path, doc);
                    break;
                case "log-configuration.xml":
                    logconfiguraion(path, doc);
                    break;
                default:
            }
        } catch (Exception e) {
            System.err.println(path.toAbsolutePath());
            e.printStackTrace();
        }
    }

    void resourceAdapter(Path path, Document doc) throws JAXBException, IOException {
        // connector_1_5.xsd, пакет connector15
        connector15.ObjectFactory cof = new connector15.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(connector15.ConnectorType.class);
        connector15.ConnectorType ct = new connector15.ConnectorType();
        JAXBElement<connector15.ConnectorType> result = cof.createConnector(ct);
        // result можем писать в файл через marshaller

        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        result = unmarshaller.unmarshal(doc, connector15.ConnectorType.class);
        ct = result.getValue();
        Assertions.assertEquals(new BigDecimal("1.5"), ct.getVersion());
    }

    void connectorj2eeengine(Path path, Document doc) throws JAXBException, IOException {
        // connector-j2ee-engine.xsd, пакет connectorj2eeengine
        connectorj2eeengine.ObjectFactory cof = new connectorj2eeengine.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(connectorj2eeengine.ConnectorType.class);
        connectorj2eeengine.ConnectorType ct = new connectorj2eeengine.ConnectorType();
        JAXBElement<connectorj2eeengine.ConnectorType> result = cof.createConnector(ct);
        // result можем писать в файл через marshaller

        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        result = unmarshaller.unmarshal(doc, connectorj2eeengine.ConnectorType.class);
        ct = result.getValue();
//        Assertions.assertNotEquals(null, ct.getDescription());
    }

    void applicationj2eeengine(Path path, Document doc) throws JAXBException, IOException {
        // application-j2ee-engine_customized.xsd, пакет applicationj2eeengine
        // JAXB на стандартном application-j2ee-engine.xsd генерирует не совсем ожидаемое
        // определение для fail-over-enable, пришлось упростить
        applicationj2eeengine.ObjectFactory cof = new applicationj2eeengine.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(applicationj2eeengine.ApplicationJ2EeEngine.class);
        JAXBElement<applicationj2eeengine.ApplicationJ2EeEngine> jaxbResult;
        applicationj2eeengine.ApplicationJ2EeEngine result;

        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        jaxbResult = unmarshaller.unmarshal(doc, applicationj2eeengine.ApplicationJ2EeEngine.class);
        result = jaxbResult.getValue();
    }

    void provider(Path path, Document doc) {

    }

    void logconfiguraion(Path path, Document doc) throws JAXBException {
        logConfiguration.ObjectFactory cof = new logConfiguration.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(logConfiguration.LogConfiguration.class);
        JAXBElement<logConfiguration.LogConfiguration> jaxbResult;
        logConfiguration.LogConfiguration result;

        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        jaxbResult = unmarshaller.unmarshal(doc, logConfiguration.LogConfiguration.class);
        result = jaxbResult.getValue();
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.marshal(jaxbResult, System.out);
        System.out.println();

    }
}
