package io.rsug.komar;

import java.util.Objects;

enum ReferenceType {weak, hard, strong}

enum TargetType {
    library, service, interface_, application;

    static TargetType parseString(String s) {
        if (interface_.toString().equals(s)) throw new IllegalArgumentException();
        return "interface".equals(s) ? interface_ : valueOf(s);
    }
}

public class DeployReference {
    final String referenceType, targetType, providerName, target;

    public DeployReference(String referenceType, String targetType, String providerName, String target) {
        Objects.requireNonNull(ReferenceType.valueOf(referenceType));
        Objects.requireNonNull(TargetType.parseString(targetType));
        Objects.requireNonNull(providerName);
        Objects.requireNonNull(target);
        if (providerName.isEmpty() || target.isEmpty()) throw new IllegalArgumentException();

        this.referenceType = referenceType;
        this.targetType = targetType;
        this.providerName = providerName;
        this.target = target;
    }

    public static DeployReference applicationHard(String name) {
        return new DeployReference("hard", "application", "sap.com", name);
    }

    public static DeployReference applicationWeak(String name) {
        return new DeployReference("weak", "application", "sap.com", name);
    }

    public static DeployReference interfaceHard(String name) {
        return new DeployReference("hard", "interface", "sap.com", name);
    }

    public static DeployReference interfaceWeak(String name) {
        return new DeployReference("weak", "interface", "sap.com", name);
    }

    public static DeployReference libraryHard(String name) {
        return new DeployReference("hard", "library", "sap.com", name);
    }

    public static DeployReference libraryWeak(String name) {
        return new DeployReference("weak", "library", "sap.com", name);
    }

    public static DeployReference serviceHard(String name) {
        return new DeployReference("hard", "service", "sap.com", name);
    }

    public static DeployReference serviceWeak(String name) {
        return new DeployReference("weak", "service", "sap.com", name);
    }



}
