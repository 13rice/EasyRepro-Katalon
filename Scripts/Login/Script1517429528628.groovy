import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as MobileBuiltInKeywords
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WSBuiltInKeywords
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUiBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.By.ById as ById
import org.openqa.selenium.WebDriver as WebDriver

if (!(GlobalVariable.isBrowserOpened)) {
    WebUI.openBrowser('')

    WebUI.maximizeWindow()

    WebUI.navigateToUrl(GlobalVariable.baseUrl)

    GlobalVariable.isBrowserOpened = true
}

boolean loginPage = DriverFactory.getWebDriver().findElements(ById.id(findTestObject('LoginPage/txtemail').findPropertyValue(
            'id'))).size() > 0

if (GlobalVariable.notLogged && loginPage) {
    WebUiBuiltInKeywords.click(findTestObject('LoginPage/txtemail'))

    WebUiBuiltInKeywords.sendKeys(findTestObject('LoginPage/txtemail'), GlobalVariable.login)

    WebUiBuiltInKeywords.click(findTestObject('LoginPage/btnNext'))

    WebUI.delay(2)

    WebUiBuiltInKeywords.waitForElementClickable(findTestObject('LoginPage/txtPassword'), GlobalVariable.delay)

    WebUiBuiltInKeywords.click(findTestObject('LoginPage/txtPassword'))

    WebUiBuiltInKeywords.setEncryptedText(findTestObject('LoginPage/txtPassword'), GlobalVariable.password)

    WebUiBuiltInKeywords.click(findTestObject('LoginPage/btnNext'))

    WebUiBuiltInKeywords.waitForElementVisible(findTestObject('LoginPage/checkDontDisplayThisMessage'), GlobalVariable.delay)

    WebUiBuiltInKeywords.click(findTestObject('LoginPage/btnNext'))

    GlobalVariable.notLogged = false

    WebUI.delay(GlobalVariable.delay)
}

