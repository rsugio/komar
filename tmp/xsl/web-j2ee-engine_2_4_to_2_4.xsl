<?xml version="1.0" encoding="UTF-8"?>
<!--Author Violeta Georgieva-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output indent="yes" method="xml" encoding="UTF-8"/>
	<xsl:template match="/web-j2ee-engine">
		<web-j2ee-engine>
			<xsl:attribute name="xsi:noNamespaceSchemaLocation">web-j2ee-engine.xsd</xsl:attribute>
			<xsl:apply-templates/>
		</web-j2ee-engine>
	</xsl:template>
	<xsl:template match="*">
		<xsl:if test="'security-role-map' != name(.)">
			<xsl:copy-of select="."/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="security-role-map">
		<xsl:choose>
			<xsl:when test="count(./role-name)!=0 and count(./server-role-name)!=0">
				<security-role-map>
					<xsl:for-each select="role-name">
						<role-name>
							<xsl:value-of select="."/>
						</role-name>
					</xsl:for-each>
					<xsl:for-each select="server-role-name">
						<server-role-name>
							<xsl:value-of select="."/>
						</server-role-name>
					</xsl:for-each>
				</security-role-map>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
