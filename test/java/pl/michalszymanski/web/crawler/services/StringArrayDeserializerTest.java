/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michalszymanski.web.crawler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class StringArrayDeserializerTest {

    public StringArrayDeserializerTest() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    StringArrayTrimmedDeserializer ds;

    @Before
    public void setUp() {
    }

    /**
     * Test of deserialize method, of class StringArrayTrimmedDeserializer.
     */
    static class TestClass {

        @JsonDeserialize(using = StringArrayTrimmedDeserializer.class)
        private String[] array;

        public String[] getArray() {
            return array;
        }

        public void setArray(String[] array) {
            this.array = array;
        }

    }

    @Test
    public void testDeserialize() throws Exception {
        String str = "ala, ma, kota";
        String[] expected = new String[]{"ala", "ma", "kota"};
        Map serialized = new HashMap();
        serialized.put("array", str);

        TestClass result = new ObjectMapper().convertValue(serialized, TestClass.class);
        assertTrue(Arrays.equals(expected, result.getArray()));

    }

}
