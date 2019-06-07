

##### CQRS Command and Query Responsibility Segregation ([CQRS](http://codebetter.com/gregyoung/2010/02/16/cqrs-task-based-uis-event-sourcing-agh/) as described here)
> Starting with CQRS, CQRS is simply the creation of two objects where there was previously only one. 
The separation occurs based upon whether the methods are a command or a query (the same definition that 
is used by Meyer in Command and Query Separation, a command is any method that mutates state and a query 
is any method that returns a value).

> When most people talk about CQRS they are really speaking about applying the CQRS pattern to the object that
 represents the service boundary of the application. Consider the following pseudo-code service definition.
 
```
CustomerService

void MakeCustomerPreferred(CustomerId) 
Customer GetCustomer(CustomerId) 
CustomerSet GetCustomersWithName(Name) 
CustomerSet GetPreferredCustomers() 
void ChangeCustomerLocale(CustomerId, NewLocale) 
void CreateCustomer(Customer) 
void EditCustomerDetails(CustomerDetails)
```
 

##### Applying CQRS on this would result in two services

```
CustomerWriteService

void MakeCustomerPreferred(CustomerId) 
void ChangeCustomerLocale(CustomerId, NewLocale) 
void CreateCustomer(Customer) 
void EditCustomerDetails(CustomerDetails)
```

```
CustomerReadService

Customer GetCustomer(CustomerId) 
CustomerSet GetCustomersWithName(Name) 
CustomerSet GetPreferredCustomers()
```

> That is it. That is the entirety of the CQRS pattern. There is nothing more to it than that… 
Doesn’t seem nearly as interesting when we explain it this way does it? This separation however 
enables us to do many interesting things architecturally, the largest is that it forces a break of the mental
 retardation that because the two use the same data they should also use the same data model.

> The largest possible benefit though is that it recognizes that their are different architectural properties 
when dealing with commands and queries … for example it allows us to host the two services differently eg: 
we can host the read service on 25 servers and the write service on two. The processing of commands and queries
 is fundamentally asymmetrical, and scaling the services symmetrically does not make a lot of sense.









References:
> https://microservices.io/patterns/data/cqrs.html

> htps://www.kennybastani.com/2017/01/building-event-driven-microservices.html

> https://www.e4developer.com/2018/03/11/cqrs-a-simple-explanation/


> https://developer.ibm.com/articles/cl-build-app-using-microservices-and-cqrs-trs/ -https://github.com/feliciatucci/cqrs-sample


> https://github.com/jrajani/mini-bank  -- https://www.youtube.com/watch?v=d3Ks40u0tO8

> https://medium.com/@qasimsoomro/building-microservices-using-node-js-with-ddd-cqrs-and-event-sourcing-part-1-of-2-52e0dc3d81df

> https://github.com/mfarache/micronaut-cqrs-kafka

> https://dzone.com/articles/cqrs-by-example-introduction --  https://github.com/asc-lab/java-cqrs-intro

> https://www.youtube.com/watch?v=uTCKzPg0Uak -- https://github.com/sdaschner/scalable-coffee-shop

> https://www.youtube.com/watch?v=JHGkaShoyNs
