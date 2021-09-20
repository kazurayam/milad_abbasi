import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

ResponseObject response = WS.sendRequest((RequestObject)findTestObject('data'))

println response.getResponseBodyContent()

//println response.getResponseText()

WS.verifyElementText(response, 'PreAuthorizeResponse.Receipt.TransactionResult', 'Approved')
