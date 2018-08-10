package gerico

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import com.sun.jndi.toolkit.url.Uri
import com.kms.katalon.core.util.KeywordUtil

import internal.GlobalVariable
import java.text.MessageFormat
import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByClassName
import org.openqa.selenium.By.ById
import org.openqa.selenium.By.ByTagName
import org.openqa.selenium.By.ByXPath
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.junit.After
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys as Keys

public class XrmEntityPage {

	static WebDriver driver = DriverFactory.getWebDriver()

	@Keyword
	public static boolean SelectForm(String name, int timeOut) {
		return SelectForm(name, timeOut, FailureHandling.STOP_ON_FAILURE)
	}

	/// <summary>
	/// Opens the Entity
	/// </summary>
	/// <param name="uri">The uri</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	@Keyword
	public static boolean OpenCreateForm(String entityName, int thinkTime)
	{
		WebUI.delay(thinkTime)

		def Uri uri = new Uri(driver.currentUrl)
		def String url = MessageFormat.format("{0}://{1}/main.aspx?etn={2}&pagetype=entityrecord", uri.scheme, uri.host, entityName)

		WebUI.navigateToUrl(url)

		DismissAlertIfPresent(false)

		WebUI.switchToDefaultContent()

		/*driver.WaitForPageToLoad();
		 driver.WaitUntilClickable(By.XPath(Elements.Xpath[Reference.Entity.Form]),
		 new TimeSpan(0, 0, 30),
		 null,
		 d => { throw new Exception("CRM Record is Unavailable or not finished loading. Timeout Exceeded"); }
		 );
		 */
		return true
	}

	/// <summary>
	/// Dismiss the Alert If Present
	/// </summary>
	/// <param name="stay"></param>
	@Keyword
	public static boolean DismissAlertIfPresent(boolean stay)
	{
		try {
			WebDriverWait wait = new WebDriverWait(driver, Constants.DefaultThinkTime)

			if(wait.until(ExpectedConditions.alertIsPresent()) != null)
			{
				if (stay)
				{
					driver.switchTo().alert().dismiss()
				}
				else
				{
					driver.switchTo().alert().accept()
				}
			}
		}
		catch (StepFailedException) { KeywordUtil.markPassed("")}

		return true
	}

	/// <summary>
	/// Selects the Form
	/// </summary>
	/// <param name="name">The name of the form</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Entity.SelectForm("Details");</example>

	@Keyword
	public static boolean SelectForm(String name, int timeOut, FailureHandling flowControl)
	{
		if (GetCurrentForm(timeOut, flowControl).compareToIgnoreCase(name))
		{
			def boolean found = false

			def TestObject to = findTestObject("EntityPage/Entity_SelectForm")
			WebUI.waitForElementPresent(to, timeOut, flowControl)
			WebUI.click(to)

			def TestObject selector = findTestObject("EntityPage/Entity_SelectFormTable")
			WebUI.waitForElementPresent(selector, timeOut, flowControl)

			def List<WebElement> items = driver.findElement(ById.id(selector.findPropertyValue("id"))).findElements(ByTagName.tagName("a"))
			for (item in items)
			{
				if (item.text == name)
				{
					KeywordUtil.logInfo("Form: " + name + " found")
					item.click()
					found = true
					break
				}
			}

			if (!found)
				KeywordUtil.markError("Form: " + name + " does not exist")

			return found;
		}

		KeywordUtil.logInfo("Already on the form : " + name + "")
		return true;
	}

	/**
	 * Return the name of the current form
	 * New, not present in EasyRepro
	 * @param timeOut
	 * @param flowControl
	 * @return
	 */
	@Keyword
	public static String GetCurrentForm(int timeOut, FailureHandling flowControl)
	{
		def TestObject to = findTestObject("EntityPage/Entity_SelectForm")
		WebUI.waitForElementPresent(to, timeOut, flowControl)

		def String formName = driver.findElement(ById.id(to.findPropertyValue("id"))).text

		// Extract the form name, removing the "Entity : "
		if (formName.indexOf(":") > -1)
		{
			formName = formName.substring(formName.indexOf(":") + 2)
		}

		return formName
	}


	/// <summary>
	/// Click add button of subgridName
	/// </summary>
	/// <param name="subgridName">The SubgridName</param>
	/// <param name="thinkTime">Used to simulate a wait time between human interactions. The Default is 2 seconds.</param>
	/// <example>xrmBrowser.Entity.ClickSubgridAddButton("Stakeholders");</example>
	@Keyword
	public static boolean ClickSubgridAddButton(String subgridName, int thinkTime = Constants.DefaultThinkTime)
	{
		// TODO failure handling
		WebUI.delay(thinkTime)

		String idBtn = subgridName + "_addImageButton"

		TestObject to = new TestObject()
		to.addProperty("id", ConditionType.EQUALS, idBtn)

		WebUI.waitForElementVisible(to, Constants.WaitTimeOut)

		driver.findElement(ById.id(idBtn)).click()

		return true;
	}

	/**
	 * Click GridView button of subgridName
	 * @param subgridName The subgridName
	 * @param thinkTime Used to simulate a wait time between human interactions. The Default is 2 seconds.
	 * @return
	 */
	@Keyword
	public static boolean ClickSubgridGridViewButton(String subgridName, int thinkTime = Constants.DefaultThinkTime)
	{
		// TODO failure handling
		driver.delay(thinkTime)

		String idBtn = subgridName + "_openAssociatedGridViewImageButtonImage"
		driver.FindElement(ById.Id(idBtn)).click()

		driver.delay(thinkTime)

		return true;
	}

	/**
	 * Closes the current entity record you are on. (test ok)
	 * @return
	 */
	@Keyword
	public static boolean CloseEntity(int thinkTime = Constants.DefaultThinkTime)
	{
		WebUI.switchToDefaultContent()

		// TODO Failure handling
		WebUI.delay(thinkTime);

		TestObject closeButton = findTestObject("Reference/Entity/Close")

		WebUI.waitForElementClickable(closeButton, thinkTime)
		WebUI.click(closeButton)

		return true
	}

	@Keyword
	public static boolean Save(int thinkTime, int timeOut, boolean waitForSave)
	{
		def boolean result = true

		// TODO Failure handling
		WebUI.delay(thinkTime)

		TestObject saveButton = findTestObject("Reference/Entity/Save")

		WebUI.waitForElementClickable(saveButton, thinkTime)
		WebUI.click(saveButton)

		if (waitForSave)
		{
			result = WaitForSave(timeOut, false)
		}

		return result
	}

	/**
	 * Custom
	 * @param timeout
	 * @return
	 */
	@Keyword
	public static boolean WaitForSave(int timeout, boolean errorExpected)
	{
		def boolean result = true

		// Switch to content panel
		XrmPage.SwitchToContent()

		def TestObject to = findTestObject("Reference/SaveFooter_statuscontrol")
		def String id = to.findPropertyValue("id")

		def WebDriverWait wait = new WebDriverWait(driver, timeout)

		if (!wait.until(new WaitForTextDifferent(driver.findElement(ById.id(id)), "enregistrement")))
		{
			KeywordUtil.markError("Save timeout")
		}
		else
		{
			if (XrmPage.VerifyError(Constants.DefaultThinkTime))
			{
				WebUI.switchToFrame(findTestObject('Dialog/InlineDialog_Iframe'), timeout)

				if (!errorExpected)
				{
					KeywordUtil.markError("Error : " + WebUI.getText(findTestObject('Dialog/ErrorMessage')))
				}

				result = false
			}
		}

		// Timeout
		return result
	}
}
