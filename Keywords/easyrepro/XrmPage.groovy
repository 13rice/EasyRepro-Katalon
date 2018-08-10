package easyrepro

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

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
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword

import internal.GlobalVariable
import java.util.Formatter.DateTime
import org.testng.Assert
import WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ById
import org.openqa.selenium.By.ByTagName
import org.openqa.selenium.By.ByXPath
import org.junit.After
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.StaleElementReferenceException
import java.text.MessageFormat

public class XrmPage {

	static WebDriver driver = DriverFactory.getWebDriver()

	public static Boolean IsVisible(WebElement element, By by) {
		try {
			return element.findElement(by).displayed;
		}
		catch (NoSuchElementException) {
			return false;
		}
	}

	@Keyword
	public static String GetValue(TestObject to) {
		// Todo check id property not null
		return GetValueById(to.findPropertyValue("id"));
	}

	/**
	 * Get the value of a field, support :
	 * Text
	 * Picklist
	 * Lookup (lookup, owner, customer)
	 * @param fieldId
	 * @return value, empty string if not found
	 */
	@Keyword
	public static String GetValueById(String fieldId) {
		String text = "";

		if (driver.findElements(ById.id(fieldId)).size() > 0) {
			WebElement fieldElement = driver.findElement(ById.id(fieldId))

			if (fieldElement.findElements(ByTagName.tagName("textarea")).size() > 0) {
				text = fieldElement.findElement(ByTagName.tagName("textarea")).getAttribute("value");
			}
			else {
				// Picklist
				if (fieldElement.getAttribute("class").contains("picklist"))
				{
					KeywordUtil.logInfo("GetValue of picklist")

				}
				else if (fieldElement.getAttribute("class").contains("lookup")
				|| fieldElement.getAttribute("class").contains("owner")
				|| fieldElement.getAttribute("class").contains("customer"))
				{
					// Lookup ou champ responsable (owner)
					KeywordUtil.logInfo("GetValue of lookup or owner")
					text = fieldElement.text
				}
				else
				{
					// Nvarchar, decimal etc.
					KeywordUtil.logInfo("GetValue of text")
					text = fieldElement.findElement(ByTagName.tagName("input")).getAttribute("value");
				}

			}
		}
		else
			KeywordUtil.markError("Field: " + fieldId + " Does not exist")

		KeywordUtil.logInfo(MessageFormat.format("Value found {0} of field {1}", text, fieldId))
		return text;
	}

	@Keyword
	public static boolean SetValue(TestObject to, String value)
	{
		String fieldId = to.findPropertyValue("id")

		if (driver.findElements(ById.id(fieldId)).size() > 0)
		{
			WebElement fieldElement = driver.findElement(ById.id(fieldId))
			WebUI.waitForElementVisible(to, Constants.WaitTimeOut)

			if (IsVisible(fieldElement, ByTagName.tagName("a")))
			{
				JavascriptExecutor js = (JavascriptExecutor)driver;
				WebElement element = fieldElement.findElement(ByTagName.tagName("a"));
				js.executeScript("arguments[0].setAttribute('style', 'pointer-events: none; cursor: default')", element);
			}

			if (!fieldElement.displayed)
			{
				// Not visible
				KeywordUtil.markError("XRM : " + fieldId + " not visible")
				return false
			}

			try
			{
				//Check to see if focus is on field already
				if (fieldElement.findElement(ByClassName.className("ms-crm-Inline-Edit")) != null)
					fieldElement.findElement(ByClassName.className("ms-crm-Inline-Edit")).click();
				else
					fieldElement.findElement(ByClassName.className("ms-crm-Inline-Value")).click();
			}
			catch (NoSuchElementException) { }

			if (fieldElement.findElements(ByTagName.tagName("textarea")).size() > 0)
			{
				KeywordUtil.logInfo(MessageFormat.format("XRM : Click on field {0}", fieldId))
				fieldElement.click();

				KeywordUtil.logInfo(MessageFormat.format("XRM : Setting value {0} to field {1}", value, fieldId))
				fieldElement.findElement(ByTagName.tagName("textarea")).clear();
				fieldElement.findElement(ByTagName.tagName("textarea")).sendKeys(value);
			}
			else if(fieldElement.tagName =="textarea")
			{
				KeywordUtil.logInfo(MessageFormat.format("XRM : Click on field {0}", fieldId))
				fieldElement.click();

				KeywordUtil.logInfo(MessageFormat.format("XRM : Setting value {0} to field {1}", value, fieldId))
				fieldElement.clear();
				fieldElement.sendKeys(value);
			}
			else
			{
				KeywordUtil.logInfo(MessageFormat.format("XRM : Setting value {0} to field {1}", value, fieldId))
				//BugFix - Setvalue -The value is getting erased even after setting the value ,might be due to recent CSS changes.
				//driver.ExecuteScript("Xrm.Page.getAttribute('" + field + "').setValue('')");

				if (fieldElement.getAttribute("class").contains("picklist"))
				{
					// Picklist
					KeywordUtil.logInfo("XRM : Picklist spotted")

					// Open the picklist
					KeywordUtil.logInfo(MessageFormat.format("XRM : Click on field {0}", fieldId))
					fieldElement.click()

					// Retrieve the options
					def WebElement select = fieldElement.findElement(ByTagName.tagName("select"))
					def List<WebElement> options = select.findElements(ByTagName.tagName("option"))

					for (op in options)
					{
						if (op.getAttribute("title") == value)
						{
							KeywordUtil.logInfo(MessageFormat.format("XRM : Picklist select {0}", value))
							op.click()
						}
					}

				}
				else if (fieldElement.getAttribute("class").contains("lookup") ||
				fieldElement.getAttribute("class").contains("owner") ||
				fieldElement.getAttribute("class").contains("customer"))
				{
					SetValueLookup(fieldElement, fieldId, value)
				}
				else
				{
					WebUI.waitForElementClickable(to, Constants.WaitTimeOut)

					KeywordUtil.logInfo(MessageFormat.format("XRM : Click on field {0}", fieldId))
					fieldElement.click();

					// Text
					def WebElement input = fieldElement.findElement(ByTagName.tagName("input"))
					input.clear();
					input.sendKeys(value);
				}
			}
		}
		else
		{
			KeywordUtil.markError("Field: " + fieldId + " Does not exist")
			return false;
		}

		return true;
	}

