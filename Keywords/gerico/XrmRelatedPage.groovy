package gerico


import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.apache.poi.openxml4j.exceptions.InvalidOperationException
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
 * OpenViewPicker	[ ]
 * SwitchView		[ ]
 * Search			[x]
 * Sort				[ ]
 * OpenGridRow		[x]
 */
public class XrmRelatedPage {
	
	static WebDriver driver = DriverFactory.getWebDriver()
	
	/// <summary>
	/// Searches the specified search criteria.
	/// </summary>
	/// <param name="searchCriteria">The search criteria.</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Related.Search("F");</example>
	@Keyword
	public static boolean Search(String searchCriteria, int thinkTime)
	{
		/*
		var inputs = driver.FindElements(By.TagName("input"))
		var input = inputs.Where(x => x.GetAttribute("id").Contains("findCriteria")).FirstOrDefault()
	
		input.SendKeys(searchCriteria)
		var searchImg = driver.FindElement(By.Id(input.GetAttribute("id") + "Img"))
		searchImg?.Click()
		return true
		*/
		WebUI.delay(thinkTime)
		
		WebUI.waitForElementClickable(findTestObject("Reference/Grid/FindCriteriaRelated"), thinkTime)
		//*[@id="crmGrid_tgz_tgz_offer_tgz_quotation_offerid_findCriteria"]
		//input[starts-with(@id, 'crmGrid_') and 'findCriteria' = substring(@id, string-length(@id) - string-length('findCriteria') +1)]
		
		WebUI.clearText(findTestObject("Reference/Grid/FindCriteriaRelated"))
		WebUI.sendKeys(findTestObject("Reference/Grid/FindCriteriaRelated"), searchCriteria)
		WebUI.sendKeys(findTestObject("Reference/Grid/FindCriteriaRelated"), Keys.chord(Keys.ENTER))
		
		return true
	}
	
	/// <summary>
	/// Opens the grid record.
	/// </summary>
	/// <param name="index">The index.</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	@Keyword
	public static boolean OpenGridRow(int index, int thinkTime)
	{
		WebUI.delay(thinkTime)

		def itemsTable = driver.findElement(ByXPath.xpath(findTestObject("Reference/Grid/GridBodyTable").findPropertyValue("xpath")))
		def links = itemsTable.findElements(ByTagName.tagName("a"))

		def currentIndex = 0
		def clicked = false

		for (link in links)
		{
			def id = link.getAttribute("id")

			if (id != null && id.startsWith(findTestObject("Reference/Grid/PrimaryField").findPropertyValue("id")))
			{
				if (currentIndex == index)
				{
					//adding fix for Firefox click issue
					/*if (this.Browser.Options.BrowserType == BrowserType.Firefox)
						driver.ExecuteScript($"document.getElementById('{id}').click()")
					else*/
						link.click()

					clicked = true

					break
				}

				currentIndex++
			}
		}

		if (!clicked)
		{
		   throw new NoSuchElementException("No record with the index '" + index + "' exists.")
		}
		
		return true
	}
}
