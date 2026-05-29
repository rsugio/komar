package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import application13.*;

import java.util.Objects;

public class ApplicationXmlGenerator {
    public static String generateApplicationXmlRar(String sdaDescription, String rarName) throws JAXBException {
        Objects.requireNonNull(sdaDescription);
        Objects.requireNonNull(rarName);
        Application application = new Application();
        DisplayName displayName = new DisplayName();
        displayName.setvalue(sdaDescription);
        application.setDisplayName(displayName);
        application13.Module module = new application13.Module();
        application.getModule().add(module);
        Connector connector = new Connector();
        connector.setvalue(rarName);
        module.getConnectorOrEjbOrJavaOrWeb().add(connector);

        JAXBContext ctx = JAXBContext.newInstance(application13.Application.class);
        return Komar.marshaller(ctx, application);
    }

    public static String generateApplicationXmlWar(String sdaDescription, String webUri, String contextRoot) throws JAXBException {
        Objects.requireNonNull(sdaDescription);
        Objects.requireNonNull(webUri);
        Objects.requireNonNull(contextRoot);

        Application application = new Application();
        DisplayName displayName = new DisplayName();
        displayName.setvalue(sdaDescription);
        application.setDisplayName(displayName);
        application13.Module module = new application13.Module();
        application.getModule().add(module);

        WebUri weburi = new WebUri();
        weburi.setvalue(webUri);
        ContextRoot contextroot = new ContextRoot();
        contextroot.setvalue(contextRoot);

        Web web = new Web();
        web.setWebUri(weburi);
        web.setContextRoot(contextroot);
        module.getConnectorOrEjbOrJavaOrWeb().add(web);

        JAXBContext ctx = JAXBContext.newInstance(application13.Application.class);
        return Komar.marshaller(ctx, application);
    }
}
