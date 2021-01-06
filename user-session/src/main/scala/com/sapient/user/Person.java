package com.sapient.user;

import java.util.Arrays;
import java.util.List;

public class Person {

    private String name;
    private int age;
    private String location;

    public Person(String name, int age, String location) {
        this.name = name;
        this.age = age;
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }





    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", location='" + location + '\'' +
                '}';
    }

    public static List<Person> getPersons() {
        return  Arrays.asList(new com.sapient.user.Person("Amit",21,"London"),
                new com.sapient.user.Person("Cynthia",28,"Belfast"),
                new com.sapient.user.Person("Wendy",26,"Manchester"),
                new com.sapient.user.Person("Gareth",21,"Cardiff"),
                new com.sapient.user.Person("Wendy",26,"Manchester"),
                new com.sapient.user.Person("Gareth",21,"Cardiff"),
                new com.sapient.user.Person("Charles",29,"Edinburgh"));
    }
}
