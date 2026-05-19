<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output indent="yes"
            doctype-system="http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd"
            doctype-public="-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN"/>

<xsl:template match="ejb-jar|assembly-descriptor|cmp-field|container-transaction|ejb-ref|enterprise-beans|entity|env-entry|method|method-params|method-permission|resource-ref|security-role|security-role-ref|session">
  <xsl:variable name="tagname"><xsl:value-of select="name(.)"/></xsl:variable>

  <xsl:element name="{$tagname}">
    <xsl:apply-templates/>
  </xsl:element>

</xsl:template>

<xsl:template match="description|display-name|ejb-class|ejb-client-jar|ejb-link|ejb-name|ejb-ref-name|ejb-ref-type|env-entry-name|env-entry-type|env-entry-value|field-name|home|large-icon|method-intf|method-name|method-param|persistence-type|prim-key-class|primkey-field|reentrant|remote|res-ref-name|res-type|role-link|role-name|session-type|small-icon|transaction-type|trans-attribute">
  <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="res-auth">
  <xsl:choose>
    <xsl:when test="normalize-space(.)='Bean'">
      <res-auth>Application</res-auth>
    </xsl:when>
    <xsl:otherwise>
      <xsl:copy-of select="."/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="session-timeout|isolation-level|bean-count|with-cache|user-id|group-id|pid">
</xsl:template>

</xsl:transform>