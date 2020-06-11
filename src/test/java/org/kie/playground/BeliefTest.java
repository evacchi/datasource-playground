package org.kie.playground;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeliefTest {

    @Test
    public void checkContents() {
        var stated = new ListDataStore<String>();
        var justified = new ListDataStore<String>();
        var ds = new Belief<String>();
        var rec = new RecordingSubscriber<String>();
        ds.subscribe(rec);

        stated.subscribe(ds.stated());
        justified.subscribe(ds.justified());

        stated.add("Hello");
        stated.add("Hello");
        justified.add("Hi");
        justified.add("Hi");

        assertEquals(List.of("Hello", "Hello", "Hi"), rec.getData());
    }

    static  class Person {
        static int counter;

        int id = ++counter;
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Person{");
            sb.append("id=").append(id);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Test
    public void checkContents2() {


        var stated = new ListDataStore<Person>();
        var justified = new ListDataStore<Person>();
        var ds = new Belief<Person>();
        var rec = new RecordingSubscriber<Person>();
        ds.subscribe(rec);

        stated.subscribe(ds.stated());
        justified.subscribe(ds.justified());

        var j1 = new Person("John");
        var j2 = new Person("John");
        var j3 = new Person("John");
        var j4 = new Person("John");
        stated.add(j1);
        stated.add(j2);
        justified.add(j3);
        justified.add(j4);

        assertEquals(List.of(j1,j2), rec.getData());
    }

}
