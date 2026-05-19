<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">  
  
<xsl:output indent="yes" 
            doctype-system="ejb-j2ee-engine.dtd"
            omit-xml-declaration="yes"/>
<xsl:template match = "text()"/>
<xsl:template match="/ejb-jar">
  
<xsl:if test="enterprise-beans/session/session-timeout | assembly-descriptor/container-transaction/isolation-level | enterprise-beans/session/bean-count | enterprise-beans/entity/bean-count">
  
<ejb-j2ee-engine>
  
  <xsl:if test="enterprise-beans/session/session-timeout or enterprise-beans/session/bean-count or enterprise-beans/entity/bean-count">
    <enterprise-beans>
      <xsl:apply-templates select="enterprise-beans"/>
    </enterprise-beans>
  </xsl:if>
  
  <xsl:if test="assembly-descriptor/container-transaction/isolation-level">
    <transaction-descriptor>
      <xsl:apply-templates select="assembly-descriptor"/>
    </transaction-descriptor>
  </xsl:if>
  
</ejb-j2ee-engine>
  
</xsl:if>
  
</xsl:template>
  
<xsl:template match="enterprise-beans">
  <xsl:apply-templates/>
</xsl:template>
  
<xsl:template match="session">
  
  <xsl:if test="bean-count or session-timeout">
    <enterprise-bean>
      <ejb-name>
        <xsl:value-of select="ejb-name"/>
      </ejb-name>
      
      <xsl:if test="bean-count">
        <container-size>
          <xsl:value-of select="normalize-space(bean-count)"/>
        </container-size>
      </xsl:if>
      
      <xsl:if test="session-timeout">
        <session-props>
          <session-timeout>
            <xsl:value-of select="normalize-space(session-timeout)"/>
          </session-timeout>
        </session-props>
      </xsl:if>
    </enterprise-bean>
  </xsl:if>
    
</xsl:template>
  
<xsl:template match="entity">
  
  <xsl:if test="bean-count">
    <enterprise-bean>
      <ejb-name>
        <xsl:value-of select="ejb-name"/>
      </ejb-name>
      
      <container-size>
        <xsl:value-of select="normalize-space(bean-count)"/>
      </container-size>
    </enterprise-bean>
  </xsl:if>
  
</xsl:template>
  
<xsl:template match="assembly-descriptor">
  <xsl:apply-templates select="container-transaction"/>
</xsl:template>
  
<xsl:template match="container-transaction">
  <xsl:apply-templates select="isolation-level"/>
</xsl:template>

<xsl:template match="isolation-level">
  <xsl:if test="not(normalize-space(.)='None')">
    <isolation-level>
      <xsl:for-each select="../method">
        <method>
          <xsl:copy-of select="ejb-name"/>
          <xsl:copy-of select="method-intf"/>
          <xsl:copy-of select="method-name"/>
          <xsl:copy-of select="method-params"/>
        </method>
      </xsl:for-each>
      <isolation-attribute>
        <xsl:choose>
        <xsl:when test="normalize-space(.)='Commited'">
          Committed
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="normalize-space(.)"/>
        </xsl:otherwise>
        </xsl:choose>
      </isolation-attribute>
    </isolation-level>
  </xsl:if>
</xsl:template>
  
</xsl:transform>