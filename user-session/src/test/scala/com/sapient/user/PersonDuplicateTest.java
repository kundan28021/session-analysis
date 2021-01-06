package com.sapient.user;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class PersonDuplicateTest extends TestCase {
    private static List<Person> personList;
    @BeforeClass
    public void setUp() throws Exception {
      personList = Arrays.asList(
                new Person("Amit",21,"London"),
                new Person("Wendy",26,"Manchester"),
                new Person("Gareth",21,"Cardiff"),
                new Person("Wendy",26,"Manchester"),
                new Person("Gareth",21,"Cardiff")
               );
    }


    @Test
    public void testPersonTest() {
       HashMap<String, Person> expected= new HashMap<String, Person>();
       expected.put("Amit-21",new Person("Amit",21,"London"));
       expected.put("Wendy-26",new Person("Wendy",26,"Manchester"));
       expected.put("Gareth-21",new Person("Gareth",21,"Cardiff"));

       Object[] expectedKeys= expected.keySet().toArray();
       HashMap<String, Person> actual=PersonDuplicate.getUniquePerson(personList);

        Object[] actualKeys= actual.keySet().toArray();

        Arrays.sort(expectedKeys);
        Arrays.sort(actualKeys);
        actual.forEach((k,v)->System.out.println(k+" "+v));

       assertTrue(expected.keySet().size() == actual.keySet().size());
       Assert.assertArrayEquals(expectedKeys,actualKeys);

    }

}