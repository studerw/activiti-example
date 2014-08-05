activiti-example
================

Activiti Workflow example using Spring MVC

To run:
mvn tomcat7:run

Access the site:
http://localhost:9090/activiti-example/

Login in as any of the users - the password is the same name as the user (e.g. kermit/kermit).

Createa a document and submit for approval.

Then logout, and login and as another user in the same group as that which the document was created.

View your tasks and see that there is a new document waiting to be approved. Approve or deny. Log back in as the original author.
