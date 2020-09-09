Voting system for deciding where to have lunch
===============================================
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/99a5b9f443e54d3f8ea54929124c1c13)](https://app.codacy.com/manual/iliketobreathe/restaurant_vote?utm_source=github.com&utm_medium=referral&utm_content=iliketobreathe/restaurant_vote&utm_campaign=Badge_Grade_Dashboard)

Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote on which restaurant they want to have lunch at
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.

P.S.: Make sure everything works with the latest version that is on github :)

P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

-------------
Examples of curl commands for admin:

create new user  
curl -s -X POST -d '{"name" : "New User", "email" : "newuser@ya.ru", "password" : "newuser", "roles" : ["USER"]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting-system/rest/admin/users --user admin@gmail.com:admin

create new restaurant  
curl -s -X POST -d '{"name" : "FrogHeaven"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting-system/rest/admin/restaurants --user admin@gmail.com:admin

get all restaurants  
curl -s http://localhost:8080/voting-system/rest/admin/restaurants --user admin@gmail.com:admin

get all today's dishes for restaurant  
$ curl -s http://localhost:8080/voting-system/rest/admin/dishes/100002 --user admin@gmail.com:admin

get all votes  
$ curl -s http://localhost:8080/voting-system/rest/profile/restaurants/votes --user admin@gmail.com:admin

Examples of curl commands for general users:

register   
curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting-system/rest/profile/register

update user information  
curl -s -X PUT -d '{"name":"newUser","email":"admin@gmail.com","password":"newPassword"}' -H 'Content-Type: application/json' http://localhost:8080/voting-system/rest/profile/ --user user@yandex.ru:password

vote for restaurant  
$ curl -s http://localhost:8080/voting-system/rest/profile/restaurants/100002/vote --user admin@gmail.com:admin

