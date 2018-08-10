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
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

public class Reference {

	
	/*def sampleMap =
  [
	  //Frames
	  "Frame_ContentFrameId" : "currentcontentid",
	  "Frame_DialogFrameId" : "InlineDialog[INDEX]_Iframe",
	  "Frame_QuickCreateFrameId" : "NavBarGloablQuickCreate",
	  "Frame_WizardFrameId" : "wizardpageframe",
	  "Frame_ViewFrameId" : "ViewArea",
	
	  //SetValue
	  "SetValue_ConfirmId" : "_compositionLinkControl_flyoutLoadingArea-confirm",
	  "SetValue_FlyOutId" : "_compositionLinkControl_flyoutLoadingArea_flyOut",
	  "SetValue_CompositionLinkControlId" : "_compositionLinkControl_",
	       
	  //Dialogs
	  "Dialog_ActualRevenue" : "actualrevenue_id",
	  "Dialog_CloseDate" : "closedate_id",
	  "Dialog_Description" : "description_id",
	  "Dialog_UserOrTeamLookupId" : "systemuserview_id",
	
	  //Add Connection
	  "Dialog_ConnectionDescription" : "description",
	
	  //Entity
	  "Entity_TabId" : "[NAME]_TAB_header_image_div",
	
	  //GuidedHelp
	  "GuidedHelp_Close" : "closeButton",
	
	  //Grid
	  "Grid_PrimaryField" : "gridBodyTable_primaryField_",
	
	  //Global Search
	  "Search_EntityNameId" : "entityName",
	  "Search_RecordNameId" : "attribone",
	  "Search_EntityContainersId" : "entitypic",
	
	  //ActivityFeed
	  "Notes_ActivityPhoneCallDescId" : "quickCreateActivity4210controlId_description",
	  "Notes_ActivityPhoneCallVoiceMailId" : "PhoneCallQuickformleftvoiceCheckBoxContol",
	  "Notes_ActivityPhoneCallDirectionId" : "quickCreateActivity4210controlId_directioncode_i",
	  "Notes_ActivityTaskSubjectId"     : "quickCreateActivity4212controlId_subject",
	  "Notes_ActivityTaskDescriptionId"     : "quickCreateActivity4212controlId_description",
	  "Notes_ActivityTaskPriorityId"     : "quickCreateActivity4212controlId_prioritycode_i",
	  "Notes_ActivityAddTaskDueTimeId"   :"selectTable_Date",
	  "Notes_ActivityAddTaskDueDateId"   :"quickCreateActivity4212controlId_scheduledend",
	
	  //Process
	  "Process_Category": "WorkflowCategory",
	  "Process_Entity": "PrimaryEntity"]*/
}
