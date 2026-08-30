# Movie Ticket Booking Management Application — CineWave Entertainment

A full-stack web app implementing the same booking lifecycle as the NIP/Pega spec
(Booking Request → Availability → Approval → Booking Execution → Resolved),
built with **Spring Boot + MySQL** on the backend and **React (Vite)** on the frontend.

## Stack
- Backend: Java 17, Spring Boot 3.3, Spring Data JPA, MySQL
- Frontend: React 18 + Vite (plain fetch, no extra state libraries)

## User-story → code map
| Story | Where it lives |
|---|---|
| US-001 Submit request | `BookingController.submit` → `BookingService.submitRequest` |
| US-002 Check availability | `BookingService.submitRequest` (availability branch) |
| US-003 Calculate cost | `BookingService.submitRequest` (totalCost calc) |
| US-004 Confirm/Cancel | `BookingController.decide` → `BookingService.decide` |
| US-005 Movie/Show data | `Movie`, `Show` entities + `MovieController`, `ShowController` |
| US-006 Review details | `BookingSummary.jsx` |
| US-007 Process booking | `BookingService.decide` (seat allocation + ticket ID) |
| US-008 Notify confirmation | `NotificationService.sendBookingConfirmation` |
| US-009 SLA | `BookingRequest` SLA fields + `SlaMonitorService` (hourly sweep) |
| US-010 Route by show type | `BookingService.routeByShowType` |

## Run it locally

### 1. Database
```sql
CREATE DATABASE cinewave_booking;
```
Update `backend/src/main/resources/application.properties` with your MySQL
username/password if they differ from `root` / `root`.

### 2. Backend
```bash
cd backend
mvn spring-boot:run
```
Runs on `http://localhost:8080`. Tables are created automatically
(`spring.jpa.hibernate.ddl-auto=update`) and seeded with sample movies/shows
from `data.sql` on first run.

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```
Runs on `http://localhost:5173` and calls the backend at `localhost:8080`.

## Using the app
1. Browse movies and pick a show.
2. Fill in your name, email, and ticket count → the app checks seats and
   shows the total cost instantly.
3. Review and Confirm (or Cancel).
4. On Confirm, seats are allocated, a ticket ID is generated, the booking is
   routed to `PremiumShowQueue` or `StandardShowQueue`, and a confirmation
   "email" is logged on the backend console.
5. **Staff Admin** (top-right) lets you add movies and shows — the
   "staff manage show details and seating availability" objective from the
   scenario.

## Notes
- This sandbox can't reach Maven Central or run MySQL, so the backend
  couldn't be compiled/executed here — build and run it on your own machine,
  where Maven will resolve dependencies normally.
- The frontend was built and verified in this sandbox (`npm run build`
  succeeds cleanly).
- Email notification is simulated via a formatted log line
  (`NotificationService`) — swap in `JavaMailSender` if you want real emails.
