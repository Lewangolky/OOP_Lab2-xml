<?xml version="1.0" encoding="UTF-8"?>
<!--
    Regroups the flat list of knives into sub-lists by weapon type
    (Muenchian grouping, XSLT 1.0):

    <knives>                          <knives>
        <knife type="dagger"/>            <dagger>
        <knife type="saber"/>     =>          <knife type="dagger"/>
        <knife type="dagger"/>                <knife type="dagger"/>
    </knives>                             </dagger>
                                          <saber>
                                              <knife type="saber"/>
                                          </saber>
                                      </knives>
-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt"
                exclude-result-prefixes="xalan">

    <xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="4"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="knives-by-type" match="knife" use="@type"/>

    <xsl:template match="/knives">
        <knives>
            <!-- the first knife of every type represents its group -->
            <xsl:for-each select="knife[generate-id() = generate-id(key('knives-by-type', @type)[1])]">
                <xsl:element name="{@type}">
                    <xsl:apply-templates select="key('knives-by-type', @type)"/>
                </xsl:element>
            </xsl:for-each>
        </knives>
    </xsl:template>

    <!-- copies an element without the namespace declarations of the source root (xsi) -->
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>