	/**
	 * Setting the value for a lookup, not easy =(
	 * @return
	 */
	public static boolean SetValueLookup(WebElement lookup, String lookupId, String value)
	{
		def String currentValue = GetValueById(lookupId)
		def WebElement input = null

		// Hard one, have to remove the value before setting the new one.
		if (!currentValue.empty)
		{
			// The label must be visible
			def String labelId = lookupId + "_cl"
			if (driver.findElements(ById.id(labelId)).size() > 0)
			{
				// Click on the label to make the lookup visible
				def WebElement label = driver.findElement(ById.id(labelId))
				label.click()

				// Send keys to activate the inpu
				def WebElement lookupDiv = lookup.findElement(ById.id(lookupId + "_lookupDiv"))
				lookupDiv.sendKeys(Keys.BACK_SPACE)
			}
			else
			{
				KeywordUtil.markError("Label lookup field: " + labelId + " must be visible to remove the lookup value")
				return false;
			}
		}
		else
		{
			KeywordUtil.logInfo(MessageFormat.format("XRM : Click on field {0}", lookupId))
			lookup.click();
		}

		// Catch the input
		input = lookup.findElement(ByTagName.tagName("input"))
		def WebElement
		input.clear();
		input.sendKeys(value)

		KeywordUtil.logInfo("XRM : Lookup spotted")

		// First value found from the list of possible values if empty string
		if (!value.empty)
		{
			input.sendKeys(Keys.ENTER);

			// Lookup search, select first element, TODO : improve that
			WebUI.delay(1)
			def String lookupDlgId = "Dialog_" + lookupId + "_IMenu";
			if (driver.findElements(ById.id(lookupDlgId)).size() > 0)
			{
				def WebElement lookupSearchDlg = driver.findElement(ById.id(lookupDlgId))
				if (lookupSearchDlg.findElements(ByTagName.tagName("a")).size() > 0)
				{
					//
					// 0 index is not visible
					def WebElement valueFound = lookupSearchDlg.findElements(ByTagName.tagName("a"))[1]

					if (valueFound.getAttribute("title").startsWith("Enregistrements introuvables."))
					{
						input.sendKeys(Keys.ESCAPE);
						KeywordUtil.markError("No Value found for lookup " + lookupId + ", value : " + value)
						return false;
					}
					else
					{
						valueFound.click()
					}
				}
			}
		}

		return true;
	}

	private static boolean RemoveLookupValue(String lookupId)
	{

	}

	/**
	 * Check if there's a popup error
	 * @return
	 */
	@Keyword
	public static boolean VerifyError(int thinkTime)
	{
		driver.switchTo().defaultContent()

		WebUI.delay(thinkTime)

		TestObject to = findTestObject("Dialog/InlineDialog")
		String divId = to.findPropertyValue("id")

		return (driver.findElements(ById.id(divId)).size() > 0)
	}


	/**
	 * 
	 * @return
	 */
	@Keyword
	public static boolean SwitchToContent()
	{
		driver.switchTo().defaultContent()

		def TestObject contentPanel = findTestObject("Reference/Frames/ContentPanel")

		//wait for the content panel to render
		WebUiBuiltInKeywords.waitForElementPresent(contentPanel, 0)

		//find the crmContentPanel and find out what the current content frame ID is - then navigate to the current content frame
		def String currentContentFrame = WebUI.getAttribute(contentPanel, findTestObject("Reference/Frames/ContentFrameId").findPropertyValue("id"))

		driver.switchTo().frame(currentContentFrame)

		return true;
	}
}
