# Review Service by Horseshoe

### About
Review Service by Horseshoe is cross-store review management system. The service provides a powerful solution to create, moderate and display product reviews in your online store with an integrated possibility to display approved reviews from other online stores as well. 

### Getting started / Registration
* start Review Service
* open Review Service's [registration page](http://localhost:6543/) 
* submit your company's name and email address
* your API key will arrive soon in a confirmation email
* now you can integrate Review Service with your online store!

### How to use
##### Routes
* `/` -> registration form
* `/review/{APIKey}/{productName}/{comment}/{ratings}` -> to send review data from your site to Review Service
* `/reviewsFromClient/{APIKey}` -> to get all reviews for all products from your store
* `/review/{APIKey}/{productName}` -> to get all rewiews for a specific product from every store using the same database

##### Moderation
* You will receive an email notification when a new review is submitted on your site. You can either approve or archive it by clicking on the corresponding link in the email, Review Service will do the rest. Only the approved reviews will be displayed on your site.

---------
Happy reviewing! Please provide feedback if you are using our service!
