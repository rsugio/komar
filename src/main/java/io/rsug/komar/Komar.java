package io.rsug.komar;

public class Komar {
    public static String minimalSdaDdXml(String softwareType) {
        String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SDA><SoftwareType>%s</SoftwareType></SDA>";
        return String.format(template, softwareType);
    }
}
