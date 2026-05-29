package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class ApplicationXmlGenerator {
    public String generateApplicationXml(String sdaDescription, String rarName) throws JAXBException {
        application13.Application application = new application13.Application();
        application13.DisplayName displayName = new application13.DisplayName();
        displayName.setvalue(sdaDescription);
        application.setDisplayName(displayName);
        application13.Module module = new application13.Module();
        application.getModule().add(module);
        application13.Connector connector = new application13.Connector();
        connector.setvalue(rarName);
        module.getConnectorOrEjbOrJavaOrWeb().add(connector);

        JAXBContext ctx = JAXBContext.newInstance(application13.Application.class);
        return Komar.marshaller(ctx, application);
    }
}
