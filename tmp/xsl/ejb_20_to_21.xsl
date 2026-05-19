<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns="http://java.sun.com/xml/ns/j2ee"> 

<xsl:output indent="yes"/>

<xsl:template match="/ejb-jar">
  <ejb-jar xmlns="http://java.sun.com/xml/ns/j2ee" version="2.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/ejb-jar_2_1.xsd">
    <xsl:apply-templates select="description|display-name"/>
    <xsl:call-template name="icon"/>
    <xsl:apply-templates select="enterprise-beans|relationships|assembly-descriptor|ejb-client-jar"/>
  </ejb-jar>
</xsl:template>

<xsl:template match="message-driven">
  <xsl:element name="message-driven">
    <xsl:apply-templates select="description|display-name"/>
    <xsl:call-template name="icon"/>
    <xsl:apply-templates select="ejb-name|ejb-class|transaction-type"/>
    <xsl:apply-templates select="message-driven-destination"/>

    <xsl:if test="message-selector or acknowledge-mode or message-driven-destination/subscription-durability">
      <xsl:element name="activation-config">
        <xsl:apply-templates select="message-selector|acknowledge-mode|message-driven-destination/subscription-durability"/>
      </xsl:element>
    </xsl:if>

    <xsl:apply-templates select="env-entry|ejb-ref|ejb-local-ref|security-identity|resource-ref|resource-env-ref"/>

  </xsl:element>
</xsl:template>

<xsl:template match="session">
  <xsl:element name="session">
    <xsl:apply-templates select="description|display-name"/>
    <xsl:call-template name="icon"/>
    <xsl:apply-templates select="ejb-name|home|remote|local-home|local|ejb-class|session-type|transaction-type|env-entry|ejb-ref|ejb-local-ref|security-role-ref|security-identity|resource-ref|resource-env-ref"/>
    </xsl:element>
</xsl:template>

<xsl:template match="entity">
  <xsl:element name="entity">
    <xsl:apply-templates select="description|display-name"/>
    <xsl:call-template name="icon"/>
    <xsl:apply-templates select="ejb-name|home|remote|local-home|local|ejb-class|persistence-type|prim-key-class|reentrant|cmp-version|abstract-schema-name|cmp-field|primkey-field|env-entry|ejb-ref|ejb-local-ref|security-role-ref|security-identity|resource-ref|resource-env-ref|query"/>
    </xsl:element>
</xsl:template>

<xsl:template match="*[local-name() != 'ejb-jar' and local-name() != 'message-driven' and local-name() != 'session' and local-name() != 'entity']">

    <xsl:variable name="tagname"><xsl:value-of select="name(.)"/></xsl:variable>
  
    <xsl:if test="($tagname='security-role-ref') and (../resource-ref) and (count(preceding-sibling::security-role-ref)=0)">
      <xsl:for-each select="../resource-ref">
        <xsl:element name="resource-ref">
           <xsl:apply-templates/>
        </xsl:element>
      </xsl:for-each>
    </xsl:if>

    <xsl:if test="($tagname='security-role-ref') and (../resource-env-ref) and (count(preceding-sibling::security-role-ref)=0)">
      <xsl:for-each select="../resource-env-ref">
        <xsl:element name="resource-env-ref">
           <xsl:apply-templates/>
        </xsl:element>
      </xsl:for-each>
    </xsl:if>

    <xsl:if test="($tagname='security-identity') and (../resource-ref) and (count(../security-role-ref)=0)">
      <xsl:for-each select="../resource-ref">
        <xsl:element name="resource-ref">
           <xsl:apply-templates/>
        </xsl:element>
      </xsl:for-each>
    </xsl:if>

    <xsl:if test="($tagname='security-identity') and (../resource-env-ref) and (count(../security-role-ref)=0)">
      <xsl:for-each select="../resource-env-ref">
        <xsl:element name="resource-env-ref">
           <xsl:apply-templates/>
        </xsl:element>
      </xsl:for-each>
    </xsl:if>

    <xsl:if test="( (($tagname!='resource-ref') and ($tagname!='resource-env-ref')) or 
                    ((count(../security-identity)=0) and (count(../security-role-ref)=0)) ) and 
		    (($tagname!='message-selector') and 
		     ($tagname!='acknowledge-mode') and 
		     ($tagname!='subscription-durability') and 
		     ($tagname!='message-driven-destination') )">
      <xsl:element name="{$tagname}">
        <xsl:apply-templates/>
      </xsl:element>
    </xsl:if>

  <xsl:if test="$tagname='message-selector'">
    <xsl:element name="activation-config-property">
      <xsl:element name="activation-config-property-name">messageSelector</xsl:element>
      <xsl:element name="activation-config-property-value"><xsl:value-of select="normalize-space(.)"/></xsl:element>
    </xsl:element>
  </xsl:if>

  <xsl:if test="$tagname='acknowledge-mode'">
    <xsl:element name="activation-config-property">
      <xsl:element name="activation-config-property-name">acknowledgeMode</xsl:element>
      <xsl:element name="activation-config-property-value"><xsl:value-of select="normalize-space(.)"/></xsl:element>
    </xsl:element>
  </xsl:if>

  <xsl:if test="$tagname='subscription-durability'">
    <xsl:element name="activation-config-property">
      <xsl:element name="activation-config-property-name">subscriptionDurability</xsl:element>
      <xsl:element name="activation-config-property-value"><xsl:value-of select="normalize-space(.)"/></xsl:element>
    </xsl:element>
  </xsl:if>

  <xsl:if test="$tagname='message-driven-destination'">
    <xsl:element name="message-destination-type">
      <xsl:value-of select="normalize-space(destination-type)"/>
    </xsl:element>
  </xsl:if>

</xsl:template>


<xsl:template match="text()">
  <xsl:variable name="val"><xsl:value-of select="."/></xsl:variable>

  <xsl:choose>
    <xsl:when test="$val='True' or $val='False'">
      <xsl:choose>
        <xsl:when test="$val='True'">
    true
        </xsl:when>
        <xsl:otherwise>
    false
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
    <xsl:value-of select="$val"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="icon">
  <xsl:if test="small-icon or large-icon">
  <xsl:element name="icon">
    <xsl:if test="small-icon">
   <xsl:element name="small-icon"><xsl:value-of select="small-icon"/></xsl:element>
    </xsl:if>
    <xsl:if test="large-icon">
    <xsl:element name="large-icon"><xsl:value-of select="large-icon"/></xsl:element>
    </xsl:if>
  </xsl:element>
</xsl:if>
</xsl:template>

</xsl:transform>