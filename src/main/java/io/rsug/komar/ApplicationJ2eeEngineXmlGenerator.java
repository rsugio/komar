package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicationJ2eeEngineXmlGenerator {
    // see https://help.sap.com/docs/SAP_NETWEAVER_750/c591e2679e104fcdb8dc8e77771ff524/4ac4eebebcfb22aee10000000a42189b.html?locale=en-US
    final Map<String, Object> constants;

    public ApplicationJ2eeEngineXmlGenerator(Map<String, Object> constants) {
        this.constants = constants;
    }

    private String $(String key) {
        return Objects.requireNonNull(constants.get(key), key).toString();
    }

    private String[] $$(String key) {
        return (String[]) Objects.requireNonNull(constants.get(key), key);
    }

    public String generateApplicationJ2eeEngineXml(List<DeployReference> lst) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(applicationj2eeengine.ApplicationJ2EeEngine.class);
        applicationj2eeengine.ApplicationJ2EeEngine a2e = new applicationj2eeengine.ApplicationJ2EeEngine();
        if (lst!=null) for (DeployReference dr: lst) {
            applicationj2eeengine.ReferenceType reference = new applicationj2eeengine.ReferenceType();
            reference.setReferenceType(dr.referenceType);
            applicationj2eeengine.ReferenceTargetType target = new applicationj2eeengine.ReferenceTargetType();
            reference.setReferenceTarget(target);
            target.setValue(dr.target);
            target.setTargetType(dr.targetType);
            target.setProviderName(dr.providerName);
            a2e.getReference().add(reference);
        }

        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();
        marshaller.marshal(a2e, sw);
        return sw.toString();
    }
}
