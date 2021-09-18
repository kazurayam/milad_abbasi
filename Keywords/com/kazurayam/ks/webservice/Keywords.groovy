package com.kazurayam.ks.webservice

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.util.KeywordUtil
import groovy.util.IndentPrinter
import groovy.util.slurpersupport.GPathResult
import groovy.util.XmlNodePrinter


public class Keywords {

	/**
	 *
	 * @param response an instance of com.kms.katalon.core.testobject.ResponseObject. 
	 * The response argument is expected to be a XML document. Otherwise an exception will be thrown.
	 * The response argument is expected to be an SOAP Envelop document. Otherwise an exception will be thrown.
	 * The SOAP Envelop document must contain an Body element. Otherwise an exception will be thrown.
	 * 
	 * @param locator GPath locator. This keyword will fist select the SOPA Body element and then apply the locator expression to find the target element.
	 * For example, when the response document is like this:
	 * <code>
	 * &lt;env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="bar">
	 *   &lt;env:Header>
	 *     &lt;foo>0&lt;/foo>
	 *   &lt;/env:HEADER
	 *   &lt;env:Body>
	 *     &lt;ns:PreAuthorizeResponse>
	 *       &lt;ns:Receipt>
	 *         &lt;ns1:DataKey>1234&lt;/ns1:DataKey>
	 *       &lt;/ns:Receipt>
	 *     &lt;/ns:PreAuthorizeResponse>
	 *   &lt;/env:Body>
	 * &lt;/env:Envelope>
	 * </code>
	 * and when we hant to verify if the `ns:DataKey` has the contet text `1234`, then the locator should be
	 * <code>PreAuthoriizeResponse.Recept.DataKey<code>
	 * The locator should start with the immediate child node of `env:Body` element.
	 * You shouldn't include XML Namespace prefixes ('ns') in the locator.
	 * 
	 * @param expected the string content which you expect the target element to have. 
	 * 
	 * @param requireExactMatch optional, default to true.
	 * If true, the XML content text and the expected string will be compared by the == operator.
	 * If false, the XML content text and the expected string will be compared loosely. The keyword will
	 * trim() and normalize white spaces (0x20, CR, LF, TAB) in the XML content text. And the keyword 
	 * performs partial match. E.g, if the XML content text is 
	 * <code>
	 * &lt;DataKey>
	 *   0 1234 56
	 * &lt;/Data> 
	 * </DataKey>
	 * then the expected `1234` will match and return true, as the content text contains `1234` as its part.
	 * 
	 * @param flowControl optional, default to the Failure Handling setting given in the settings.
	 * 
	 * @author kazurayam
	 */
	@Keyword
	static boolean verifySOAPBodyElementText(ResponseObject response, 
			String locator, String expected, boolean requireExactMatch = true, 
			FailureHandling flowControl = RunConfiguration.getDefaultFailureHandling()) {
		Objects.requireNonNull(response)
		Objects.requireNonNull(locator)
		Objects.requireNonNull(expected)
		Objects.requireNonNull(flowControl)
		if (response.isXmlContentType()) {
			GPathResult root = new XmlSlurper().parseText(response.getResponseText())
								.declareNamespace(env: "http://schemas.xmlsoap.org/soap/envelope/")
			if (root.name() == 'Envelope') {
				GPathResult body = root.'env:Body'
				if (body != null) {
					String nodeText = getXmlNodeText(body, locator)
					if (nodeText == null || nodeText == '') {
						stepFailed("the node ${toXmlString(body)} does not have an ancestor node that matches the locator \"${locator}\"", flowControl)
					}
					// println "nodeText: \"${nodeText}\", expected: \"${expected}\", requireExactMatch: ${requireExactMatch}"
					if (requireExactMatch) {
						boolean b = (nodeText == expected)
						if (!b) {
							stepFailed("the node at \"${locator}\" has a text \"${nodeText}\", " + 
								"which is not equal to the expected string \"${expected}\"", flowControl)
						}
						return b
					} else {
						boolean b = (nodeText.trim().replaceAll("\\s+", " ").contains(expected))
						if (!b) {
							stepFailed("the node at \"${locator}\" has a text \"${nodeText}\", " + 
								"which does not contain the expected string \"${expected}\"", flowControl)
						}
						return b
					}
				} else {
					stepFailed("response text is a SOAP Envelope but has not Body element", flowControl)	
				}
			} else {
				stepFailed("response text is not a SOAP Envelope", flowControl)
			}
		} else {
			stepFailed("response text is not a XML", flowControl)
		}
	}
	
	static String getXmlNodeText(GPathResult source, String locator) {
		GPathResult node = source
		def paths = locator.split("\\.")
		for (String child : paths) {
			node = node[child]
		}
		return node.text()
	}
	
	static String toXmlString(GPathResult gpr) {
		StringWriter sw = new StringWriter()
		IndentPrinter ip = new IndentPrinter(sw, "  ")
		XmlNodePrinter xnp = new XmlNodePrinter(ip).setPreserveWhitespace(true)
		xnp.print(gpr)
		return sw.toString()
	}
	
	static def stepFailed(String message, FailureHandling flowControl) {
		if (flowControl == FailureHandling.OPTIONAL) {
			//println "#stepFailed('${message}',FailureHandling.OPTIONAL)"
			KeywordUtil.logInfo(message)
		} else if (flowControl == FailureHandling.CONTINUE_ON_FAILURE) {
			//println "#stepFailed('${message}',FailureHandling.CONTINUE_ON_FAILURE)"
			KeywordUtil.markFailed(message)
		} else {
			// in the case where flowControl == FailureHandling.STOP_ON_FAILURE
			//println "#stepFailed('${message}',FailureHandling.STOP_ON_FAILURE)"
			KeywordUtil.markFailedAndStop(message)
		}
	}
}
