package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;

public class ApplicationJ2eeEngineXmlGenerator {
    // see https://help.sap.com/docs/SAP_NETWEAVER_750/c591e2679e104fcdb8dc8e77771ff524/4ac4eebebcfb22aee10000000a42189b.html?locale=en-US

    public static String generateApplicationJ2eeEngineXml(List<DeployReference> lst) throws JAXBException {
        applicationj2eeengine.ApplicationJ2EeEngine a2e = new applicationj2eeengine.ApplicationJ2EeEngine();
        if (lst != null) for (DeployReference dr : lst) {
            applicationj2eeengine.ReferenceType reference = new applicationj2eeengine.ReferenceType();
            reference.setReferenceType(dr.referenceType);
            applicationj2eeengine.ReferenceTargetType target = new applicationj2eeengine.ReferenceTargetType();
            reference.setReferenceTarget(target);
            target.setValue(dr.target);
            target.setTargetType(dr.targetType);
            target.setProviderName(dr.providerName);
            a2e.getReference().add(reference);
        }

        JAXBContext ctx = JAXBContext.newInstance(applicationj2eeengine.ApplicationJ2EeEngine.class);
        return Komar.marshaller(ctx, a2e);
    }
}
