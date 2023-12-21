package org.sentrysoftware.wbem;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sentrysoftware.wbem.client.WqlQuery;
import org.sentrysoftware.wbem.client.exceptions.WqlQuerySyntaxException;

class WqlQueryTest {

	@Test
	void testQueryCommon() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> WqlQuery.parseQuery(null));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery(""));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Sel"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Up"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Upate T set a=x, b=y"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Upate T set a=x, b=y where c=z"));
	}

	@Test
	void testSelect() throws Exception {
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Sel a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Update a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select *,a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select *,* From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select * * From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b "));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select From "));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery(" a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b From "));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select Select a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b From From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select ,a,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a, ,b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b, From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a  b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a;b From T"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b From T U"));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("Select a,b T "));
		Assertions.assertThrows(WqlQuerySyntaxException.class, () -> WqlQuery.parseQuery("From a,b Select T"));

		{
			final WqlQuery wqlQuery = WqlQuery.parseQuery("SELECT * FROM HITACHI_FCPort");

			Assertions.assertEquals("HITACHI_FCPort", wqlQuery.getClassName());
			Assertions.assertNull(wqlQuery.getPropertiesArray());
			Assertions.assertTrue(wqlQuery.getProperties().isEmpty());
			Assertions.assertTrue(wqlQuery.getOriginalProperties().isEmpty());
			Assertions.assertFalse(wqlQuery.hasDuplicateProperties());
		}
		{
			final WqlQuery wqlQuery = WqlQuery.parseQuery(" select    Antecedent,     Dependent    from IBMTSDS_SHWIDToSPC");

			Assertions.assertEquals("IBMTSDS_SHWIDToSPC", wqlQuery.getClassName());
			Assertions.assertArrayEquals(new String[] {"Antecedent", "Dependent"}, wqlQuery.getPropertiesArray());
			Assertions.assertEquals(Arrays.asList("Antecedent", "Dependent").stream().collect(Collectors.toCollection(LinkedHashSet::new)), wqlQuery.getProperties());
			Assertions.assertEquals(Arrays.asList("Antecedent", "Dependent"), wqlQuery.getOriginalProperties());
			Assertions.assertFalse(wqlQuery.hasDuplicateProperties());
		}
		{
			final WqlQuery wqlQuery = WqlQuery.parseQuery("   SELECT __Path, Name, __PATH   from   EMC_StorageSystem   ");

			Assertions.assertEquals("EMC_StorageSystem", wqlQuery.getClassName());
			Assertions.assertArrayEquals(new String[] {"Name"}, wqlQuery.getPropertiesArray());
			Assertions.assertEquals(Arrays.asList("__Path", "Name").stream().collect(Collectors.toCollection(LinkedHashSet::new)), wqlQuery.getProperties());
			Assertions.assertEquals(Arrays.asList("__Path", "Name", "__PATH"), wqlQuery.getOriginalProperties());
			Assertions.assertTrue(wqlQuery.hasDuplicateProperties());
		}
	}
}
