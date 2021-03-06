/*
 * Copyright (C) 2012 BeeOne GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.beeone.netbankinglight.test;

import at.beeone.netbankinglight.api.model.Account;
import at.beeone.netbankinglight.api.model.Transaction;
import at.beeone.netbankinglight.api.model.User;
import at.beeone.netbankinglight.util.IoHelper;
import at.beeone.netbankinglight.util.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class JsonParserTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParseTransaction() throws JSONException {
		JSONObject json = new JSONObject(getSampleJson("transaction.json"));
		Transaction transaction = JsonParser.toTransaction(json);
		assertNotNull(transaction);
		assertEquals(transaction.getId(), "1155");
		assertEquals(null, transaction.getBill());
		assertEquals(Boolean.FALSE, transaction.isCancelled());
		assertEquals(Boolean.TRUE, transaction.isFinished());
		assertEquals(1350635542000L, transaction.getCarryOutDate().getTime());
		assertEquals(1350635542000L, transaction.getCreatedOn().getTime());
		assertEquals(null, transaction.getCustomerData());
		assertEquals(null, transaction.getIdentification());
		assertEquals(null, transaction.getNotes());
		assertTrue(transaction.getTags().isEmpty());
	}

	protected String getSampleJson(String filename) {
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream(filename);
			StringBuilder jsonString = IoHelper.readStream(inputStream);
			return jsonString.toString();
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	@Test
	public void testParseAccounts() throws JSONException {
		JSONArray json = new JSONArray(getSampleJson("accounts.json"));
		List<Account> accounts = JsonParser.toAccountList(json);
		assertNotNull(accounts);
		assertEquals(3, accounts.size());
		Account firstAccount = accounts.get(0);
		Account secondAccount = accounts.get(1);
		Account thirdAccount = accounts.get(2);

		assertEquals(1, firstAccount.getOwners().size());
		assertEquals(3, secondAccount.getOwners().size());

		assertEquals(User.class, firstAccount.getOwners().iterator().next()
				.getClass());
		assertEquals(Long.valueOf(223600), firstAccount.getAvailableFunds());
		assertEquals(Long.valueOf(123600), firstAccount.getBalance());
		assertEquals(Long.valueOf(100000), firstAccount.getOverdraft());
		assertTrue(firstAccount.getSettings().isSearchable());
		assertEquals("VISA", secondAccount.getSettings().getName());

		assertEquals("Loan Account", thirdAccount.getSettings().getName());
		assertEquals("LOAN", thirdAccount.getType());

	}

}
