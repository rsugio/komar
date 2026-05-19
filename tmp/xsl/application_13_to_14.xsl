<?xml version="1.0" encoding="UTF-8"?>

<!-- 

This stylesheet transforms a 
http://java.sun.com/dtd/application_1_3.dtd compliant application.xml into a 
http://java.sun.com/xml/ns/j2ee/application_1_4.xsd compliant application.xml

-->
     
<!-- Author: Jan Sievers -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/j2ee">
  <xsl:output indent="yes" method="xml" encoding="UTF-8"/>
  <xsl:template match="/application">
    <application>
      <xsl:attribute name="xsi:schemaLocation">http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/application_1_4.xsd</xsl:attribute>
      <xsl:attribute name="version">1.4</xsl:attribute>
      <xsl:for-each select="description[.!='']">
        <description>
          <xsl:value-of select="."/>
        </description>
      </xsl:for-each>
      <xsl:for-each select="display-name[.!='']">
        <display-name>
          <xsl:value-of select="."/>
        </display-name>
      </xsl:for-each>
      <xsl:for-each select="icon[.!='']">
        <icon>
          <xsl:for-each select="small-icon[.!='']">
            <small-icon>
              <xsl:value-of select="."/>
            </small-icon>
          </xsl:for-each>
          <xsl:for-each select="large-icon[.!='']">
            <large-icon>
              <xsl:value-of select="."/>
            </large-icon>
          </xsl:for-each>
        </icon>
      </xsl:for-each>
      <xsl:for-each select="module[.!='']">
        <module>
          <xsl:for-each select="web[.!='']">
            <web>
              <xsl:for-each select="web-uri">
                <xsl:apply-templates select="." mode="copy-no-ns"/>
              </xsl:for-each>
              <xsl:for-each select="context-root">
                <xsl:apply-templates select="." mode="copy-no-ns"/>
              </xsl:for-each>
            </web>
          </xsl:for-each>
          <xsl:for-each select="connector|ejb|java|alt-dd">
            <xsl:apply-templates select="." mode="copy-no-ns"/>
          </xsl:for-each>
        </module>
      </xsl:for-each>

      <xsl:apply-templates select="security-role" mode="copy-no-ns"/>
    </application>
  </xsl:template>

  <!-- we have to copy without namespace attributes
       because otherwise we get xmlns="" in the output -->

  <xsl:template mode="copy-no-ns" match="*">
    <xsl:element name="{name(.)}">
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates mode="copy-no-ns"/>
    </xsl:element>
  </xsl:template>

</xsl:stylesheet>
