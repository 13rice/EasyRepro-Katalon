package gerico


import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.apache.poi.openxml4j.exceptions.InvalidOperationException
import org.junit.After
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ByCssSelector
import org.openqa.selenium.By.ByName
import org.openqa.selenium.By.ByXPath

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.util.KeywordUtil

import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

import java.text.MessageFormat

import org.openqa.selenium.By.ById
import org.openqa.selenium.By.ByTagName


/**
 *  Roadmap
 * 
 */
public class XrmDialogPage {


	static WebDriver driver = DriverFactory.getWebDriver()

	@Keyword
	public boolean Deactivate(int thinkTime) {
		WebUI.delay(thinkTime)

		driver.switchTo().defaultContent()
		
		WebUI.switchToFrame(findTestObject('Dialog/InlineDialog_Iframe'), thinkTime)
		
		// TODO end this nightmare dialog.... picklist is identified as a nvarchar...
		
	}
}
