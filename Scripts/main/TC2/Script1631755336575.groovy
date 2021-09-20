import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

import groovy.util.slurpersupport.GPathResult

ResponseObject response = WS.sendRequest((RequestObject)findTestObject('data'))

println "response.getResponseText() : " + response.getResponseText()

GPathResult envelope = new XmlSlurper().parseText(response.getResponseText())
                .declareNamespace(env: "http://schemas.xmlsoap.org/soap/envelope/",
					wsse: "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
					wsu: "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd",
					ns: "beep", ns2: "bop", ns1: "foo")

assert envelope.'env:Header'.'wsse:Security'.'wsu:Timestamp'.'wsu:Created'.text().contains('2021-09-15T19:23:13.688Z')

assert envelope.'env:Body'.'ns:PreAuthorizeResponse'.'ns:Receipt'.'ns1:TransactionResult'.text().contains('Approved')
