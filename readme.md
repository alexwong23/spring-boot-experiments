# Spring Boot, JPA

# Things to do
1. render pages for user controller 
   1. get (/user) - render all users with button to edit and delete
      1. edit button - redirects to (/user/id)
      2. delete button - fire delete request and re-render (/user) page
   2. get (/user/new) - render user form to create new user
      1. enable form validation - throws error messages
   3. get (/user/id) - render user form to edit user
      1. populate form with user data
      2. enable form validation - throws error messages
   
