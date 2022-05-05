# ProjectManSql
Project Management Application Built on Java and MySql
The program is used to keep track of the many projects a team or company work on.
I have used MySql to create a local database which stores all the project information in tables.

The following information is stored for every project:

● Project number.
● Project name.
● What type of building is being designed? E.g. House, apartment block or
store, etc.
● The physical address for the project.
● ERF number.
● The total fee being charged for the project.
● The total amount paid to date.
● Deadline for the project.
● The name, telephone number, email address and physical address of the
architect for the project.
● The name, telephone number, email address and physical address of the
contractor for the project.
● The name, telephone number, email address and physical address of the
customer for the project.

How it works:

● Capture information about new projects. If a project name is not provided when the information is captured, the project is named using the surname of 
the customer. For example, a house being built by Mike Tyson would be called “House Tyson” and an apartment block owned by Jared Goldman
would be called “Apartment Goldman”.
● Update information about existing projects. Information may need to be adjusted at different stages throughout the lifecycle of a project. 
For example, the deadline might change after a meeting with various stakeholders.

● Finalise existing projects. When a project is finalised, the following will happen:
○ An invoice will be generated for the client. This invoice will contain the customer’s contact details and the total amount that the customer must still pay. 
If the customer has already paid the full fee, an invoice will not be generated. The project will be marked as “finalised” and the completion date will be added.
● Users will also be able to see a list of projects that still need to be completed.
● See a list of projects that are past the due date.
● Find and select a project by entering either the project number 
