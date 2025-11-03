---
layout: page
title: User Guide
---

TAConnect is a **desktop app for NUS CS2040 teaching assistants to manage students, sessions, and communication**, optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, TAConnect can get your tutorial group management tasks done faster than traditional GUI apps.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Target Users

TAConnect is for NUS CS2040 Teaching Assistants managing tutorial/lab groups who prefer fast, keyboard‑driven workflows. It streamlines common TA tasks such as adding and finding contacts, filtering by session, and exporting class lists.

### How TAConnect Helps
- Filter by session quickly (e.g., `listsession F2`) to focus on a single group.
- Export the currently shown list to CSV for sharing or backups.
- Command history and concise errors keep you in flow on the keyboard.

## Quick start

1. This app supports Java `17` only. Ensure you have the precise JDK version installed and active:
   - Verify with: `java -version` (e.g., `openjdk version "17.0.x"`)
   - For Windows users, you can refer to the guide [here](https://se-education.org/guides/tutorials/javaInstallationWindows.html).
   - For Mac users, you can refer to the guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
   - For Linux users, you can refer to the guide [here](https://se-education.org/guides/tutorials/javaInstallationLinux.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2526S1-CS2103T-F15a-1/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your TAConnect contact list.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar TAConnect-v1.5.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)
   Key areas: the command box (type commands), result display (messages), contact list (filtered by commands), and the Export CSV button.

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n:John Doe p:98765432 e:johnd@example.com t:student u:@johndoe s:F2` : Adds a student contact named `John Doe` to the contact list.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n:NAME`, `NAME` is a parameter which can be used as `add n:John Doe`.

* Items in square brackets are optional.<br>
  e.g `n:NAME [u:TELEGRAM_USERNAME]` can be used as `n:John Doe u:@JohnDoe` or as `n:John Doe`.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n:NAME p:PHONE_NUMBER`, `p:PHONE_NUMBER n:NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

### Field Constraints

- Name: Maximum of up to 500 characters. Allows ASCII letters only (a–z, A–Z; no digits), spaces, apostrophes (' U+0027, ‘ U+2018, ’ U+2019, ʼ U+02BC), hyphens (-), periods (.), and slashes (/). Examples: O'Connor, D’Angelo, Jean-Luc, J. P. Morgan, Rajesh S/O Raman.
- Phone: Singapore numbers only — exactly 8 digits (0–9). No spaces, symbols, or country codes in this field.
- Email: Must be of the form local-part@domain and adhere to:
  - one and only one '@', no spaces
  - total length ≤ 254; local-part ≤ 64; each domain label ≤ 63
  - local-part uses letters/digits with [._+-] as separators; cannot start/end with a separator; no consecutive dots
  - domain labels separated by '.', each starts/ends alphanumeric; hyphens allowed inside; final label (Top Level Domain, TLD) ≥ 2 characters
  - the domain must include at least one '.' (e.g., example.com); single-label domains like "ro" or "localhost" are not accepted
  - domain is case-insensitive; stored in lowercase
- Type and Session: Type must be one of the four inputs, **case-sensitive**: `student`, `ta`, `instructor`, and `staff`
  - `student` and `ta` must have a session, while `instructor` and `staff` should not have any session
- Session: Must match `[A-Z](?:[1-9][0-9]?)` — one uppercase letter followed by 1–2 digits from 1 to 99 (no leading zeros).
  - Examples: `F1`, `G2`, `T10`. Invalid: `f1` (lowercase), `G01` (leading zero), `AA1` (two letters).
- Telegram Username: Optional field, must adhere to:
  - 5 to 32 characters long
  - accepted characters: a-z, A-Z, 0-9 and underscores
  - optionally, include @ as the first character
  - for more details, see the following: https://core.telegram.org/method/account.updateUsername#parameters

### Data limits

- Maximum contacts: 2500
- Maximum unique sessions: 250
- When either limit is reached, `add` will fail with a clear error message.
- Importing a JSON data file that exceeds these limits will be rejected on startup.

### Duplicate contacts

- TAConnect allows only one contact per exact `NAME`. A duplicate is any new entry whose name matches an existing contact **with the same letter casing**.
- Differences in email, phone, Telegram username, session, or tags do not matter once the names match exactly; the command will be rejected as a duplicate.
- Names that differ only by letter casing (e.g., `alice tan` vs `Alice Tan`) are treated as different contacts.

### Viewing help : `help`

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a person: `add`

Adds a contact to the contact list of TAConnect.

Format: `add n:NAME p:PHONE_NUMBER e:EMAIL t:TYPE [u:TELEGRAM_USERNAME] [s:SESSION]`

Notes:
* `s:SESSION` must be provided when the Type is `student` or `ta`.
* `s:SESSION` must be omitted when the Type is `instructor` or `staff`.
* Trying to add a contact whose name exactly matches an existing one (same letter casing) will be rejected as a duplicate, even if the other fields differ.

For convenience, a TA can also record telegram username, but it is an optional field.

Examples:
* Add contact type `student`: `add n:John Doe p:98765432 e:johnd@example.com t:student u:@johndoe s:F2` (session required)
![student.png](images/student.png)
* Add contact type `ta`: `add n:David Shen p:23456789 e:davidshen@example.com t:ta s:L3`
![ta.png](images/ta.png)
* Add contact type `instructor`: `add n:Betsy Crowe p:34560781 e:betsycrowe@example.com t:instructor` (session omitted)
![instructor.png](images/instructor.png)
* Add contact type `staff`: `add n:Sophie Yuan p:17480572 e:sophie@example.come t:staff u:@yyssophie`
![staff.png](images/staff.png)

### Listing all contacts : `list`

Displays all contacts currently stored in TAConnect.

Format: `list`

* The list is reset to the full view, clearing any filters applied by previous commands such as `find` or `listsession`.
* Useful when you want to return to the complete contact list after filtering.

Examples:
* `list` — Displays all contacts in TAConnect.

### Listing contacts by session : `listsession`

Shows a list of all persons who belong to the specified session.

Format: `listsession SESSION`

* Displays only the contacts whose session field matches the given `SESSION` value.
* Sessions are case-sensitive and must match `[A-Z](?:[1-9][0-9]?)` (e.g., `F1`, `G2`, `T10`): one uppercase letter followed by 1–2 digits from 1 to 99, with no leading zeros.
* Contacts without a session (e.g., instructors or staff) will not appear in the result.
* Useful for CS2040 TAs who manage multiple tutorial or lab groups.

Examples:
* `listsession F20` — Lists all contacts in session F20.
* `listsession G1` — Lists all contacts in session G1.

### Listing all sessions : `sessions`

Shows all unique sessions currently recorded in TAConnect.

Format: `sessions`

* Displays the number of sessions and a list of the session codes in the result display.
* Useful for getting an overview of all existing tutorial/lab groups.

Examples:
* `sessions` — Lists all sessions, e.g., `12 sessions found in TAConnect. Here is the list: [F1, F2, G3, ...]`.


### Exporting the displayed contacts

Exports the contacts currently shown in the list to a CSV file containing `Name`, `Telegram`, `Email`, `Type`, and `Session`.

* Run `export` or click the `Export CSV` button located beside the command box.
* TAConnect saves the file as `exports/contacts-YYYYMMDD-HHmmss.csv`, using the timestamp of when you trigger the export.
* Only the contacts currently listed are exported. Combine with commands such as `find` to export a filtered subset before clicking the button.
* The result display shows the location of the generated file once the export completes, e.g., `Exported 12 contact(s) to /path/to/exports/contacts-20241027-153120.csv`.

Why export:
- Share a filtered list with instructors or teammates.
- Upload to spreadsheets/LMS, or keep quick backups.

### Locating persons by name: `find`

Finds persons whose names contain the given substring (case-insensitive).

Format: `find KEYWORD`

* Only the name is searched.
* The search is case-insensitive. e.g `hans` will match `Hans`.
* Partial matches are allowed. e.g. `Han` will match `Hans`.
* Whitespace inside `KEYWORD` is preserved; `find alex david` searches for the exact substring `"alex david"` (including the space).
* Allowed characters in `KEYWORD`: ASCII letters only (a–z, A–Z; no digits), spaces, apostrophes (' U+0027, ‘ U+2018, ’ U+2019, ʼ U+02BC), hyphens (-), periods (.), and slashes (/). Examples: `find O’Connor`, `find Jean-Luc`, `find Rajesh S/O`.

Examples:
* `find John` returns `john` and `John Doe`
* `find alex` returns `Alex Yeoh`

### Deleting a person : `delete`
Aliases: `del`, `rm`

Deletes the specified person from the contact list.

Format: `delete INDEX [MORE_INDEXES|RANGE] [n:NAME] [n:MORE_NAMES]`

* Deletes each person at the specified `INDEX` values, any indices in a `RANGE` of the form `A-B` (inclusive),
  or with the exact `NAME` provided.
* The indexes refer to the numbers shown in the displayed person list.
* Every index **must be a positive integer** 1, 2, 3, …​
* Names are case-sensitive and must match the contact name exactly, including spaces.

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the contact list.
* `list` followed by `delete 1 3` deletes both the 1st and 3rd persons in the currently displayed list.
* `delete 2-5` deletes the 2nd to 5th persons shown in the current list (inclusive).
* `delete 1 3-4` deletes the 1st, 3rd, and 4th persons.
* `delete n:Alice Tan` deletes the contact whose name is exactly `Alice Tan`.
* `list` followed by `delete 1 n:Alice Tan` deletes the 1st person in the list and the contact named `Alice Tan`.
* `find Betsy` followed by `delete 1` deletes the 1st person in the results of the `find` command.
* Aliases: `del 2`, `rm n:Alice Tan` behave the same as `delete`.

### Undo last change : `undo`

Undoes the most recent command that modified the contact list (e.g., `add`, `delete`, `clear`). History is kept only for the current session; closing the app clears the undo stack.

Format: `undo`

Examples:
* `add n:John Doe ...` followed by `undo` removes the newly added contact and shows `Undo successful (reverted: add)`.
* `delete 1 3-4` followed by `undo` restores the contacts removed by that delete and reports the exact command alias that was reverted (e.g., `delete`, `del`).

### Command history (↑/↓)

You can navigate your previously entered commands using the **UP** and **DOWN** arrow keys, similar to a terminal.

## Common Errors and Fixes

- “Keyword to find is invalid! Use only letters (a–z, A–Z), spaces, apostrophes (' ‘ ’ ʼ), hyphens (-), periods (.), or slashes (/).”
  - Cause: `KEYWORD` contains disallowed characters (e.g., `@`, backtick `\``).
  - Fix: Use only the same characters allowed in names.

- “Emails must be of the form local-part@domain … domain contains at least one '.' … final label (TLD) ≥ 2 chars”
  - Cause: Domain missing a dot (e.g., `rob@ro`) or TLD too short (e.g., `exeee.m`).
  - Fix: Use domains like `example.com`, `u.nus.edu`, or TLDs with ≥ 2 characters, e.g., `exeee.me`.

- “Phone number must be a Singapore number with exactly 8 digits (0-9).”
  - Cause: Too few/many digits, or non-digit characters/spaces.
  - Fix: Provide exactly 8 digits with no spaces or symbols.

## Glossary
- TA: Teaching Assistant.
- Session: Group code for a tutorial/lab (e.g., `F1`, `G2`, `T10`).
- Telegram username: Optional contact handle (5–32 chars; letters, digits, underscores; may start with `@`).
- TLD (Top‑Level Domain): The last label in a domain (e.g., `.com`, `.sg`, `.edu`). Must be ≥ 2 characters in this app.

* **UP:** Shows the previous command in history.
* **DOWN:** Shows the next command in history; if you reach the end, your current unfinished input (buffer) is restored.
* **Duplicates & empty lines:** Empty inputs are not saved. Consecutive identical commands are stored only once.
* **Edits while browsing history:** If you edit the text at any time, those edits are preserved when you return to the end (buffer).

Examples:
1. Type `list` → press Enter. Press ↑ → `list` appears.
2. Type `find alex` → press Enter. Press ↑ twice → `find alex`, then `list`.
3. Press ↑ to view `list`, type `l` to modify it, then press ↓ until the end → your unfinished text appears again (e.g., `l`).

### Clearing all entries : `clear`

Clears all entries from the contact list of TAConnect.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

TAConnect data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

TAConnect data are saved automatically as a JSON file `[JAR file location]/data/taconnect.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, TAConnect will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the TAConnect to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous TAConnect home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add student** | `add n:NAME p:PHONE_NUMBER e:EMAIL t:student [u:TELEGRAM_USERNAME] s:SESSION` <br> e.g., `add n:John Doe p:98765432 e:johnd@example.com t:student u:@johndoe s:F2`
**Add ta** | `add n:NAME p:PHONE_NUMBER e:EMAIL t:ta [u:TELEGRAM_USERNAME] s:SESSION` <br> e.g., `add n:David Shen p:23456789 e:davidshen@example.com t:ta s:L3`
**Add instructor** | `add n:NAME p:PHONE_NUMBER e:EMAIL t:instructor [u:TELEGRAM_USERNAME]` <br> e.g., `add n:Betsy Crowe p:34560781 e:betsycrowe@example.com t:instructor`
**Add staff** | `add n:NAME p:PHONE_NUMBER e:EMAIL t:staff [u:TELEGRAM_USERNAME]` <br> e.g., `add n:Sophie Yuan p:17480572 e:sophie@example.come t:staff u:@yyssophie`
**Clear** | `clear`
**Delete** | `delete|del|rm INDEX [MORE_INDEXES] [n:NAME] [n:MORE_NAMES]`<br> e.g., `delete 3`, `del 1 4`, `rm n:Alice Tan`
**Find** | `find KEYWORD`<br> e.g., `find James Jake`
**List** | `list`
**List session** | `listsession SESSION` <br> e.g., `listsession F20`
**Sessions** | `sessions`
**Undo** | `undo`
**Export CSV** | `export` or click the `Export CSV` button (saves to `exports/contacts-YYYYMMDD-HHmmss.csv`)
**Help** | `help`
**Exit** | `exit`
