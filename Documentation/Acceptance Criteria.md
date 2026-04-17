# Acceptance Criteria



## Acceptance Criteria: Employees Functionality

| ID  | Given| When| Then|
|-----|-------------|-------------|---------------|
| AC1 | Employee is logged in to the employee panel  | Employee opens "Today's appointments" | System shows all appointments scheduled for the current day.|
| AC2 | Today's appointments are displayed| Employee views an appointment row| System shows date, time, package, customer name, and customer email.|
| AC3 | Holiday and maintenance dates exist in system | Employee opens calendar/availability view  | System marks dates when the Spa is closed.|


## Acceptance Criteria: Customers Functionality 

| ID  | Given| When| Then|
|-----|-------------|-------------|---------------|
| AC4 | Customer is on the landing page  | Customer clicks "Book an appointment" | System shows the booking interface with available time slots and packages.|
| AC5 | Customer is booking an appointment  | Customer selects a date, time, and package, then confirms the booking | System processes the booking and sends a confirmation email to the customer with appointment details and support contact information.|
| AC6 | Customer has booked an appointment  | Customer tries to cancel the appointment within 20 seconds of booking | System allows the customer to cancel the appointment and sends a cancellation confirmation email.|
| AC7 | Customer has booked an appointment  | Customer tries to cancel the appointment after 20 seconds of booking | System does not allow the customer to cancel the appointment and provides information on how to contact support for cancellation.|

## Acceptance Criteria: Admin Functionality
| ID  | Given| When| Then|
|-----|-------------|-------------|---------------|
| AC8 | Admin is logged in to the admin panel  | Admin opens "Manage Appointments" | System shows all appointments scheduled for the current day.|
| AC9 | Admin is logged in to the admin panel  | Admin opens "Manage Packages" | System shows all packages with their details and options to add, edit, or remove packages.|
| AC10 | Admin is logged in to the admin panel  | Admin opens "Manage Employees" | System shows all employees with their schedules and options to manage them.|
| AC11 | Admin is logged in to the admin panel  | Admin opens "View Appointments" | System shows all appointments with full price details for the day.|

