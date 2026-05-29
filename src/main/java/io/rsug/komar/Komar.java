package io.rsug.komar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class Komar {
    public static String minimalSdaDdXml(String softwareType) {
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SDA><SoftwareType>%s</SoftwareType></SDA>";
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
}
