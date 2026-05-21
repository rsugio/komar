package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

public class ApplicationXmlGenerator {
    final Map<String, Object> constants;

    public ApplicationXmlGenerator(Map<String, Object> constants) {
        this.constants = constants;
    }

    private String $(String key) {
        return (String) Objects.requireNonNull(constants.get(key), key);
    }

    private String[] $$(String key) {
        return (String[]) Objects.requireNonNull(constants.get(key), key);
    }

    public String generateApplicationXml() throws JAXBException {
        application13.Application application = new application13.Application();
        application13.DisplayName displayName = new application13.DisplayName();
        displayName.setvalue($("sdaDescription"));
        application.setDisplayName(displayName);
        application13.Module module = new application13.Module();
        application.getModule().add(module);
        application13.Connector connector = new application13.Connector();
        connector.setvalue($("rarName"));
        module.getConnectorOrEjbOrJavaOrWeb().add(connector);

        JAXBContext ctx = JAXBContext.newInstance(application13.Application.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter sw = new StringWriter();
        marshaller.marshal(application, sw);
        return sw.toString();
    }
}
