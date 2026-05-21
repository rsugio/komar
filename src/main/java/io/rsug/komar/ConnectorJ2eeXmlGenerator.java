package io.rsug.komar;

import connectorj2eeengine.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.String;
import java.util.Map;
import java.util.Objects;

public class ConnectorJ2eeXmlGenerator {
    // see https://help.sap.com/docs/SAP_NETWEAVER_750/c591e2679e104fcdb8dc8e77771ff524/4ac4eebebcfb22aee10000000a42189b.html?locale=en-US
    final Map<String,Object> constants;

    public ConnectorJ2eeXmlGenerator(Map<String,Object> constants) {
        this.constants = constants;
    }

    private String $(String key) {
        return Objects.requireNonNull(constants.get(key), key).toString();
    }

    private String[] $$(String key) {
        return (String[]) Objects.requireNonNull(constants.get(key), key);
    }

    public String generateConnectorXml() throws JAXBException {
        ConnectorType con = new ConnectorType();
        con.setDescription($("kolhoz"));
        ResourceadapterType ra = new ResourceadapterType();
        ra.setRaJndiName($("raName"));
        OutboundResourceadapterType ora = new OutboundResourceadapterType();
        ConnectionDefinitionType cd = new ConnectionDefinitionType();
        cd.setConnectionfactoryInterface("javax.resource.cci.ConnectionFactory");   //PO const
        cd.setJndiName($("raName"));
        ora.getConnectionDefinition().add(cd);
        ra.setOutboundResourceadapter(ora);
        con.setResourceadapter(ra);
        LoaderReferencesType lr = new LoaderReferencesType();
        for (String link: $$("connectorLoaderReferences")) {
            LoaderReferencesType.LoaderName ln = new LoaderReferencesType.LoaderName();
            ln.setStrength("hard");
            ln.setValue(link);
            lr.getLoaderName().add(ln);
        }
        ra.setLoaderReferences(lr);

        ObjectFactory cof = new ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(ConnectorType.class);
        JAXBElement<ConnectorType> jaxbElement = cof.createConnector(con);

        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();
        marshaller.marshal(jaxbElement, sw);
        return sw.toString();
    }
}
