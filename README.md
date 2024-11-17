# Autograder Project
# COMP 3607 - OBJECT ORIENTED PROGRAMMING 2

Team Members - Jonathan Joseph \
             - Matthew Roodal - 816024135 \
             - Joshua Weekes - 816032626 \
             - Naomi Dookeran - 816026826 \
             - Aarti Jessica Sirju - 816035928 \

# Project Scope
Autograder for Object-Oriented Programming Assignments (JAVA only).
The project focuses on designing, developing an automated judging/grading system that can be utilized to grade and mark assignments
from Object Oriented Programming. \
The system must: \
    - Accept a ZIP file containing one or more .java files.\
    - Evaluate the correctness of the code based on predefined test cases from the assignment specification.\
    - Produce a PDF report with the results of test cases (pass/fail) and provide feedback where necessary.\
    - Calculate and present an overall score for the submission based on the test results.\
    
The solution must:\
    - Be implemented in Java.\
    - Include at least three design patterns (excluding Singleton).\
    - Conform to SOLID design principles.\
    - Be evaluated using a test suite.\
    - Be packaged as a Maven project.\


# DESIGN

# Design Patterns Used:
Factory Method:
Creational Design Pattern
Provides an interface for creating objects in a superclass, but allows subclasses to alter the type of objects that will be created.
Used when the exact types and dependencies of the objects your code should work with is unknown.
Used to provide a library or framework with a way to extend its internal components.

Template Method:
Behavioural Design Pattern
Defines the skeleton of an algorithm in the superclass but lets subclasses override specific steps of the algorithm without changing its structure.
Used to let clients extend only particular steps of an algorithm, but not the whole algorithm or its structure.
Used when there are classes that contain almost identical algorithms with some minor differences.

Strategy Method:
Behavioural Design Pattern
Defines a family of algorithms, putting each of them into a separate class to make their objects interchangeable.
Differentiates variants of an algorithm within an object and is able to switch from one algorithm to another during runtime.
Distinguishes similar classes that only differ in the way they execute some behavior. 
Isolates the business logic of a class from its implementation details.


# Conformance to SOLID Design Principles:
Single Responsibility Principle:
A class should have only one reason to change, meaning it should focus on one responsibility or functionality.
Break down the system into small, cohesive classes, each handling a specific responsibility.
Avoid mixing concerns like data handling, business logic, and presentation in a single class.

Open/ Closed Principle:
Classes should be open for extension but closed for modification.
Design using interfaces and abstract classes so functionality can be extended without altering existing code.
Use polymorphism to allow different behaviors to be plugged in dynamically, adhering to existing contracts.

Liskov Substitution Principle:
Subtypes must be substitutable for their base types without altering the correctness of the program.
Ensure derived classes honor the behavior and constraints of their base class.
Avoid overriding base class methods in a way that changes expected behavior.

Interface Segregation Principle:
A class should not be forced to implement interfaces it does not use.
Break large interfaces into smaller, more focused ones.
Ensure each interface represents a single functionality, allowing classes to implement only what they need.

Dependency Inversion Principle:
High-level modules should not depend on low-level modules; both should depend on abstractions.
Use dependency injection to pass dependencies via constructors or setters.
Depend on interfaces or abstract classes rather than concrete implementations to promote flexibility.



