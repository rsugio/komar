package io.rsug.komar;

import adaptermetadata.AdapterTypeMetaData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

public class Komar {
    public static String minimalSdaDdXml(String softwareType) {
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<SDA><SoftwareType>%s</SoftwareType></SDA>";
        return String.format(template, softwareType);
    }

    public static String marshaller(JAXBContext ctx, Object o) throws JAXBException {
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();
        marshaller.marshal(o, sw);
        return sw.toString();
    }

    public static String componentElementDC(String dcName, String dcVendor, String dcVendorLocation, String counter, String swcName, String swcVendor) {
        Objects.requireNonNull(dcName);
        Objects.requireNonNull(dcVendor);
        Objects.requireNonNull(dcVendorLocation);
        Objects.requireNonNull(counter);
        Objects.requireNonNull(swcName);
        Objects.requireNonNull(swcVendor);

        String xml = "<componentelement name=\"%s\" vendor=\"%s\" componenttype=\"DC\" " + "subsystem=\"NO_SUBSYS\" location=\"%s\" " + "counter=\"%s\" scname=\"%s\" scvendor=\"%s\" " + "deltaversion=\"F\" componentprovider=\"%s\" servertype=\"P4\"/>";
        return String.format(xml, dcName, dcVendor, dcVendorLocation, counter, swcName, swcVendor, dcVendorLocation);
    }

    public static String componentElementSC(String scName, String scVendor, String scVendorLocation, String counter,
                                            String release, String serviceLevel, String patchLevel,
                                            String updateversion) {
        Objects.requireNonNull(scName);
        Objects.requireNonNull(scVendor);
        Objects.requireNonNull(scVendorLocation);
        Objects.requireNonNull(counter);
        Objects.requireNonNull(release);
        Objects.requireNonNull(serviceLevel);
        Objects.requireNonNull(patchLevel);
        Objects.requireNonNull(updateversion);

        String xml = "<componentelement name=\"{0}\" vendor=\"{1}\" location=\"{2}\" "
                + "componenttyp=\"SC\" subsystem=\"NO_SUBSYS\" " +
                "counter=\"{3}\" scname=\"{0}\" scvendor=\"{1}\" deltaversion=\"F\" " +
                "release=\"{4}\" servicelevel=\"{5}\" patchlevel=\"{6}\" " +
                "updateversion=\"{7}\" componentprovider=\"{2}\" />";
        return MessageFormat.format(xml, scName, scVendor, scVendorLocation, counter,
                release, serviceLevel, patchLevel, updateversion);
    }

    public static String generateProviderXml(String displayName, String componentName, String providerName, List<DeployReference> deployReferenceList, List<String> jarNames) {
        try {
            return ProviderXmlGenerator.generateProviderXml(displayName, componentName, providerName, deployReferenceList, jarNames);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateConnectorJ2eeXmlGenerator(String description, String resourceAdapterJNDI, List<DeployReference> drefList) {
        try {
            return ConnectorJ2eeXmlGenerator.generateConnectorJ2eeXmlGenerator(description, resourceAdapterJNDI, drefList);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRaXml(String adapterType, String adapterNamespace, String adapterVendor, String adapterVersion, String displayNameType, String eisType, String raSPIManagedConnectionFactory, String raCCIConnectionFactory, String raCCIConnection) {
        try {
            return RaXmlGenerator.generateRaXml(adapterType, adapterNamespace, adapterVendor, adapterVersion, displayNameType, eisType, raSPIManagedConnectionFactory, raCCIConnectionFactory, raCCIConnection);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateApplicationJ2eeEngineXml(List<DeployReference> lst, String additionalModuleName, String additionalModuleType, String providerName) {
        try {
            return ApplicationJ2eeEngineXmlGenerator.generateApplicationJ2eeEngineXml(lst, additionalModuleName, additionalModuleType, providerName);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateApplicationXmlRar(String sdaDescription, String rarName) {
        try {
            return ApplicationXmlGenerator.generateApplicationXmlRar(sdaDescription, rarName);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateApplicationXmlWar(String sdaDescription, String webUri, String contextRoot) {
        try {
            return ApplicationXmlGenerator.generateApplicationXmlWar(sdaDescription, webUri, contextRoot);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String marshallAdapterTypeMetaData(AdapterTypeMetaData atmd) {
        try {
            return AdapterMetaData.marshall(atmd);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
