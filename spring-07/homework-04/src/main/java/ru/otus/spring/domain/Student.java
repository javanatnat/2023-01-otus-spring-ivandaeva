package ru.otus.spring.domain;

public class Student {
    private final String firstName;
    private final String secondName;

    public Student(
            String firstName,
            String secondName
    ) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFullName() {
        return firstName + " " + secondName;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31*result + secondName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Student)) {
            return false;
        }

        Student student = (Student) obj;

        return this.firstName.equals(student.firstName) && this.secondName.equals(student.secondName);
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
