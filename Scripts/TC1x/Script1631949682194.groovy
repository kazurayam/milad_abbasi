import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS

ResponseObject response = WS.sendRequest((RequestObject)findTestObject('data'))

// println response.getResponseText()

/**
 * The contet text in the XML contains redundant white spaces and new lines 
<code>
  &lt;ns1:TransactionResult>Approved
        &lt;/ns1:TransactionResult>
</code>
 * therefore we need to be tolerant for the whitespaces
 */
CustomKeywords."com.kazurayam.ks.webservice.Keywords.verifySOAPBodyElementText"(response, 
	'PreAuthorizeResponse.Receipt.TransactionResult', 'prove',
	true, FailureHandling.CONTINUE_ON_FAILURE)   // will fail

CustomKeywords."com.kazurayam.ks.webservice.Keywords.verifySOAPBodyElementText"(response,
	'PreAuthorizeResponse.Receipt.TransactionResult', 'proof',
	false, FailureHandling.CONTINUE_ON_FAILURE)  // will fail

CustomKeywords."com.kazurayam.ks.webservice.Keywords.verifySOAPBodyElementText"(response,
	'PreAuthorizeResponse.Receipt.TransactionResult', 'prove',
	false, FailureHandling.CONTINUE_ON_FAILURE)  // will pass

CustomKeywords."com.kazurayam.ks.webservice.Keywords.verifySOAPBodyElementText"(response,
	'PreAuthorizeResponse.Receipt.TransactionResult', 'Approved',
	false, FailureHandling.CONTINUE_ON_FAILURE)  // will pass

CustomKeywords."com.kazurayam.ks.webservice.Keywords.verifySOAPBodyElementText"(response,
	'foo.bar', 'baz',
	true, FailureHandling.CONTINUE_ON_FAILURE)  // will throw exception
