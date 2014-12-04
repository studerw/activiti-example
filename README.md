activiti-example
================

Activiti Workflow example using Spring MVC

To run:
mvn tomcat7:run

Access the site:
http://localhost:9090/activiti-example/

Login in as any of the users - the password is the same name as the user (e.g. kermit/kermit).

Create a document and submit for approval.

Then logout and login as another user in the same group as that whom the document was created. You can
view the list of users by clicking the 'users' button on the bottom right of the form.

View your tasks and see that there is a new document waiting to be approved. Approve or deny. Log back in as the original author.
