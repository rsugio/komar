package io.rsug.komar;

import provider.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProviderXmlGenerator {

    public String generateProviderXml(String displayName,
                                      String componentName,
                                      String providerName,
                                      List<DeployReference> deployReferenceList,
                                      List<String> jarNames
    ) throws JAXBException {
        ProviderDescriptor pd = new ProviderDescriptor();
        pd.setDisplayName(displayName);
        pd.setComponentName(componentName);
        pd.setProviderName(providerName);
        References references = new References();
        pd.setReferences(references);
        for (DeployReference dref : deployReferenceList) {
            Reference ref = new Reference();
            ref.setType(dref.referenceType);
            ref.setStrength(dref.targetType);
            ref.setProviderName(dref.providerName);
            ref.setvalue(dref.target);
            references.getReference().add(ref);
        }
        Jars jars = new Jars();
        pd.setJars(jars);
        for (String jar : jarNames) {
            JarName jn = new JarName();
            jn.setvalue(jar);
            jars.getJarName().add(jn);
        }

        return Komar.marshaller(JAXBContext.newInstance(ProviderDescriptor.class), pd);
    }
}
