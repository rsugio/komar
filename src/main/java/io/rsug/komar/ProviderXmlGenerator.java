package io.rsug.komar;

import provider.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.List;
import java.util.Objects;

public class ProviderXmlGenerator {

    public static String generateProviderXml(String displayName, String componentName, String providerName, List<DeployReference> deployReferenceList, List<String> jarNames) throws JAXBException {
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(componentName);
        Objects.requireNonNull(providerName);
        ProviderDescriptor pd = new ProviderDescriptor();
        pd.setDisplayName(displayName);
        pd.setComponentName(componentName);
        pd.setProviderName(providerName);
        References references = new References();
        pd.setReferences(references);
        if (deployReferenceList != null) {
            for (DeployReference dref : deployReferenceList) {
                Reference ref = new Reference();
                ref.setType(dref.referenceType);
                ref.setStrength(dref.targetType);
                ref.setProviderName(dref.providerName);
                ref.setvalue(dref.target);
                references.getReference().add(ref);
            }
        }
        Jars jars = new Jars();
        pd.setJars(jars);
        if (jarNames != null) {
            for (String jar : jarNames) {
                JarName jn = new JarName();
                jn.setvalue(jar);
                jars.getJarName().add(jn);
            }
        }

        return Komar.marshaller(JAXBContext.newInstance(ProviderDescriptor.class), pd);
    }
}
