/**
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 * <p/>
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.connector.integration.test.outlookmail;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.connector.integration.test.base.ConnectorIntegrationTestBase;
import org.wso2.connector.integration.test.base.RestResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutLookMailConnectorIntegrationTest extends ConnectorIntegrationTestBase {
	private Map<String, String> esbRequestHeadersMap = new HashMap<String, String>();
	private Map<String, String> apiRequestHeadersMap = new HashMap<String, String>();

	/**
	 * Set up the environment.
	 */
	@BeforeClass(alwaysRun = true)
	public void setEnvironment() throws Exception {
		String connectorName = System.getProperty("connector_name") + "-connector-" +
				System.getProperty("connector_version") + ".zip";
		addCertificatesToEIKeyStore("client-truststore.jks", "wso2carbon");
		init(connectorName);

		esbRequestHeadersMap.put("Accept-Charset", "UTF-8");
		esbRequestHeadersMap.put("Content-Type", "application/json");
		esbRequestHeadersMap.put("Accept", "application/json");

		apiRequestHeadersMap.put("Accept-Charset", "UTF-8");
		apiRequestHeadersMap.put("Content-Type", "application/json");
		apiRequestHeadersMap.put("Accept", "application/json");

		String accessToken = connectorProperties.getProperty("accessToken");
		apiRequestHeadersMap.put("Authorization", "Bearer " + accessToken);
	}

	/**
	 * Positive test case for createFolder method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {createFolder} integration test with mandatory" + " parameters.")
	public void testCreateFolderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "createFolder_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
	}

	/**
	 * Negative test case for createFolder method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {createFolder} integration test with negative " + "case.")
	public void testCreateFolderNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "createFolder_negative.json");
		Assert.assertEquals(esbRestResponse.getBody().getJSONObject("error").getString("code"),
		                    "ErrorInvalidRequest");
	}

	/**
	 * Positive test case for getFolder method with mandatory parameters.
	 */
	@Test(enabled = true, dependsOnMethods = {
			"testCreateFolderWithMandatoryParameters" },
			description = "outlookmail {getFolder} integration test with mandatory parameters.")
	public void testGetFolderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getFolder_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/MailFolders";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getFolder method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {getFolder} integration test with negative case.")
	public void testGetFolderWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getFolder_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for getChildFolder method with mandatory parameters.
	 */
	@Test(enabled = true, dependsOnMethods = {
			"testCreateFolderWithMandatoryParameters" },
			description = "outlookmail {getChildFolder} integration test with mandatory parameters.")
	public void testGetChildFolderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getChildFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getChildFolder_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/MailFolders/" +
		                     connectorProperties.getProperty("folderId") + "/childfolders";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getChildFolder method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {getChildFolder} integration test with negative case.")
	public void testGetChildFolderWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getChildFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getFolder_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for deleteFolder method with mandatory parameters.
	 */
	@Test(enabled = true, priority = 1,
			description = "outlookmail {deleteFolder} integration test with mandatory parameters.")
	public void testDeleteFolderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteFolder_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	}

	/**
	 * Negative test case for deleteFolder method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {deleteFolder} integration test with negative " +
	                                    "case.")
	public void testDeleteFolderWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteFolder_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for moveFolder method with mandatory parameters.
	 */
	@Test(enabled = true, dependsOnMethods = {
			"testCreateFolderWithMandatoryParameters" },
			description = "outlookmail {moveFolder} integration test with mandatory parameters.")
	public void testMoveFolderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:moveFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "moveFolder_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
	}

	/**
	 * Negative test case for moveFolder method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {moveFolder} integration test with negative case.")
	public void testMoveFolderWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:moveFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "moveFolder_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for getAttachmentCollection method with mandatory parameters.
	 */
	@Test(enabled = true, dependsOnMethods = {
			"testCreateAttachmentWithMandatoryParameters" }, description =
			"outlookmail {getAttachmentCollection} integration test with mandatory " +
			"parameters.")
	public void testGetAttachmentCollectionWithMandatoryParameters()
			throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getAttachmentCollection");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getAttachmentCollection_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/messages/" +
		                     connectorProperties.getProperty("messageId") + "/attachments";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getAttachmentCollection method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {getAttachmentCollection} integration test with negative case.")
	public void testGetAttachmentCollectionWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getAttachmentCollection");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getAttachmentCollection_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for getAttachment method with mandatory parameters.
	 */
	@Test(enabled = true, dependsOnMethods = {
			"testCreateAttachmentWithMandatoryParameters" },
			description = "outlookmail {getAttachment} integration test with mandatory parameters.")
	public void testGetAttachmentMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getAttachment_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/messages/" +
		                     connectorProperties.getProperty("messageId") + "/attachments/" +
		                     connectorProperties.getProperty("attachmentId");
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getAttachment method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {getAttachment} integration test with negative case.")
	public void testGetAttachmentWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getAttachment_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for deleteAttachment method with mandatory parameters.
	 */
	@Test(enabled = true, priority = 1,
			description = "outlookmail {deleteAttachment} integration test with mandatory parameters.")
	public void testDeleteAttachmentWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteAttachment_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	}

	/**
	 * Negative test case for deleteAttachment method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {deleteAttachment} integration test with negative case.")
	public void testDeleteAttachmentWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteAttachment_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for createAttachment method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {createAttachment} integration test with mandatory parameters.")
	public void testCreateAttachmentWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "createAttachment_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
	}

	/**
	 * Negative test case for createAttachment method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {createAttachment} integration test with negative case.")
	public void testCreateAttachmentWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:createAttachment");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "createAttachment_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for getMessage method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {getMessage} integration test with mandatory parameters.")
	public void testGetMessageMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getMessage_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/messages/" +
		                     connectorProperties.getProperty("messageId");
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getMessage method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {getMessage} integration test with negative case.")
	public void testGetMessageWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getMessage_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for getMessageCollectionFromFolder method with mandatory parameters.
	 */
	@Test(enabled = true, description =
			"outlookmail {getMessageCollectionFromFolder} integration test with mandatory " +
			"parameters.")
	public void testGetMessageCollectionFromFolderWithMandatoryParameters()
			throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getMessageCollectionFromFolder");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getMessageCollectionFromFolder_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/MailFolders/" +
		                     connectorProperties.getProperty("folderId") + "/messages";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "GET", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 200);
		Assert.assertEquals(esbRestResponse.getBody().toString(),
		                    apiRestResponse.getBody().toString());
	}

	/**
	 * Negative test case for getMessageCollectionFromFolder method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {getMessage} integration test with negative case.")
	public void testGetMessageCollectionFromFolderWithNegativeCase()
			throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:getMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "getMessageCollectionFromFolder_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for deleteMessage method with mandatory parameters.
	 */
	@Test(enabled = true, priority = 2,
			description = "outlookmail {deleteMessage} integration test with mandatory parameters.")
	public void testDeleteMessageWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteMessage_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 204);
	}

	/**
	 * Negative test case for deleteMessage method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {deleteMessage} integration test with negative case.")
	public void testDeleteMessageWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:deleteMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "deleteMessage_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for forwardMessage method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {forwardMessage} integration test with mandatory parameters.")
	public void testforwardMessageWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:forwardMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "forwardMessage_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 202);
	}

	/**
	 * Negative test case for forwardMessage method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {forwardMessage} integration test with negative " +
	                                    "case.")
	public void testforwardMessageWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:forwardMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "forwardMessage_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for moveMessage method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {moveMessage} integration test with mandatory parameters.")
	public void testMoveMessageWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:moveMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "moveMessage_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 201);
	}

	/**
	 * Negative test case for moveMessage method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {moveMessage} integration test with negative case.")
	public void testMoveMessageWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:moveMessage");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "moveMessage_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for replyToAll method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {replyToAll} integration test with mandatory parameters.")
	public void testReplyToAllWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:replyToAll");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "replyToAll_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/messages/" +
		                     connectorProperties.getProperty("messageId") + "/replyall";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "POST", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 202);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 202);
	}

	/**
	 * Negative test case for replyToAll method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {moveMessage} integration test with negative " +
	                                    "case.")
	public void testReplyToAllWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:replyToAll");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "replyToAll_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for replyToSender method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {replyToSender} integration test with mandatory parameters.")
	public void testReplyToSenderWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:replyToSender");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "replyToSender_mandatory.json");

		String apiEndPoint = connectorProperties.getProperty("apiUrl") + "/" +
		                     connectorProperties.getProperty("apiVersion") + "/me/messages/" +
		                     connectorProperties.getProperty("messageId") + "/reply";
		RestResponse<JSONObject> apiRestResponse =
				sendJsonRestRequest(apiEndPoint, "POST", apiRequestHeadersMap);

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 202);
		Assert.assertEquals(apiRestResponse.getHttpStatusCode(), 202);
	}

	/**
	 * Negative test case for replyToAll method with mandatory parameters.
	 */
	@Test(enabled = true,
			description = "outlookmail {replyToSender} integration test with negative case.")
	public void testReplyToSenderWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:replyToSender");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "replyToSender_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}

	/**
	 * Positive test case for sendMail method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {sendMail} integration test with mandatory" +
	                                    " parameters.")
	public void testSendMailWithMandatoryParameters() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:sendMail");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "sendMail_mandatory.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 202);
	}

	/**
	 * Negative test case for sendMail method with mandatory parameters.
	 */
	@Test(enabled = true, description = "outlookmail {sendMail} integration test with negative case.")
	public void testSendMailWithNegativeCase() throws IOException, JSONException {
		esbRequestHeadersMap.put("Action", "urn:sendMail");

		RestResponse<JSONObject> esbRestResponse =
				sendJsonRestRequest(proxyUrl, "POST", esbRequestHeadersMap,
				                    "sendMail_negative.json");

		Assert.assertEquals(esbRestResponse.getHttpStatusCode(), 401);
	}
}
