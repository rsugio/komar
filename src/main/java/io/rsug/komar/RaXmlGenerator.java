package io.rsug.komar;

import connector15.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.lang.String;
import java.math.BigDecimal;
import java.util.Objects;

public class RaXmlGenerator {

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

    public static String generateRaXml(String adapterType, String adapterNamespace, String adapterVendor, String adapterVersion, String displayNameType, String eisType, String raSPIManagedConnectionFactory, String raCCIConnectionFactory, String raCCIConnection) throws JAXBException {
        Objects.requireNonNull(displayNameType);
        Objects.requireNonNull(adapterVendor);
        Objects.requireNonNull(adapterType);
        Objects.requireNonNull(adapterNamespace);
        Objects.requireNonNull(adapterVersion);
        Objects.requireNonNull(eisType);
        Objects.requireNonNull(raSPIManagedConnectionFactory);
        Objects.requireNonNull(raCCIConnectionFactory);
        Objects.requireNonNull(raCCIConnection);

        ConnectorType ct = new ConnectorType();
        ct.setVersion(new BigDecimal("1.5"));
        DisplayNameType dnt = new DisplayNameType();
        dnt.setValue(displayNameType);
        ct.getDisplayName().add(dnt);
        ct.setVendorName(xsdStringType(adapterVendor));
        ct.setEisType(xsdStringType(eisType));
        ct.setResourceadapterVersion(xsdStringType(adapterVersion));

        ResourceadapterType ra = new ResourceadapterType();
        ct.setResourceadapter(ra);
        OutboundResourceadapterType ora = new OutboundResourceadapterType();
        ra.setOutboundResourceadapter(ora);
        ConnectionDefinitionType cd = new ConnectionDefinitionType();
        ora.getConnectionDefinition().add(cd);
        cd.setManagedconnectionfactoryClass(fullyQualifiedClassType(raSPIManagedConnectionFactory));
        ConfigPropertyType addressMode = configPropertyType(null, "addressMode", "java.lang.String", "CPA");
        ConfigPropertyType adapterTyp = configPropertyType(null, "adapterType", "java.lang.String", adapterType);
        ConfigPropertyType adapterNamespac = configPropertyType(null, "adapterNamespace", "java.lang.String", adapterNamespace);
        cd.getConfigProperty().add(addressMode);
        cd.getConfigProperty().add(adapterTyp);
        cd.getConfigProperty().add(adapterNamespac);
        cd.setConnectionfactoryInterface(fullyQualifiedClassType("javax.resource.cci.ConnectionFactory"));
        cd.setConnectionfactoryImplClass(fullyQualifiedClassType(raCCIConnectionFactory));
        cd.setConnectionInterface(fullyQualifiedClassType("javax.resource.cci.Connection"));
        cd.setConnectionImplClass(fullyQualifiedClassType(raCCIConnection));
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

        JAXBContext ctx = JAXBContext.newInstance(ConnectorType.class);
        JAXBElement<ConnectorType> jaxbElement = new ObjectFactory().createConnector(ct);
        return Komar.marshaller(ctx, jaxbElement);
    }
}
