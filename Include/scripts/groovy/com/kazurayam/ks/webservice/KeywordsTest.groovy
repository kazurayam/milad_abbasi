package com.kazurayam.ks.webservice

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.configuration.RunConfiguration
import internal.GlobalVariable
import com.kazurayam.ks.webservice.Keywords
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*

import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import groovy.util.slurpersupport.GPathResult
import groovy.util.XmlNodePrinter
import java.nio.file.Path
import java.nio.file.Paths
import groovy.util.XmlSlurper

public class KeywordsTest {
	
	Path projectDir
	String xml 
	
	@Before
	void setup() {
		projectDir = Paths.get(RunConfiguration.getProjectDir())
		Path dataFile = projectDir.resolve("Include/web/data.xml")
		xml = dataFile.toFile().text
	}
	
	@Test
	void test_toXmlString() {
		GPathResult root = new XmlSlurper().parseText(xml)
		String out = Keywords.toXmlString(root)
		println out
	}
}
