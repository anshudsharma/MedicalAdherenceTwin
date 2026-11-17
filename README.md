# Medicine Adherence Twin

Medicine Adherence Twin is a simple, beginner-friendly Android application designed to help users track their daily medication schedules, log when they take or miss doses, and view a calculated adherence percentage. 

This app is built natively using **Java** and **XML** with no reliance on external cloud databases, making it a perfect example of local storage architecture for Mobile Development Lab projects.

---

## 📱 Features

- **Offline Authentication**: User registration and login utilizing local `SharedPreferences`.
- **Dashboard Summary**: Immediate visual summary of the number of tracked medicines and today's adherence rate.
- **Medicine Management**: Full CRUD (Create, Read, Update, Delete) capability for medications.
- **Local Database**: All medicines and dosage logs are securely stored entirely offline using a native `SQLiteOpenHelper`.
- **Dose Tracking**: Dedicated interface to instantly log scheduled doses as "Taken" or "Missed". Safely handles accidental duplicates via SQLite `UPDATE` handling.
- **Adherence Reporting**: Dynamically calculates and displays adherence percentages based on historical dose statuses.
- **Timely Reminders**: Utilizes the native Android `AlarmManager` and `BroadcastReceiver` to spawn system notifications when a medicine is scheduled to be taken.

---

## 🏗️ Project Architecture & Modules

The application is structured into the following modules based on standard Android Activity lifecycles:

### 1. Authentication Module
- `SplashActivity.java`: Pauses for 2 seconds and evaluates `SessionManager.isLoggedIn()` to route the user gracefully to the Dashboard or Login screen.
- `RegisterActivity.java` & `LoginActivity.java`: Authenticates user locally against `SharedPreferences`.

### 2. Database Module (`DatabaseHelper.java`)
Manages the `MedicineAdherence.db` SQLite database using a 2-table schema.
- **`medicines` Table**: 
  - `id` (INTEGER, PK)
  - `name` (TEXT)
  - `dosage` (TEXT)
  - `start_date` (TEXT)
  - `time` (TEXT)
  - `frequency` (INTEGER)
  - `notes` (TEXT)
- **`doses` Table**: 
  - `id` (INTEGER, PK)
  - `medicine_id` (INTEGER, FK referencing `medicines(id)` ON DELETE CASCADE)
  - `dose_date` (TEXT)
  - `status` (TEXT) -> "Taken" or "Missed"

### 3. Core App Module
- **`MainActivity.java`**: The main dashboard mapping explicit Intents to all other sub-activities.
- **`AddMedicineActivity.java` & `EditMedicineActivity.java`**: Handlers for inserting and modifying medicines. Implements `DatePickerDialog` and `TimePickerDialog` for error-free date/time entry. 
- **`MedicineListActivity.java`**: Parses the `DatabaseHelper` outputs into a visual scrollable `RecyclerView`.
- **`DoseTrackingActivity.java`**: Provides the interface for marking doses.
- **`AdherenceReportActivity.java`**: Calculates `(taken doses / total doses) * 100` natively avoiding zero-division crashes.
- **`AlarmHelper.java` & `ReminderReceiver.java`**: Schedules exact alarms utilizing Android 13+ permission standards and spawns `NotificationCompat.Builder` notifications.

---

## 🔄 Screen Navigation Flow

Navigation is handled entirely by explicit Intents mapping between standard Activities:

1. **App Launch** ➡️ `SplashActivity`
2. **Not Logged In** ➡️ `LoginActivity` ↔️ `RegisterActivity`
3. **Logged In** ➡️ `MainActivity` (Dashboard)
   - Dashboard ➡️ **Add Medicine** (`AddMedicineActivity`)
   - Dashboard ➡️ **View Medicines** (`MedicineListActivity`) ➡️ **Edit Medicine** (`EditMedicineActivity`)
   - Dashboard ➡️ **Track Doses** (`DoseTrackingActivity`)
   - Dashboard ➡️ **Adherence Report** (`AdherenceReportActivity`)
   - Dashboard ➡️ **Logout** (Returns to `LoginActivity`)

---

## ✅ Testing Checklist

Before deployment or project submission, verify the following:

- [ ] **Registration:** Registering with blank fields or mismatched passwords triggers an error Toast.
- [ ] **Login:** App persists the session. Closing and reopening the app bypasses the login screen.
- [ ] **Add Medicine:** `DatePicker` and `TimePicker` successfully open and format string data correctly.
- [ ] **SQLite Interaction:** Adding a medicine makes it immediately visible in `MedicineListActivity`.
- [ ] **Notifications:** Saving a medicine triggers a notification at the exact chosen time (ensure Android 13+ Notification permissions are granted).
- [ ] **Cascade Deletion:** Deleting a medicine via the `AlertDialog` permanently drops it from the list and nullifies its scheduled `AlarmManager` intents.
- [ ] **Tracking:** Marking a dose as Taken/Missed accurately reflects in the `AdherenceReportActivity`.
- [ ] **Logout:** Logs the user out and properly destroys the active dashboard session.

---

## 📦 How to Generate a Signed APK (For Submission)

To export the final application for your mobile phone or university submission:

1. Open this project in **Android Studio**.
2. Navigate to the top menu and click **Build** > **Generate Signed Bundle / APK...**
3. Select **APK** and click **Next**.
4. Under **Key store path**, click **Create new...**
    - Choose a destination folder (e.g., your Desktop).
    - Give it a filename (e.g., `twin_key.jks`).
    - Create a strong password.
    - Fill in at least one "Certificate" field (First and Last Name) and click **OK**.
5. With the new keystore selected, click **Next**.
6. Choose the **release** build variant and click **Create**.
7. Once Gradle completes, click the **locate** link on the popup notification in the bottom right corner to find your final `app-release.apk` file.
