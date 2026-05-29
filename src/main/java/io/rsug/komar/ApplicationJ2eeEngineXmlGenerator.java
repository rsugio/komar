package io.rsug.komar;

import applicationj2eeengine.ApplicationJ2EeEngine;
import applicationj2eeengine.ModuleType;
import applicationj2eeengine.ModulesAdditionalType;
import applicationj2eeengine.ReferenceTargetType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;

public class ApplicationJ2eeEngineXmlGenerator {
    // see https://help.sap.com/docs/SAP_NETWEAVER_750/c591e2679e104fcdb8dc8e77771ff524/4ac4eebebcfb22aee10000000a42189b.html?locale=en-US

    public static String generateApplicationJ2eeEngineXml(List<DeployReference> lst, String additionalModuleName, String additionalModuleType, String providerName) throws JAXBException {
        ApplicationJ2EeEngine a2e = new ApplicationJ2EeEngine();
        if (lst != null) for (DeployReference dr : lst) {
            applicationj2eeengine.ReferenceType reference = new applicationj2eeengine.ReferenceType();
            reference.setReferenceType(dr.referenceType);
            ReferenceTargetType target = new ReferenceTargetType();
            reference.setReferenceTarget(target);
            target.setValue(dr.target);
            target.setTargetType(dr.targetType);
            target.setProviderName(dr.providerName);
            a2e.getReference().add(reference);
        }
        if (additionalModuleName!=null && additionalModuleType!=null) {
            ModuleType mt = new ModuleType();
            mt.setEntryName(additionalModuleName);
            mt.getContainerType().add(additionalModuleType);
            ModulesAdditionalType ma = new ModulesAdditionalType();
            ma.getModule().add(mt);
            a2e.setModulesAdditional(ma);
        }
        if (providerName!=null) {
            a2e.setProviderName(providerName);
        }

        JAXBContext ctx = JAXBContext.newInstance(applicationj2eeengine.ApplicationJ2EeEngine.class);
        return Komar.marshaller(ctx, a2e);
    }
}
