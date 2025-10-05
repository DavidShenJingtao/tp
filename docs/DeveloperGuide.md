---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* NUS tutors teaching CS mods who want to deal with the contacts of students, tutors and course instructors in a specific course
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: proposes an efficient way for TAs to add, modify and access _contact details_ of student, tutors, instructors for a specific course, which makes it more convenient to help students with learning, connect with other TAs and reach out to staff in case of unexpected situations.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| ----- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* *` | tutor                                      | see usage instructions         | refer to instructions when I forget how to use the App                 |
| `* * *` | tutor                                      | add new _contacts_ | keep the contact list updated with _contact details_ and _session_ |
| `* * *` | tutor                                      | delete _contacts_ by _contact ID_ | remove _contacts_ from the contact list in case they have  |                                
| `* * *` | tutor                                      | search contact list by _name_ | locate details of _contacts_ by name without having to go through the entire list |
| `* *` | tutor                                      | search contact list by _contact ID_ | locate details of _contacts_ by _contact ID_ without having to go through the entire list |
| `* *` | tutor                                      | list all _contacts_ from the course | view all _contacts_ and their _contact details_ and _session_ in the contact list |
| `* * *` | tutor                                      | list all _contacts_ by _session_ | view all _contacts_ and their _contact details_ in particular session in the contact list |

*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `TAConnect` program and the **Actor** is the `tutor`, unless specified otherwise)

**Use case: UC1 - Add a new contact in the contact list**

**MSS**
1.  Tutor enters `add command` including details of a contact.
2.  TAConnect parses the command input.
3.  TAConnect validates that the command is correctly formatted and all required fields are correctly updated.
4.  TAConnect adds the new contact in the contact list.
5.  TAConnect saves the updated contact list to the local data file.
6.  TAConnect displays a success message showing details of the new contact.

    Use case ends.

**Extensions**

3a. TAConnect detects an error in the command (invalid type or incorrect format).
  * 3a1. TAConnect shows an error message specifying the issue and correct format.
  * 3a2. Tutor re-enters the command.

    Steps 3a1-3a2 are repeated until the type entered is correct.

    Use case resumes from step 4.

4a. TAConnect finds that the added contact already exists in the contact list.
  * 4a1. TAConnect rejects the duplicate entry.
  * 4a2. TAConnect shows an error message indicating that the added contact already exists.
    
    Use case ends.

5a. Storage operation fails due to I/O error.
  * 5a1. TAConnect displays an error message indicating that data could not be saved.

    Use case ends.

5b. Storage file is corrupted.
  * 5b1. TAConnect shows an error message indicating that the data file is corrupted.
  * 5b2. TAConnect attempts to back up or recreate the storage file.
  * Use case resumes from step 5 if recovery succeeds; otherwise, use case ends.

**Use case: UC2 - Delete a contact in the contact list**

**MSS**
1.  Tutor enters `list` to view the current contacts and their indexes.
2.  TAConnect shows the list of contacts with index numbers.
3.  Tutor enters `delete INDEX` to remove the intended contact.
4.  TAConnect validates that the `INDEX` refers to a contact in the displayed list.
5.  TAConnect removes the contact from the contact list, saves the updated data, and confirms the deletion.

    Use case ends.

**Extensions**

3a. Tutor enters an `INDEX` that is not a positive integer.
  * 3a1. TAConnect shows an error message describing the valid index format.
  * 3a2. Tutor re-enters the command with a valid `INDEX`.

    Use case resumes from step 3.

4a. The specified `INDEX` does not correspond to any contact currently displayed.
  * 4a1. TAConnect informs the tutor that the index is invalid.

    Use case resumes from step 3.

5a. Storage operation fails due to an I/O error.
  * 5a1. TAConnect displays an error message indicating that the data could not be saved.

    Use case ends.

**Use case: UC3 - Search contacts in the list by name**

**MSS**
1.  Tutor enters `find KEYWORD` to locate a contact.
2.  TAConnect parses the command and checks that at least one keyword is provided.
3.  TAConnect filters the contact list to contacts whose names contain the keyword(s).
4.  TAConnect displays the filtered list to the tutor.
5.  Tutor uses the displayed contact details to reach out to the intended person.

    Use case ends.

**Extensions**

2a. Tutor omits the keyword or enters only whitespace.
  * 2a1. TAConnect shows an error message indicating that at least one keyword is required.

    Use case resumes from step 1.

3a. No contact matches the supplied keyword(s).
  * 3a1. TAConnect shows a message indicating that no contacts were found.

    Use case ends.

5a. Tutor wishes to refine the search.
  * 5a1. Tutor enters another `find` command with different keyword(s).

    Use case resumes from step 1.

**Use case: UC4 - List all contacts in the course**

**MSS**

1.  Tutor requests to list all users as well as their _contact types_ and _session_ for the particular course
2.  TAConnect shows a list of all users as well as their _contact types_ and _session_ for the particular course

    Use case ends.

**Extensions**

2a. The list is empty.

  Use case ends.

**Use case: UC5 - List all student contacts in a specific session**

**MSS**

1. Tutor enters `list s/SESSION` to list student contacts for a specific session.
2. TAConnect parses the command input and validates that the session identifier is present and correctly formatted.
3. TAConnect filters the contact list to entries with tag `student` that match the specified session.
4. TAConnect displays the filtered list of student contacts for the specified session.

    Use case ends.

**Extensions**

2a. TAConnect detects an invalid command syntax or missing/incorrect session format.
  * 2a1. TAConnect shows an error message specifying the issue and the correct format.
  * 2a2. Tutor re-enters the command.

    Steps 2a1-2a2 are repeated until the data entered are correct.

    Use case resumes from step 3.

3a. No matching student contacts exist for the specified session.
  * 3a1. TAConnect shows a message indicating no student contacts were found for that session.

    Use case ends.

4a. Storage operation fails due to a data retrieval or I/O error.
  * 4a1. TAConnect displays an error message indicating that data could not be accessed.

    Use case ends.

### Non-Functional Requirements

**Performance requirements**
1. Should execute core commands (i.e. `add`, `delete`, `find`) within 1 second under usual conditions.
2. Should be able to handle up to 2500 users and 250 sessions without a noticeable sluggishness in performance for typical usage.
3. Should automatically save after each successful modification command (i.e. `add`, `delete`) without affecting UI responsiveness.

**Usability requirements**
1. A tutor with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
2. A new user should be able to learn and perform basic commands within 10 minutes under the help of user guide.
3. The user interface should provide consistent layout and feedback messages across all _mainstream OSes_.

**Scalability requirements**
1. The internal data structures (contact list) should efficiently support search and retrieval operations in O(n) time complexity.
2. Should allow easy addition of new commands without modifying existing core logic.

**Other requirements**
1. Should work on any _mainstream OS_ as long as it has Java `17` installed.
2. All unit and integration tests should pass before release, maintaining at least 90% test coverage.

### Glossary

* **Contact ID**: For students or tutors who are not full-time employees of NUS this is their matriculation number (eg. A01234567X). For tutors or instructors who are full-time employees of NUS, the TAConnect program will assign a contact ID with the format `FTE-{INITIALS}`, so a NUS full-time employee that has a name Betsy Crowe will have the contact ID `FTE-BC`. If multiple contacts have the same initials we will append a number, representing the number of times this initial has been used, in front, so if the contact ID `FTE-BC` already exists and another NUS full-time employee that has a name Bob Charlie is added, the contact ID will be `FTE-BC2`.
* **Contact**: The user's name, _contact ID_, email and optionally a Telegram handle.
* **Contact Type**: The category of a contact, i.e. student, tutor, course instructor, staff
* **Mainstream OS**: Windows, Linux, Unix, Mac
* **Session**: A period of lab or tutorial during which tutor is responsible for delivering the class.
* **Tutor**: Teaching assistant in a NUS CS-coded course
* **UI (User interface)**: The visual and interactive components of TAConnect through which users issue commands and receive responses.

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
