<?xml version="1.0"?>
<xsl:transform xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output indent="yes"
            doctype-system="persistent.dtd"/>

<xsl:template match="text()"/>


<xsl:template match="/EntityEJBStorages">
  <xsl:if test="EntityEJB">
    <persistent-ejb-map>
      <db-properties>
        <data-source-name>
	      <xsl:value-of select="EntityEJB/Storage/RDBStorageProps/@PoolName"/>
   	    </data-source-name>
      </db-properties>
      <entity-beans>
        <xsl:apply-templates/>
      </entity-beans>
    </persistent-ejb-map>
  </xsl:if>
</xsl:template>


<xsl:template match="EntityEJB">
  <entity-bean>
    <ejb-name>
      <xsl:value-of select="@Name"/>
    </ejb-name>
    <table-name>
      <xsl:value-of select="Storage/RDBStorageProps/@TableName"/>
    </table-name>
    <xsl:apply-templates select="Storage/RDBStorageProps/FieldColumnMap" mode="readKeyAttr"/>
    <xsl:apply-templates select="Storage/RDBStorageProps/rdbFinderDescriptor"/>
  </entity-bean>
</xsl:template>


<xsl:template match="FieldColumnMap" mode="readKeyAttr">
   <xsl:choose>
   <xsl:when test="@Key='1'">
     <field-map key-type="PrimaryKey">
        <xsl:apply-templates select="." mode="readFieldInfo"/>
     </field-map>
   </xsl:when>
   <xsl:when test="@Key='0'">
     <field-map key-type="NoKey">
        <xsl:apply-templates select="." mode="readFieldInfo"/>
     </field-map>
   </xsl:when>
   <xsl:otherwise>
      <field-map>
        <xsl:apply-templates select="." mode="readFieldInfo"/>
      </field-map>
   </xsl:otherwise>
   </xsl:choose>
</xsl:template>


<xsl:template match="FieldColumnMap" mode="readFieldInfo">
  <field-name>
    <xsl:value-of select="@Field"/>
  </field-name>
  <column>
    <column-name>
      <xsl:value-of select="@Column"/>
    </column-name>
  </column>
</xsl:template>


<xsl:template match="rdbFinderDescriptor">
  <finder-descriptor>
    <criteria>
      <xsl:apply-templates select="." mode="processCriteria"/>
    </criteria>
    <method-name>
      <xsl:value-of select="@MethodName"/>
    </method-name>
    <method-params>
      <xsl:for-each select="MethodArgument">
        <method-param>
          <xsl:value-of select="."/>
        </method-param>
      </xsl:for-each>
    </method-params>
  </finder-descriptor>
</xsl:template>


<xsl:template match="rdbFinderDescriptor" mode="processCriteria">
  <xsl:choose>
    <xsl:when test="@Criteria=''">
      <!-- findAll -->
      <xsl:apply-templates select=".."/>
    </xsl:when>
    <xsl:when test="starts-with(@Criteria, 'select')">
      <!-- sql statement is fully specified -->
      <xsl:value-of select="translate(@Criteria,'$','?')"/>
    </xsl:when>
    <xsl:otherwise>
      <!-- only where clause is specified -->
      <xsl:apply-templates select=".."/> where <xsl:value-of select="translate(@Criteria,'$','?')"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:template match="RDBStorageProps">
  <xsl:text>select </xsl:text>
  <xsl:for-each select="FieldColumnMap">
    <xsl:text>\"</xsl:text><xsl:value-of select="translate(@Column,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/><xsl:text>\"</xsl:text>
    <xsl:if test="not(position()=last())"><xsl:text>, </xsl:text></xsl:if>
  </xsl:for-each>
  <xsl:text> from </xsl:text><xsl:value-of select="@TableName"/>
</xsl:template>


</xsl:transform>