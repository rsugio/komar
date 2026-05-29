package io.rsug.komar;

import connectorj2eeengine.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.lang.String;
import java.util.List;

public class ConnectorJ2eeXmlGenerator {
    // see https://help.sap.com/docs/SAP_NETWEAVER_750/c591e2679e104fcdb8dc8e77771ff524/4ac4eebebcfb22aee10000000a42189b.html?locale=en-US

    public static String generateConnectorXml(String description, String resourceAdapterJNDI, List<DeployReference> drefList) throws JAXBException {
        ConnectorType con = new ConnectorType();
        con.setDescription(description);
        ResourceadapterType ra = new ResourceadapterType();
        ra.setRaJndiName(resourceAdapterJNDI);
        OutboundResourceadapterType ora = new OutboundResourceadapterType();
        ConnectionDefinitionType cd = new ConnectionDefinitionType();
        cd.setConnectionfactoryInterface("javax.resource.cci.ConnectionFactory");   //PO const
        cd.setJndiName(resourceAdapterJNDI);
        ora.getConnectionDefinition().add(cd);
        ra.setOutboundResourceadapter(ora);
        con.setResourceadapter(ra);

        LoaderReferencesType lr = new LoaderReferencesType();
        for (DeployReference link : drefList) {
            LoaderReferencesType.LoaderName ln = new LoaderReferencesType.LoaderName();
            ln.setStrength(link.referenceType);
            ln.setValue(link.target);
            lr.getLoaderName().add(ln);
        }
        ra.setLoaderReferences(lr);

        JAXBContext ctx = JAXBContext.newInstance(ConnectorType.class);
        JAXBElement<ConnectorType> jaxbElement = new ObjectFactory().createConnector(con);
        return Komar.marshaller(ctx, jaxbElement);
    }
}
