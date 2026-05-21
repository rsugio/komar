package io.rsug.komar;

import connector15.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class RaXmlGenerator {
    final Map<String,Object> constants;

    public RaXmlGenerator(Map<String,Object> constants) {
        this.constants = constants;
    }

    private static XsdStringType xsdStringType(String s) {
        XsdStringType xst = new XsdStringType();
        xst.setValue(s);
        return xst;
    }

    private static FullyQualifiedClassType fullyQualifiedClassType(String s) {
        FullyQualifiedClassType t = new FullyQualifiedClassType();
        t.setValue(s);
        return t;
    }

    private static ConfigPropertyType configPropertyType(String description, String name, String type, String value) {
        ConfigPropertyType cp = new ConfigPropertyType();
        DescriptionType d = new DescriptionType();
        d.setValue(description);
        ConfigPropertyNameType cpn = new ConfigPropertyNameType();
        cpn.setValue(name);
        ConfigPropertyTypeType cpt = new ConfigPropertyTypeType();
        cpt.setValue(type);
        cp.getDescription().add(d);
        cp.setConfigPropertyName(cpn);
        cp.setConfigPropertyType(cpt);
        cp.setConfigPropertyValue(xsdStringType(value));
        return cp;
    }

    private String $(String key) {
        return Objects.requireNonNull(constants.get(key), key).toString();
    }

    public String generateConnectorXml() throws JAXBException {
        ConnectorType ct = new ConnectorType();
        ct.setVersion(new BigDecimal("1.5"));
        DisplayNameType dnt = new DisplayNameType();
        dnt.setValue("Ресурсный адаптер " + $("raName") + " (из ra.xml)");
        ct.getDisplayName().add(dnt);
        ct.setVendorName(xsdStringType($("adapterVendor")));
        ct.setEisType(xsdStringType($("raEis")));
        ct.setResourceadapterVersion(xsdStringType($("raVersion")));

        ResourceadapterType ra = new ResourceadapterType();
        ct.setResourceadapter(ra);
        OutboundResourceadapterType ora = new OutboundResourceadapterType();
        ra.setOutboundResourceadapter(ora);
        ConnectionDefinitionType cd = new ConnectionDefinitionType();
        ora.getConnectionDefinition().add(cd);
        cd.setManagedconnectionfactoryClass(fullyQualifiedClassType($("raSPIManagedConnectionFactory")));
        ConfigPropertyType addressMode = configPropertyType(null, "addressMode", "java.lang.String", "CPA");
        ConfigPropertyType adapterType = configPropertyType(null, "adapterType", "java.lang.String", $("adapterType"));
        ConfigPropertyType adapterNamespace = configPropertyType(null, "adapterNamespace", "java.lang.String", $("adapterNamespace"));
        cd.getConfigProperty().add(addressMode);
        cd.getConfigProperty().add(adapterType);
        cd.getConfigProperty().add(adapterNamespace);
        cd.setConnectionfactoryInterface(fullyQualifiedClassType("javax.resource.cci.ConnectionFactory"));
        cd.setConnectionfactoryImplClass(fullyQualifiedClassType($("raCCIConnectionFactory")));
        cd.setConnectionInterface(fullyQualifiedClassType("javax.resource.cci.Connection"));
        cd.setConnectionImplClass(fullyQualifiedClassType($("raCCIConnection")));
        TransactionSupportType ts = new TransactionSupportType();
        ts.setValue("NoTransaction");
        ora.setTransactionSupport(ts);
        AuthenticationMechanismType am = new AuthenticationMechanismType();
        am.setAuthenticationMechanismType(xsdStringType("BasicPassword"));
        CredentialInterfaceType ci = new CredentialInterfaceType();
        ci.setValue("javax.resource.spi.security.PasswordCredential");
        am.setCredentialInterface(ci);
        ora.getAuthenticationMechanism().add(am);
        TrueFalseType tf = new TrueFalseType();
        tf.setValue(false);
        ora.setReauthenticationSupport(tf);

        ObjectFactory cof = new ObjectFactory();
        JAXBContext ctx = JAXBContext.newInstance(ConnectorType.class);
        JAXBElement<ConnectorType> jaxbElement = cof.createConnector(ct);

        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();
        marshaller.marshal(jaxbElement, sw);
        return sw.toString();
    }
}
