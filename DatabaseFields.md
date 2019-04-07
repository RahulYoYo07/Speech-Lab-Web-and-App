# CS243-Speech-Lab-Web-and-App
## List of Database Tables

### 1.Users
* Username - Text (Outlook Email,remove .iitg.ac.in)
* Password- Text (Hashed)
* First Username
* Last Name
* Designation- Faculty, Student, Admin
* Roll Number
* Profile Picture
* Write Up (About Me)
* Phone Number (Contact)
* Room No.
* College Designation
* Department
* URL (Option to add more)
     * Homepage
     * Github
     * LinkedIn
* Program- Students only

### 2. Projects table
* Unique Key
* Project Title
* Media
     * Caption
     * Media file (gif,video or image-> Embedded in webpage)
* Course ID
* Writeup (Abstract)
* Contributors
     * Project guide
     * Collaborators
* Add More fields (Rendered Dynamically)

### 3. FAQ table
* Question
* Answer
* Tags

### 4.Courses Table
* Course ID- Number
* Course Name - Text
* Faculty ID - List of References
* Student ID - List of References
* TA ID - List of References
* Enrollment Key - Text
* TA permissions
     * Notice Board
     * Course material
     * Attendance
     * Assignment + Submission + deadline
     * Grading
     * Planner Editing
* Planner- API implementation
* Abstract
* References
* Start Date- End Date
* Attendance
* Assignment ID

### 5. Assignment Table
* ID
* Name
* Abstract
* Resources (List of files)
* List of Group ID's
* Deadlines

### 6.Groups table (Make automatically when course is added)
* ID(Group ID)
* Student ID
* Submission File
* Grading

### 7. Discussion Table
* Group ID References
* Message ID References
* Message Body
* Author
* Timestamp
* Unique ID
* isPoll

### 8. Notice Table
* Course ID References
* Notice ID 
* Heading+ Message
* PDF(optional)
* Author
* Timestamp
