# Thought Process - eXXcellent Coding Challenge

## 1. Separation of concerns:

- Reading the contents from the data source.


- Storing the read contents in a data structure.


- Processing the contents which were stored in the data structure.

## 2. Architectural choices:

- Intentionally building without using of a Microservice architecture. <br>
  ⇒ Would introduce too much overhead for this simple task. 


- Instead, to keep the code decoupled but still somewhat scalable and easily maintainable,
  I decided to use the Event-Driven Architecture (EDA) Pattern.
