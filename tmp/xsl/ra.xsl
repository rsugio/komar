<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output indent="yes" method="xml"/>

  <xsl:template match="connector">
    <xsl:text disable-output-escaping="yes">&lt;connector xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/connector_1_5.xsd" version="1.5"&gt;</xsl:text>
      <xsl:for-each select="child::description">
        <xsl:copy-of select="/connector/description"/>
      </xsl:for-each>
      <xsl:for-each select="child::display-name">
        <xsl:copy-of select="//display-name"/>
      </xsl:for-each>
      <xsl:for-each select="child::icon">
        <icon>
          <xsl:if test="//icon/small-icon">
            <xsl:copy-of select="//icon/small-icon"/>
          </xsl:if>
          <xsl:if test="//icon/large-icon">
            <xsl:copy-of select="//icon/large-icon"/>
          </xsl:if>
        </icon>
      </xsl:for-each>
      <vendor-name><xsl:value-of select="vendor-name"/></vendor-name>
      <eis-type><xsl:value-of select="eis-type"/></eis-type>
      <xsl:if test="license">
        <license>
          <xsl:if test="license/description">
            <description><xsl:value-of select="license/description"/></description>
          </xsl:if>
          <license-required><xsl:value-of select="license/license-required"/></license-required>
        </license>
      </xsl:if>
      <resourceadapter-version><xsl:value-of select="version"/></resourceadapter-version>
      <resourceadapter>
        <outbound-resourceadapter>
          <connection-definition>
            <managedconnectionfactory-class><xsl:value-of select="resourceadapter/managedconnectionfactory-class"/></managedconnectionfactory-class>
            <xsl:apply-templates select="resourceadapter/config-property"/>
            <connectionfactory-interface><xsl:value-of select="resourceadapter/connectionfactory-interface"/></connectionfactory-interface>
            <connectionfactory-impl-class><xsl:value-of select="resourceadapter/connectionfactory-impl-class"/></connectionfactory-impl-class>
            <connection-interface><xsl:value-of select="resourceadapter/connection-interface"/></connection-interface>
            <connection-impl-class><xsl:value-of select="resourceadapter/connection-impl-class"/></connection-impl-class>
          </connection-definition>
          <transaction-support><xsl:value-of select="resourceadapter/transaction-support"/></transaction-support>
          <xsl:apply-templates select="resourceadapter/authentication-mechanism"/>
          <reauthentication-support><xsl:value-of select="resourceadapter/reauthentication-support"/></reauthentication-support>
        </outbound-resourceadapter>
        <xsl:apply-templates select="resourceadapter/security-permission"/>
      </resourceadapter>
    <xsl:text disable-output-escaping="yes">&lt;/connector&gt;</xsl:text>
  </xsl:template>

  <xsl:template match="config-property">
    <config-property>
      <xsl:if test="description">
        <description><xsl:value-of select="description"/></description>
      </xsl:if>
      <config-property-name><xsl:value-of select="config-property-name"/></config-property-name>
      <config-property-type><xsl:value-of select="config-property-type"/></config-property-type>
      <xsl:if test="config-property-value">
        <config-property-value><xsl:value-of select="config-property-value"/></config-property-value>
      </xsl:if>
    </config-property>
  </xsl:template>

  <xsl:template match="authentication-mechanism">
    <authentication-mechanism>
      <xsl:if test="description">
        <description><xsl:value-of select="description"/></description>
      </xsl:if>
      <authentication-mechanism-type><xsl:value-of select="authentication-mechanism-type"/></authentication-mechanism-type>
      <credential-interface><xsl:value-of select="credential-interface"/></credential-interface>
    </authentication-mechanism>
  </xsl:template>

  <xsl:template match="security-permission">
    <security-permission>
      <xsl:if test="description">
        <description><xsl:value-of select="description"/></description>
      </xsl:if>
      <security-permission-spec><xsl:value-of select="security-permission-spec"/></security-permission-spec>
    </security-permission>
  </xsl:template>
</xsl:stylesheet>
