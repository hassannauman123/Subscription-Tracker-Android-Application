
## What technical debt has been cleaned up



## What technical debt did you leave?  

We don’t like how our subscriptionHandler class works. It’s a bit too big, and isn’t focused enough (possibly single responsibility violation). We created the class in the very first iteration, and largely based it on the example from the cook-book. Over time, the class grew. In retrospect we wish we thought out the design a little bit more carefully.
If we could restructure our design, we would have created a subscription parameters or validator class that held all the allowable parameters for a subscription object (supporting injection) and did all of the validation work with this class. Then we would have created a subscriptionAccess class that is more focused by having it only deal with fetching and saving subscriptions, and the validator would be injected into that class. In the future if we added more classes (like a class that helps build a subscription object) we could just inject in the same validatorClass. We would classify this technical debt as prudent and inadvertent debt. We were trying to structure our code well from the beginning, but simply didn’t know enough programming principles to accomplish that goal. We have learned that it’s very important to apply good programming principles from the very start otherwise you may end up with a tangled mess of code that is hard to modify. We will take this lesson to heart in future software engineering programs.


## Discuss a Feature or User Story that was cut/re-prioritized  

We decided to cut the partnered service feature ( along with the user story). Originally the idea was that a user would be able to subscribe to a new service right on the app by searching for a type of service (like video streaming), and then would be shown a list of services that they can sign up for using our app. We (the app developers, or owners) would then be compensated by the service. Since our phone is a local non internet app, we would pretty much have to fake every single aspect of this feature. In real life such a feature would be very large, and probably viewed more as a service. You would have to create an entire infrastructure to allow our app to sign up users for other services, and you might even need a sales team to contact other companies to convince them to partner with you. This would imply that the app already has a big enough user base for anyone to care about it. So this is probably a feature 

## Acceptance test/end-to-end  

We created the filter by tags test. We tested the ability for the user to add a subscription with tags, and then filter the subscriptions by tags with the correct subscription showing up.  The test first adds two subscriptions with two different sets of tags (we used a method we wrote to make the task easier). Then our test filters by the correct tag (all using predefined variables to target our input), and then it makes sure the correct subscriptions show up when the filters are applied. We did a couple of things to prevent the test from being flaky. First we set up the database such that it was always the same, so that the test environment was identical each time. We also determined that we needed to use a system clock sleep function to put in a delay between clicks,otherwise on slower computers espresso may try to click a dynamically created popup menu before it is fully created. Another thing we did was to use string resources to target the button and popup confirmations. Now if the confirmation dialog or button text ever changes our test should automatically update, and continue to work.

## Acceptance test, untestable  

It was very difficult to get the acceptance test to properly select a dynamically created drop down menu. We tried everything, including using the built-in espresso recorder test. It took many, many hours of googling to finally get it to run correctly. Also, It was very difficult for us to test the sort methods. For instance, one sort method sorts the subscriptions by name but our main activity doesn’t display the subscriptions name by themselves, instead they are in the middle of a string. Another problem we faced is that we couldn’t use the same test utility from the original unit tests to create a new sql database each test (technical reasons: it boils down to the fact that acceptance tests actually run in android, so the database is always saved). So our solution was to switch to a test database before any acceptance tests run. Another problem we faced was that sometimes tests would fail (almost randomly), and yet other times that exact same test would pass. What we determined was that espresso would sometimes try to click a dynamically created element before it even finished loading, which would crash the test. Our solution to this was to inject some wait time using a sleep method, hopefully ensuring that the element is given enough time to load before espresso tries to click it. 


## n+1 Acceptance test  



## Velocity/teamwork  

Our time estimate accuracy more or less stayed the same through out the two iterations. In both iterations the actual time spent was close to the estimate. In should be noted though, that for iteration 2 there were a lot more tasks that we did not record in our estimate. For instance, setting up the hsqldb and creating the integration tests was not counted as developer tasks, but took a lot of time. * Be sure to link to the velocity here. 
