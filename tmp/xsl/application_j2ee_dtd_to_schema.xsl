<?xml version="1.0" encoding="UTF-8"?>
   
<!-- Author: Luchesar Cekov -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output indent="yes" method="xml" encoding="UTF-8"/>
	<xsl:template match="/application-j2ee-engine">
		<application-j2ee-engine xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="application-j2ee-engine.xsd">
			<xsl:for-each select="reference[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>
			<xsl:for-each select="library-loader[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>
			<xsl:for-each select="classpath[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>
			<xsl:for-each select="provider-name[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>
			<xsl:for-each select="modules-additional[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>
			
			<xsl:for-each select="fail-over-enable">
				<xsl:choose>
					<xsl:when test="@mode!='disable'">					
						<xsl:element name="fail-over-enable">
							<xsl:attribute name="mode">
								<xsl:value-of select="@mode"/>
							</xsl:attribute>
							<xsl:if test="@scope">
								<xsl:attribute name="scope">
									<xsl:value-of select="@scope"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="@delta">
								<xsl:attribute name="delta">
									<xsl:value-of select="@delta"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="xsi:type" namespace="http://www.w3.org/2001/XMLSchema-instance">fail-over-enableType_enable</xsl:attribute>
						</xsl:element>					
					</xsl:when>				
					<xsl:otherwise> 
						<xsl:element name="fail-over-enable">
							<xsl:attribute name="mode">
								<xsl:value-of select="@mode"/>
							</xsl:attribute>							
							<xsl:attribute name="xsi:type" namespace="http://www.w3.org/2001/XMLSchema-instance">fail-over-enableType_disable</xsl:attribute>
						</xsl:element>		        	    	        	        
					</xsl:otherwise>
				</xsl:choose>			
			</xsl:for-each>         					
			
			<xsl:for-each select="start-up">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>		
			<xsl:for-each select="java-version[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>	
			<xsl:for-each select="exclude-default-reference[.!='']">
				  <xsl:apply-templates select="." mode="copy-no-ns"/>
			</xsl:for-each>	
		</application-j2ee-engine>
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