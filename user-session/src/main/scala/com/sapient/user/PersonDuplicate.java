package com.sapient.user;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.List;

public class PersonDuplicate {

    private final static Logger logger = Logger.getLogger(PersonDuplicate.class);

    /*get the list of Persons*/
    private static List<Person> personList = Person.getPersons();

    public static HashMap<String, Person> finalMap = new HashMap<>();


    public static void main(String[] args) {
        PropertyConfigurator.configure("log4j.properties");
        printUniquePersons(getUniquePerson(personList));
    }


    /* Preparing all unique persons based on name and age*/
    public static HashMap<String, Person> getUniquePerson(List<Person> personList) {
        for (Person emp : personList) {
            String key = emp.getName() + "-" + emp.getAge();
            if (!finalMap.containsKey(key)) {
                finalMap.put(key, emp);
                logger.info("populating Hash Map with " + key);
            } else {
                logger.info(key + " is already present");
            }
        }
        return finalMap;
    }


    /* Printing name and age of all unique persons with : sperated*/
    private static void printUniquePersons(HashMap personMap) {
        for (Object key : personMap.keySet()) {
            logger.info("Printing name and Location " + key);
        }

    }


}