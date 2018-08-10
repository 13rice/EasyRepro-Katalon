package easyrepro

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

import internal.GlobalVariable

import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

import java.text.MessageFormat

import org.openqa.selenium.By.ById
import org.openqa.selenium.By.ByTagName

/**
 * Roadmap
 * OpenViewPicker	[ ]
 * SwitchView		[ ]
 * OpenChart		[ ]
 * CloseChart		[ ]
 * Pin				[ ]
 * Search			[x]
 * Sort				[ ]
 * OpenRecord		[x]
 * SelectRecord		[ ]
 * FilterByLetter	[ ]
 * FilterByAll		[ ]
 * EnableFilter		[ ]
 * SwitchChart		[ ]
 * 
 * @author 13rice
 *
 */
public class XrmGridPage {


	static WebDriver driver = DriverFactory.getWebDriver()

	/// <summary>
	/// Searches the specified search criteria.
	/// </summary>
	/// <param name="searchCriteria">The search criteria.</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Grid.Search("Test API")</example>
	@Keyword
	public static boolean Search(String searchCriteria, int thinkTime)
	{
		WebUI.delay(thinkTime)

		WebUI.waitForElementClickable(findTestObject("Reference/Grid/FindCriteria"), thinkTime)

		WebUI.clearText(findTestObject("Reference/Grid/FindCriteria"))
		WebUI.sendKeys(findTestObject("Reference/Grid/FindCriteria"), searchCriteria)
		WebUI.sendKeys(findTestObject("Reference/Grid/FindCriteria"), Keys.chord(Keys.ENTER))

		return true
	}

	/*
	 /// <summary>
	 /// Sorts the specified column name.
	 /// </summary>
	 /// <param name="columnName">Name of the column.</param>
	 /// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	 /// <example>xrmBrowser.Grid.Sort("Account Name")</example>
	 public static boolean Sort(string columnName,int thinkTime = Constants.DefaultThinkTime)
	 {
	 Browser.ThinkTime(thinkTime)
	 def sortCols = driver.FindElements(By.ClassName(Elements.CssClass[Reference.Grid.SortColumn]))
	 def sortCol = sortCols.FirstOrDefault(x => x.Text == columnName)
	 if (sortCol == null)
	 throw new InvalidOperationException($"Column: {columnName} Does not exist")
	 else
	 sortCol.Click()
	 return true
	 }*/

	/// <summary>
	/// Opens the grid record.
	/// </summary>
	/// <param name="index">The index.</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Grid.OpenRecord(0)</example>
	@Keyword
	public static boolean OpenRecord(int index, int thinkTime)
	{
		WebUI.delay(thinkTime)

		def rowType = WebUI.getAttribute(findTestObject("Reference/Grid/FirstRow"),  "otypename")
		//def rowType = driver.FindElement(By.XPath(Elements.Xpath[Reference.Grid.FirstRow])).GetAttribute("otypename")

		def itemsTable = driver.findElement(ByXPath.xpath(findTestObject("Reference/Grid/GridBodyTable").findPropertyValue("xpath")))
		def links = itemsTable.findElements(ByTagName.tagName("a"))

		def currentIndex = 0
		def clicked = false

		for(link in links)
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

		if (clicked)
		{
			if (rowType != "report")
			{
				/*SwitchToContent()
				 driver.WaitForPageToLoad()
				 driver.WaitUntilClickable(By.XPath(Elements.Xpath[Reference.Entity.Form]))*/
			}
			return true
		}
		else
		{
			throw new InvalidOperationException("No record with the index '" + index + "' exists.")
		}

		return true
	}
}
