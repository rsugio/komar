package io.rsug.komar;

import adaptermetadata.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Objects;

public class AdapterMetaData {
    public static AdapterTypeMetaData makeStub(String adapterType, String adapterVersion, String adapterLabelEn) {
        AdapterTypeMetaData atmd = new AdapterTypeMetaData();
        atmd.setVersion(new BigInteger(adapterVersion));
        atmd.setType(adapterType);

        //во всех хороших адаптерах есть название
        Label label = new Label();
        label.setLanguage("EN");
        label.setContent(adapterLabelEn);
        atmd.setGuiLabels(new GuiLabels());
        atmd.getGuiLabels().getLabel().add(label);
        return atmd;
    }

    public static Outbound outbound(AdapterTypeMetaData atmd, String sameProtocol) {
        Objects.requireNonNull(atmd);
        Objects.requireNonNull(sameProtocol);
        String version = atmd.getVersion().toString();

        Label label = new Label();
        label.setLanguage("EN");
        label.setContent(sameProtocol);

        MessageProtocol mp = new MessageProtocol();
        mp.setGuiLabels(new GuiLabels());
        mp.setName(sameProtocol);
        mp.setVersion(version);
        mp.getGuiLabels().getLabel().add(label);

        ModuleProcessorAttributes mpa = new ModuleProcessorAttributes();
        mp.setModuleProcessorAttributes(mpa);
        ModuleSequence ms = new ModuleSequence();
        mpa.setModuleSequence(ms);
        ModuleSequenceItem msi = new ModuleSequenceItem();
        ms.setModuleSequenceItem(msi);
        msi.setName("localejbs/ModuleProcessorExitBean");
        msi.setType("local");
        msi.setKey("exit");

        ValidMessageProtocols vmp = new ValidMessageProtocols();
        ProtocolIdentifier pi = new ProtocolIdentifier();
        pi.setName(mp.getName());
        pi.setVersion(mp.getVersion());
        vmp.getProtocolIdentifier().add(pi);

        TransportProtocol tp = new TransportProtocol();
        tp.setGuiLabels(new GuiLabels());
        tp.setName(sameProtocol);
        tp.setVersion(version);
        tp.getGuiLabels().getLabel().add(label);
        tp.setValidMessageProtocols(vmp);

        AttributeReference ar = new AttributeReference();
        Attribute adapterStatus = (Attribute) atmd.getAttributeOrAttributeTableOrDynamicAttributes().get(0);
        ar.setReferenceName(adapterStatus.getName());

        Tab tab = new Tab();
        tab.setId("main");
        tab.getAttributeReferenceOrAttributeGroup().add(ar);

        GlobalChannelAttributes gca = new GlobalChannelAttributes();
        gca.setTab(tab);

        Outbound out = new Outbound();
        out.getMessageProtocol().add(mp);
        out.getTransportProtocol().add(tp);
        out.setGlobalChannelAttributes(gca);
        return out;
    }

    public static Attribute adapterStatus() {
        Attribute attr = new Attribute();
        attr.setName("adapterStatus");
        attr.setUsage("optional");
        attr.setIsPassword(false);
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("Default"), String.class, "active"));
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("DataType"), String.class, "xsd:string"));
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("Length"), BigInteger.class, BigInteger.ZERO));
        attr.getFlagOrGuiLabelsOrDataType().add(getLabelsEN("Adapter status"));

        FixedValue fv = new FixedValue();
        fv.setValue("active");
        fv.setGuiLabels(getLabelsEN("Active"));
        attr.getFlagOrGuiLabelsOrDataType().add(fv);

        fv = new FixedValue();
        fv.setValue("inactive");
        fv.setGuiLabels(getLabelsEN("Inactive"));
        attr.getFlagOrGuiLabelsOrDataType().add(fv);

        return attr;
    }

    public static Attribute text(String name, int length) {
        Attribute attr = new Attribute();
        attr.setName(name);
        attr.setUsage("optional");
        attr.setIsPassword(false);
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("Flag"), String.class, "nonTransportable"));
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("Default"), String.class, "default value"));
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("DataType"), String.class, "xsd:string"));
        attr.getFlagOrGuiLabelsOrDataType().add(new JAXBElement<>(new QName("Length"), BigInteger.class, BigInteger.valueOf(length)));
        attr.getFlagOrGuiLabelsOrDataType().add(getLabelsEN(name));
        return attr;
    }

    public static GuiLabels getLabelsEN(String text) {
        Label label = new Label();
        label.setLanguage("EN");
        label.setContent(text);
        GuiLabels labels = new GuiLabels();
        labels.getLabel().add(label);
        return labels;
    }


    public static void linter(AdapterTypeMetaData atmd) throws RuntimeException {
        if (atmd.getGuiLabels() == null || atmd.getGuiLabels().getLabel() == null || atmd.getGuiLabels().getLabel().size() == 0) {
            throw new RuntimeException("/AdapterTypeMetaData/GuiLabels null or empty");
        }
    }

    public static String marshall(AdapterTypeMetaData atmd) throws JAXBException {
        linter(atmd);
        return Komar.marshaller(JAXBContext.newInstance(AdapterTypeMetaData.class), atmd);
    }

    public static AdapterTypeMetaData unmarshall(InputStream is) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(AdapterTypeMetaData.class);
        return (AdapterTypeMetaData) ctx.createUnmarshaller().unmarshal(is);
    }
}
