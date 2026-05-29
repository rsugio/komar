import adaptermetadata.AdapterTypeMetaData;
import adaptermetadata.Attribute;
import adaptermetadata.AttributeReference;
import adaptermetadata.Outbound;
import io.rsug.komar.AdapterMetaData;
import io.rsug.komar.Komar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import provider.ProviderDescriptor;

import javax.xml.bind.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
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
    public void dummy() {
        String x = "", y;
        y = Komar.minimalSdaDdXml(x);
        Objects.requireNonNull(y);
        y = Komar.componentElementDC(x, x, x, x, x, x);
        Objects.requireNonNull(y);
        y = Komar.generateProviderXml(x, x, x, null, null);
        Objects.requireNonNull(y);
        y = Komar.generateConnectorJ2eeXmlGenerator(x, x, null);
        Objects.requireNonNull(y);
        y = Komar.generateRaXml(x, x, x, x, x, x, x, x, x);
        Objects.requireNonNull(y);
        y = Komar.generateApplicationJ2eeEngineXml(null);
        Objects.requireNonNull(y);
        y = Komar.generateApplicationXml(x, x);
        Objects.requireNonNull(y);
    }

    @Test
    public void runAllTests() throws Exception {
        Path testDir = Paths.get("./src/test/resources");
        try (Stream<Path> pathStream = Files.walk(testDir, 10)) {
            pathStream.forEach(path -> {
                if (Files.isRegularFile(path) && path.toString().contains(".xml")) {
                    parseXml(path);
                }
            });
        }
    }

    void parseXml(Path path) {
        String name = path.getFileName().toString();
        try {
            Document doc = db.parse(path.toFile());
            if (name.startsWith("ra.xml")) {
                resourceAdapter(doc);
            }

            switch (name) {
                case "connector-j2ee-engine.xml":
                    connectorj2eeengine(doc);
                    break;
                case "application-j2ee-engine.xml":
                    applicationj2eeengine(doc);
                    break;
                case "provider.xml":
                    provider(doc);
                    break;
                case "log-configuration.xml":
                    logconfiguraion(doc);
                    break;
                default:
            }
        } catch (Exception e) {
            System.err.println(path.toAbsolutePath());
            e.printStackTrace();
        }
    }

    void resourceAdapter(Document doc) throws JAXBException {
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

    void connectorj2eeengine(Document doc) throws JAXBException {
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

    void applicationj2eeengine(Document doc) throws JAXBException {
        // application-j2ee-engine_customized.xsd, пакет applicationj2eeengine
        // JAXB на стандартном application-j2ee-engine.xsd генерирует не совсем ожидаемое
        // определение для fail-over-enable, пришлось упростить
        applicationj2eeengine.ObjectFactory cof = new applicationj2eeengine.ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(applicationj2eeengine.ApplicationJ2EeEngine.class);
        JAXBElement<applicationj2eeengine.ApplicationJ2EeEngine> jaxbResult;

        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        jaxbResult = unmarshaller.unmarshal(doc, applicationj2eeengine.ApplicationJ2EeEngine.class);
        applicationj2eeengine.ApplicationJ2EeEngine result = jaxbResult.getValue();
    }

    void provider(Document doc) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(provider.ProviderDescriptor.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<provider.ProviderDescriptor> jr = unmarshaller.unmarshal(doc, provider.ProviderDescriptor.class);
        ProviderDescriptor pd = jr.getValue();
    }

    void logconfiguraion(Document doc) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(logConfiguration.LogConfiguration.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<logConfiguration.LogConfiguration> jaxbResult = unmarshaller.unmarshal(doc, logConfiguration.LogConfiguration.class);
        logConfiguration.LogConfiguration result = jaxbResult.getValue();
        Marshaller marshaller = ctx.createMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshal(jaxbResult, sw);
    }

    @Test
    void adapterTypeMetaData() throws Exception {
        AdapterTypeMetaData idoc = AdapterMetaData.unmarshall(Objects.requireNonNull(getClass().getResourceAsStream("/adaptermetadata/IDoc_AAE.xml")));
        AdapterMetaData.marshall(idoc);
        AdapterTypeMetaData sftp = AdapterMetaData.unmarshall(Objects.requireNonNull(getClass().getResourceAsStream("/adaptermetadata/SFTPAdapterMetadata.xml")));
        AdapterMetaData.marshall(sftp);
        AdapterTypeMetaData sample = AdapterMetaData.unmarshall(Objects.requireNonNull(getClass().getResourceAsStream("/adaptermetadata/SampleRA.xml")));
        AdapterMetaData.marshall(sample);

        AdapterTypeMetaData my = AdapterMetaData.makeStub("Echo", "1", "Echo adapter");
        Attribute adapterStatus = AdapterMetaData.adapterStatus();
        my.getAttributeOrAttributeTableOrDynamicAttributes().add(adapterStatus);
        Attribute text64 = AdapterMetaData.text("text64", 64);
        my.getAttributeOrAttributeTableOrDynamicAttributes().add(text64);
        Outbound out = AdapterMetaData.outbound(my, "NoProtocol");
        AttributeReference ar = new AttributeReference();
        ar.setReferenceName(text64.getName());
        out.getGlobalChannelAttributes().getTab().getAttributeReferenceOrAttributeGroup().add(ar);
        my.setOutbound(out);
        String s = AdapterMetaData.marshall(my);
        System.out.println(s);
        Objects.requireNonNull(s);
    }

}
