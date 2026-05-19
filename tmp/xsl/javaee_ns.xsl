<?xml version="1.0" encoding="UTF-8"?>

<!--
  This stylesheet changes the namespace of any document to "http://java.sun.com/xml/ns/javaee"
  Author: Jan Sievers
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                
  <xsl:output indent="yes" method="xml" encoding="UTF-8"/> 

  <xsl:template match="/*">
    <xsl:element name="{name(.)}" namespace="http://java.sun.com/xml/ns/javaee">
      <xsl:copy-of select="@*"/>
	    <xsl:for-each select="*">
	      <xsl:apply-templates select="." mode="copy-no-ns"/> 
	    </xsl:for-each>
    </xsl:element>
  </xsl:template>

  <!-- we have to copy without namespace attributes
       because otherwise we get the original xmlns in the output -->

  <xsl:template mode="copy-no-ns" match="*">
    <xsl:element name="{name(.)}">
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates mode="copy-no-ns"/>
    </xsl:element>
  </xsl:template>

</xsl:stylesheet>
