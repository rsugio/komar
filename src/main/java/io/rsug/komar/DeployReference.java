package io.rsug.komar;

import java.util.Objects;

enum ReferenceType {weak, hard}

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
}
