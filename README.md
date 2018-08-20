# Conversion of EasyRepro from C# / Selenium to Groovy for Katalon Studio
In this repository you will find a starter projet with some samples to use **EasyRepro within Katalon Studio**. 
## EasyRepro
First of all [EasyRepro](https://github.com/Microsoft/EasyRepro) is a set of methods made by Microsoft for web testing of Dynamics 365 with C# and Selenium library. It's available from github. Unfortunately it's only code based without any UI, so it targets dev essentially.
## Katalon Studio
Katalon is a free tool for webtesting based on Selenium and Groovy code, you can download it [here](https://www.katalon.com/). In Katalon you can easily construct Tests cases, Tests Suites, Reports with a UI. There's a set of natives functions, named Keywords, and you can extend them with custom Keywords.

## First Sample - Create a new account
From Katalon Studio, go to **Tests Cases > New > Test Case**
1. First step, use the native function Call Test Case, Object : Login, with no inputs
2. Add Custom Keyword, **easyrepro.XrmEntityPage.OpenCreateForm**, inputs : "account" and 
- **entityName** : "account"
- **thinkTime** : 2
3. Add > Custom Keyword, **easyrepro.XrmEntityPage.SwitchToContent** (switch to the content frame, allows access to the fields in the form)
4. Edit some fields, name for example, Add > Custom Keyword, **easyrepro.XrmPage.SetValue**
- **Object** : name
- **Input** : "Katalon account !"
5. To finish, save the account with, Add > Custom Keyword, **easyrepro.XrmEntityPage.Save**, no object, input :
- **thinkTime** : 2
- **timeOut** : 120
- **waitForSave** : true
6. Last step, configure once your global variables with baseUrl, login and password (encrypted).

Here we go ! you can now **Launch your test case** in Katalon with your favorite browser *.
*Login Test Case is not working in headless browser
## Files in Katalon from EasyRepro
### ElementReference > Objects
XPath constants used in EasyRepro are translated in Object for Katalon with the same hierarchy.
- Reference in EasyRepro :
```csharp
Elements.Xpath[Reference.Frames.ContentPanel])
```
- Same Reference in Katalon :
```groovy
findTestObject("Reference/Frames/ContentPanel")
```
### Methods > Keywords
Custom keywords can differ from their equivalent in EasyRepro, for :
- Simplify the code
- Fix issues with different behaviours in Katalon
