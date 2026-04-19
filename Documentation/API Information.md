# APIs
in this section you will find all the APIs that are available in the backend, along with their request and response formats.


## Employees APIs
| Method | Endpoint | Description | Request Body | Response Body |
|--------|----------|-------------|--------------|---------------|
| GET | /api/employees/todays-appointments | Get all appointments scheduled for the current day. | None | List of appointments with date, time, package, customer name, and customer email. Response codes: 200 OK or 400 Bad Request |
| GET | /api/employees/calendar | Get calendar view with marked dates when the Spa is closed. | None | Table data with marked holiday and maintenance dates. Response codes: 200 OK or 400 Bad Request |
|GET | /api/employees/free-dates | Get all free dates for the next 30 days. | None | List of free dates for the next 30 days. Response codes: 200 OK or 400 Bad Request |



## Admin APIs
| Method | Endpoint | Description | Request Body | Response Body |
|--------|----------|-------------|--------------|---------------|
| GET | /api/admin/manage-appointments | Get all appointments scheduled for the current day. | None | List of appointments with date, time, package, customer name, customer email and full price. Response codes: 200 OK or 400 Bad Request |
| POST | /api/admin/manage-packages | Add a new package. | Package details (name, description, price, discount percentage) | Confirmation message with the created package details. Response codes: 201 Created or 400 Bad Request |
| PUT | /api/admin/manage-packages/{id} | Edit an existing package. | | Updated package details (name, description, price, discount percentage) | Confirmation message with the updated package details. Response codes: 200 OK or 400 Bad Request |
| DELETE | /api/admin/manage-packages/{id} | Remove an existing package. | None | Confirmation message with the removed package details. Response codes: 200 OK or 400 Bad Request |
| GET | /api/admin/manage-employees | Get all employees. | None | List of employees. Response codes: 200 OK or 400 Bad Request |
| POST | /api/admin/manage-employees | Add a new employee. | Employee details (name, email) | Confirmation message with the created employee details. Response codes: 201 Created or 400 Bad Request |
| PUT | /api/admin/manage-employees/{id} | Edit an existing employee. | Updated employee details (name, email) | Confirmation message with the updated employee details. Response codes: 200 OK or 400 Bad Request |
| DELETE | /api/admin/manage-employees/{id} | Remove an existing employee. | None | Confirmation message with the removed employee details. Response codes: 200 OK or 400 Bad Request |
| GET | /api/admin/view-appointments | Get all appointments with full price details for the last 30 days. | None | List of appointments with date, time, package, customer name, customer email and full price. Response codes: 200 OK or 400 Bad Request |
| GET | /api/admin/free-dates | Get all free dates for the next 30 days. | None | List of free dates for the next 30 days. Response codes: 200 OK or 400 Bad Request |


## Customers APIs
| Method | Endpoint | Description | Request Body | Response Body |
|--------|----------|-------------|--------------|---------------|
| GET | /api/customers/booking-interface | Get the booking interface with available time slots and packages. | None | Booking interface data with available time slots and packages. Response codes: 200 OK or 400 Bad Request |
| POST | /api/customers/book-appointment | Book an appointment. | Appointment details (date, time, package, number of people, customer name, customer email) | Confirmation message with appointment details and support contact information. Response codes: 201 Created or 400 Bad Request |
| DELETE | /api/customers/cancel-appointment/{id} | Cancel an appointment. | None | Confirmation message with cancellation details or information on how to contact support for cancellation. Response codes: 201 Created or 400 Bad Request |


