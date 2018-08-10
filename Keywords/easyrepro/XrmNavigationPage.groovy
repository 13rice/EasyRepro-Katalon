package easyrepro

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

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
 * OpenMenu			[x]
 * OpenSubArea		[x]
 * OpenSubMenu		[x]
 * OpenRelated		[x]
 *
 * @author 13rice
 *
 */
public class XrmNavigationPage {


	static WebDriver driver = DriverFactory.getWebDriver()

	/// <summary>
	/// Opens the Menu
	/// </summary>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	@Keyword
	public static Map<String, WebElement> OpenMenu(int thinkTime)
	{
		driver.switchTo().defaultContent()

		WebUI.delay(thinkTime);

		def dictionary = [:]

		def TestObject topLevelItem = findTestObject("Reference/Navigation/TopLevelItem")
		def String className = topLevelItem.findPropertyValue("class")

		if (driver.findElements(ByClassName.className(className)).size() > 0)
		{
			WebUI.click(findTestObject("Reference/Navigation/HomeTab"))

			WebUI.delay(thinkTime);

			def element = driver.findElement(ByXPath.xpath(findTestObject("Reference/Navigation/ActionGroup").findPropertyValue("xpath")))
			def subItems = element.findElements(ByClassName.className(findTestObject("Reference/Navigation/ActionButtonContainer").findPropertyValue("css")))

			for (def subItem in subItems)
			{
				dictionary.put(subItem.getAttribute("title").toLowerCase(), subItem)
			}
		}

		return dictionary
	}


	/// <summary>
	/// Opens the Sub Area
	/// </summary>
	/// <param name="area">The area you want to open</param>
	/// <param name="subArea">The subarea you want to open</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Navigation.OpenSubArea("Sales", "Opportunities");</example>
	@Keyword
	public static boolean OpenSubArea(String area, String subArea, int thinkTime)
	{
		WebUI.delay(thinkTime)

		area = area.toLowerCase();
		subArea = subArea.toLowerCase()

		def areas = OpenMenu(thinkTime)

		if (!areas.containsKey(area))
		{
			KeywordUtil.markError(MessageFormat.format("No area with the name '{0}' exists.", area))
		}

		def subAreas = OpenSubMenu(areas[area], thinkTime);

		if (!subAreas.containsKey(subArea))
		{
			KeywordUtil.markError(MessageFormat.format("No subarea with the name '{0}' exists inside of '{1}'.", subArea, area))
		}

		subAreas[subArea].click()

		XrmPage.SwitchToContent()
		//driver.WaitForPageToLoad()

		return true
	}

	/// <summary>
	/// Opens the Related Menu
	/// </summary>
	/// <param name="relatedArea">The Related area</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Navigation.OpenRelated("Cases");</example>
	@Keyword
	public static boolean OpenRelated(String relatedArea, int thinkTime)
	{
		WebUI.delay(thinkTime)

		// TabNode located in default content
		driver.switchTo().defaultContent()
		WebUI.click(findTestObject("Reference/Navigation/TabNode"))

		def WebElement element = driver.findElement(ByXPath.xpath(findTestObject("Reference/Navigation/ActionGroup").findPropertyValue("xpath")))
		def subItems = element.findElements(ByClassName.className("nav-rowBody"))

		// C# var related = subItems.Where(x => x.Text == relatedArea).FirstOrDefault()
		def related = null
		for (item in subItems)
		{
			if (item.text == relatedArea)
			{
				related = item
				break
			}
		}

		if (related == null)
		{
			throw new NoSuchElementException("No relatedarea with the name '" + relatedArea + "' exists.")
		}
		else
		{
			// C# Browser.ActiveFrameId = related.GetAttribute("id").Replace("Node_nav", "area")
			related.click()

			// Important : retrieve the id before switching the frame, otherwise the id of "related" can't be found
			def String frameId = related.getAttribute("id").replace("Node_nav", "area") + "Frame"

			WebUI.delay(thinkTime)

			// Switch to the related grid
			XrmPage.SwitchToContent()

			driver.switchTo().frame(frameId)
		}

		return true
	}

	private static Map<String, WebElement> OpenSubMenu(WebElement area, int thinkTime)
	{
		def dictionary = [:]

		//driver.WaitUntilVisible(ById.id(area.getAttribute("Id")))

		// Opens the sub menu
		area.click()

		WebUI.delay(1)

		WebUI.waitForElementPresent(findTestObject("Reference/Navigation/SubActionGroupContainer"), thinkTime)

		def WebElement subNavElement = driver.findElement(ByXPath.xpath(findTestObject("Reference/Navigation/SubActionGroupContainer").findPropertyValue("xpath")))

		// Retrieves all items of the sub menu
		def subItems = subNavElement.findElements(ByClassName.className(findTestObject("Reference/Navigation/SubActionElementClass").findPropertyValue("class")))

		for (subItem in subItems)
		{
			if(!subItem.text.isEmpty())
				dictionary.put(subItem.text.toLowerCase(), subItem)
		}

		return dictionary;
	}
}
