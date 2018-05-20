package com.coffeebucks.examples;

import com.coffeebucks.dto.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Vigneshwar Raghuram
 */

public class Example1 {

    public static void main(String[] args) {

        List<Person> personList = populatePerson();


        /*
            Filter Example
            --Find All the persons with age<30
         */

        List<Person> personLessThan30 = personList
                                        .stream()
                                        .filter(t->t.getAge()<30)
                                        .collect(Collectors.toList());

        System.out.println(personLessThan30.size());



        /*
            Optional  Example
            --Find the Person who lives in Chicago and also whose age is greater than 50.
         */

        Optional<Person> person = personList.stream().filter(t->t.getAge()>50).findFirst();
        person.ifPresent(name -> System.out.println(name.getName()));


        /*
            Map  Example
            --Find the Persons City whose name is Joey.
         */

        String city = personList
                        .stream()
                        .filter(t->t.getName().equalsIgnoreCase("Joey"))
                        .map(Person::getCity).findFirst().orElse("");

        System.out.println(city);

        /*
            Stream Example
         */

        List<Integer> num = Arrays.asList(1,2,3,4,5);
        List<Integer> collect1 = num
                                .stream()
                                .map(n -> n * 2)
                                .collect(Collectors.toList());
        System.out.println(collect1);


    }

    private static List<Person> populatePerson() {
        List<Person> personList = new ArrayList<>();

        Person person1 = new Person();
        person1.setAge(23);
        person1.setName("Chandler");
        person1.setCity("Los Angeles");

        Person person2 = new Person();
        person2.setAge(55);
        person2.setName("Monica");
        person2.setCity("Chicago");

        Person person3 = new Person();
        person3.setAge(25);
        person3.setName("Joey");
        person3.setCity("San Diego");

        Person person4 = new Person();
        person4.setAge(30);
        person4.setName("Ross");
        person4.setCity("Los Angeles");

        Person person5 = new Person();
        person5.setAge(45);
        person5.setName("Rachel");
        person5.setCity("NewYork");

        Person person6 = new Person();
        person6.setAge(27);
        person6.setName("Phoebe");
        person6.setCity("Conencticut");
        personList.add(person1);
        personList.add(person2);
        personList.add(person3);
        personList.add(person4);
        personList.add(person5);
        personList.add(person6);
        return personList;
    }
}