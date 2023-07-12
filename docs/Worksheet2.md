# Iteration 2 Worksheet

## Paying Off Technical Debt 
1. Originally we had allowable frequencies as a string. This meant that whenever we had to reference a frequency property we needed a bunch of switch statements ( usually in multiple functions, depending on what part of the frequency information we needed). This meant adding new frequencies required a bunch of code modification in different places, and we were violating the open for extension closed for modification principle. We paid off this debt by creating a frequency interface, and creating different classes that implement the frequency interface that hold needed methods of the frequencies. This was inadvertent and prudent technical debt, as we did not yet know about the principle when we were first introduced to this feature during the first iteration of the project, but changed our implementation once we learned about open closed principle.
original:  
https://code.cs.umanitoba.ca/3350-summer2023/ghostdevs-7/-/blame/9b7cb21cc454441f025958cd26e6d5ade485c7c0/app/src/main/java/com/track_it/logic/SubscriptionHandler.java#L29  
Updated as a list of frequency objects.  
https://code.cs.umanitoba.ca/3350-summer2023/ghostdevs-7/-/blame/bryceWorking/app/src/main/java/com/track_it/logic/SubscriptionHandler.java#L30

2. Dependency Inversion Principle Violation  
Originally our code was highly coupled with the concrete implementation of our fake database (an array list). This was addressed by rewriting and restructuring several classes and tests to support different database implementations. This debt was prudent and inadvertent as we knew we would be implementing a SQL database for the second iteration which would require several modifications. We didn't have enough knowledge at the time to know how to properly structure our code as we had no familiarity with Android development or experience implementing an HSQL database. We knew going into iteration 2 several changes would have to be made but taking on the debt in iteration 1 allowed us work quickly and focus on other aspects of the project.  

## SOLID Violations
We created an issue for projects 8. We informed them that their classes should support dependency injection, and specifically linked to a class in their main branch that violated the “Dependency inversion principle”.
https://code.cs.umanitoba.ca/3350-summer2023/tech-titans-8/-/issues/83

## Retrospective 
Our retrospective for iteration 1 brought our attention to several issues that we had hoped to address while completing iteration 2. While we improved in some areas, not all of the proposed solutions were implemented.

Firstly, we had agreed that the manner in which we wrote our commit messages could be improved. We had agreed to start implementing the suggestions outlined in the article tagged in the readings. Taking a look at our commits it is clear this was not implemented and will be the focus for iteration 3.

A second issue we acknowledged was that we failed to calculate and report the total time spent on each user story and feature. This was well documented and reported as can be observed in the issues section.

Communication overall seemed to improve. Our group was more responsive for this iteration and worked well together. An example of this was when one group member was working on a bug fix while another member was in possession of the Samsung device at the time. One member would implement changes, the other group member would run the app with the updates and report back what still needed to be addressed. This was done very efficiently. A screenshot of this exchange is displayed below.

## Design Patterns
Singleton design pattern, combined with a builder class.
For the SubscriptionHandler class there are a lot of input parameters, and a lot of expensive setup operations to create a  new SubscriptionHandler object. So we created a wrapper class called SetupParameters that initializes and returns one copy of the SubscriptionHandler with the correct input. This prevents the calling code from having to set up all the correct input, and makes it faster by only creating the SubscriptionHandler object once, and returning the same object for each call to the getSubscriptionHandler method ( assuming the setup parameters have not changed).
https://code.cs.umanitoba.ca/3350-summer2023/ghostdevs-7/-/blob/bryceWorking/app/src/main/java/com/track_it/presentation/util/SetupParameters.java

## Iteration 1 Feedback Fixes
Issue:
https://code.cs.umanitoba.ca/3350-summer2023/ghostdevs-7/-/issues/34

Fix:
https://code.cs.umanitoba.ca/3350-summer2023/ghostdevs-7/-/blame/bryceWorking/app/src/main/java/com/track_it/presentation/MainActivity.java#L263

The issue was that on the main activity screen we showed all the different subscriptions in a big list, but the subscriptions were not visually shown as being separate (ie, one subscription details started right after the other with no border, or color change). This made it hard to clearly see how subscriptions were separated. We fixed this issue by giving each successive subscription a different background color, so that you can more easily identify different subscriptions . 
