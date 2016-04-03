activiti-example
================

Activiti Workflow example using _Spring MVC_
__________________________________________

To run you need Maven:

```bash
mvn tomcat7:run
```

Access the site:
[http://127.0.0.1:9090/activiti-example/]([http://127.0.0.1:9090/activiti-example/])

* Login in as any of the users - the password is the same name as the user (e.g. kermit/kermit).

* Create a document and submit for approval. Take notice for which group (engineering, sales, management, etc.) the document was created.

* Then logout and login as another user in the same group. You can
view the list of users by clicking the 'users' button on the bottom right of the form.

* View your tasks and see that there is a new document waiting to be approved. Approve or deny. Log back in as the original author.

* Next modify the workflow for a group by adding additonal approval steps, using different users and/or groups if desired. Create new document(s) under the modified group(s).
