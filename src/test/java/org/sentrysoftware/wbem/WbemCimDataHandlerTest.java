package org.sentrysoftware.wbem;

import java.math.BigInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.sentrysoftware.wbem.javax.cim.CIMClass;
import org.sentrysoftware.wbem.javax.cim.CIMClassProperty;
import org.sentrysoftware.wbem.javax.cim.CIMDataType;
import org.sentrysoftware.wbem.javax.cim.CIMDateTime;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeAbsolute;
import org.sentrysoftware.wbem.javax.cim.CIMDateTimeInterval;
import org.sentrysoftware.wbem.javax.cim.CIMInstance;
import org.sentrysoftware.wbem.javax.cim.CIMObjectPath;
import org.sentrysoftware.wbem.javax.cim.CIMProperty;
import org.sentrysoftware.wbem.client.WbemCimDataHandler;

class WbemCimDataHandlerTest {

	@Test
	void testGetCimPropertyAsString() {

		final CIMObjectPath objectPath = new CIMObjectPath("root/emc:Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"");

		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemCimDataHandler.getCimPropertyAsString(null, new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, "SYMMETRIX-+-000297800620")}), null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemCimDataHandler.getCimPropertyAsString("Name", null, null));

		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemCimDataHandler.getCimPropertyAsString("__Path", new CIMInstance(null, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, "SYMMETRIX-+-000297800620")}), null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> WbemCimDataHandler.getCimPropertyAsString("Ref", new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Ref", new CIMDataType("Ref", 1), "x")}), null));

		Assertions.assertEquals(
				"Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"",
				WbemCimDataHandler.getCimPropertyAsString("__PATH", new CIMInstance(objectPath, null), null));

		Assertions.assertEquals(
				"",
				WbemCimDataHandler.getCimPropertyAsString(
						"UnknownProperty",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, "\"SYMMETRIX-+-000297800620;\"")}), null));

		Assertions.assertEquals(
				"blablaVal",
				WbemCimDataHandler.getCimPropertyAsString(
						"blabla",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, "\"SYMMETRIX-+-000297800620;\""), new CIMProperty<String>("blabla", CIMDataType.STRING_T, "blablaVal", true, false, null)}), null));

		Assertions.assertEquals(
				"",
				WbemCimDataHandler.getCimPropertyAsString(
						"Name",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, null)}), null));

		Assertions.assertEquals(
				"\"SYMMETRIX-+-000297800620;\"",
				WbemCimDataHandler.getCimPropertyAsString(
						"Name",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<String>("Name", CIMDataType.STRING_T, "\"SYMMETRIX-+-000297800620;\"")}), null));

		Assertions.assertEquals(
				"true",
				WbemCimDataHandler.getCimPropertyAsString(
						"EMCAutoMetaEnabled",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<Boolean>("EMCAutoMetaEnabled", CIMDataType.BOOLEAN_T, Boolean.TRUE)}), null));

		Assertions.assertEquals(
				"true|false|",
				WbemCimDataHandler.getCimPropertyAsString(
						"EMCAutoMetaEnabledArray",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<Boolean[]>("EMCAutoMetaEnabledArray", CIMDataType.BOOLEAN_ARRAY_T, new Boolean[] {Boolean.TRUE, Boolean.FALSE})}), null));

		Assertions.assertEquals(
				"10",
				WbemCimDataHandler.getCimPropertyAsString(
						"PortType",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<Integer>("PortType", CIMDataType.SINT32_T, Integer.valueOf(10))}), null));

		Assertions.assertEquals(
				"2125000000",
				WbemCimDataHandler.getCimPropertyAsString(
						"MaxSpeed",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<BigInteger>("MaxSpeed", CIMDataType.UINT64_T, new BigInteger("2125000000"))}), null));

		Assertions.assertEquals(
				"1|2|",
				WbemCimDataHandler.getCimPropertyAsString(
						"OperationalStatus",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<Short[]>("OperationalStatus", CIMDataType.SINT16_ARRAY_T, new Short[] {1,2})}), null));

		Assertions.assertEquals(
				"Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"",
				WbemCimDataHandler.getCimPropertyAsString(
						"Ref",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMObjectPath>("Ref", new CIMDataType("Ref"), objectPath)}), null));

		Assertions.assertEquals(
				"Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"" +
				"|",
				WbemCimDataHandler.getCimPropertyAsString(
						"Ref",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMObjectPath[]>("Ref", new CIMDataType("Ref", 1), new CIMObjectPath[] {objectPath})}), null));

		Assertions.assertEquals(
				"Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"" +
				"||"+
				"Symm_StorageSystem.CreationClassName=\"Symm_StorageSystem\",Name=\"SYMMETRIX-+-000297800620\"" +
				"|",
				WbemCimDataHandler.getCimPropertyAsString(
						"Ref",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMObjectPath[]>("Ref", new CIMDataType("Ref", 3), new CIMObjectPath[] {objectPath, null, objectPath})}), null));

		Assertions.assertEquals(
				"1173882243000",
				WbemCimDataHandler.getCimPropertyAsString(
						"timeAbsolute",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMDateTimeAbsolute>("timeAbsolute", CIMDataType.DATETIME_T, new CIMDateTimeAbsolute("20070314160503.566012+101"))}), null));

		Assertions.assertEquals(
				"1173882243566",
				WbemCimDataHandler.getCimPropertyAsString(
						"timeInterval",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMDateTimeInterval>("timeInterval", CIMDataType.DATETIME_T, new CIMDateTimeInterval(1173882243566L))}), null));

		Assertions.assertEquals(
				"1173882243000|1173882243566|",
				WbemCimDataHandler.getCimPropertyAsString(
						"timeArray",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMDateTime[]>("timeArray", CIMDataType.DATETIME_ARRAY_T, new CIMDateTime[] {new CIMDateTimeAbsolute("20070314160503.566012+101"), new CIMDateTimeInterval(1173882243566L)})}), null));

		final CIMClass cimClass =  new CIMClass(
				"testClass",
				null,
				null,
				new CIMClassProperty[] {new CIMClassProperty<Object>("KeyProp", CIMDataType.STRING_T, null, null, false, false, null)},
				null);

		Assertions.assertEquals(
				"class testClass {" +
				"  string KeyProp;" +
				"};",
				WbemCimDataHandler.getCimPropertyAsString(
						"propClass",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMClass>("propClass", CIMDataType.CLASS_T, cimClass)}), null).replaceAll("[\r\n]", ""));

		Assertions.assertEquals(
				"class testClass {" +
				"  string KeyProp;" +
				"};" +
				"$$" +
				"class testClass {" +
				"  string KeyProp;" +
				"};" +
				"$",
				WbemCimDataHandler.getCimPropertyAsString(
						"propClass",
						new CIMInstance(objectPath, new CIMProperty<?>[] {new CIMProperty<CIMClass[]>("propClass", CIMDataType.CLASS_ARRAY_T, new CIMClass[] {cimClass, null, cimClass})}), "$").replaceAll("[\r\n]", ""));
	}
}
