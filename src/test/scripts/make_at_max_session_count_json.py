#!/usr/bin/env python3
import json
import random
import string
import re
from collections import Counter

random.seed(42)  # reproducible

# ---- Config ----
N = 1000  # total people (> S)
S = 250
OUTPUT = "../data/AddCommandTest/atMaxSessionCountAddressBook.json"

# Build exactly S unique sessions of the form <LETTER><1..99> (no leading zeros, max 2 digits).
PREFIXES = ["W", "X", "Y", "Z"]

def build_sessions():
    sessions = []
    for p in PREFIXES:
        for num in range(1, 100):  # 1..99
            sessions.append(f"{p}{num}")
            if len(sessions) == S:
                assert all(re.match(r"^[A-Z]([1-9][0-9]?)$", s) for s in sessions)
                return sessions
    raise RuntimeError(f"Not enough prefixes to reach {S} sessions.")

SESSIONS = build_sessions()

TYPES = ["student", "ta"]
TYPE_WEIGHTS = [0.85, 0.15]  # mostly students

FIRST_NAMES = [
    "Alice","Benson","Carl","Daniel","Elle","Fiona","George","Hana","Ivan","Jill",
    "Kai","Liam","Maya","Noah","Olive","Pavel","Quinn","Ravi","Sia","Tara",
    "Uma","Vera","Will","Xena","Yara","Zane","Aaron","Bella","Cody","Dina",
    "Evan","Faith","Glen","Hugo","Iris","Jon","Kira","Luca","Mina","Nate",
    "Owen","Pia","Ruth","Sara","Theo","Vince","Wren","Yuki","Zara"
]
LAST_NAMES = [
    "Pauline","Meier","Kurz","Meyer","Kunz","Best","Tan","Lim","Ng","Chong",
    "Yap","Lee","Wong","Teo","Goh","Chua","Sim","Toh","Ong","Chew",
    "Koh","Quek","Low","Foo","Pereira","Rahman","Singh","Das","Ibrahim","Putra",
    "Smith","Johnson","Brown","Davis","Miller","Garcia","Rodriguez","Wilson","Martinez","Anderson"
]

# Optional middle initials expand the unique name space safely
MIDDLE_INITIALS = [""] + [f"{c}" for c in string.ascii_uppercase]  # 27 options total

F_LEN = len(FIRST_NAMES)            # 50
L_LEN = len(LAST_NAMES)             # 40
M_LEN = len(MIDDLE_INITIALS)        # 27
TOTAL_NAME_SPACE = F_LEN * L_LEN * M_LEN  # 54,000

def unique_name_by_index(i_zero_based: int) -> str:
    """Deterministically map index -> unique 'First [M.] Last' name."""
    if i_zero_based >= TOTAL_NAME_SPACE:
        base = unique_name_by_index(i_zero_based % TOTAL_NAME_SPACE)
        return f"{base} {i_zero_based // TOTAL_NAME_SPACE}"  # fallback suffix if ever exceeded
    f = FIRST_NAMES[i_zero_based % F_LEN]
    l = LAST_NAMES[(i_zero_based // F_LEN) % L_LEN]
    m = MIDDLE_INITIALS[(i_zero_based // (F_LEN * L_LEN)) % M_LEN]
    return f"{f} {m + ' ' if m else ''}{l}"

# ---- Helpers ----
def unique_phone(used):
    # SG-style: 8 digits starting with 8 or 9
    while True:
        num = random.choice(["8","9"]) + "".join(random.choices(string.digits, k=7))
        if num not in used:
            used.add(num)
            return num

def slugify(s):
    return "".join(ch.lower() for ch in s if ch.isalnum())

def unique_email(name_for_base, idx, used):
    base = f"{slugify(name_for_base)}{idx}"
    email = f"{base}@example.com"
    bump = 1
    while email in used:
        email = f"{base}{bump}@example.com"
        bump += 1
    used.add(email)
    return email

def random_handle():
    # Telegram: '@' + 5–32 chars [A-Za-z0-9_], first after @ is a letter
    length = random.randint(5, 32)
    first_char = random.choice(string.ascii_letters)
    rest_pool = string.ascii_letters + string.digits + "_"
    rest = "".join(random.choices(rest_pool, k=length - 1))
    return "@" + first_char + rest

def maybe_telegram(user_type):
    p = 0.6 if user_type == "student" else 0.5
    return random_handle() if random.random() < p else None

def generate_person(i, session_id, used_phones, used_emails):
    name = unique_name_by_index(i - 1)  # ensure unique name
    user_type = random.choices(TYPES, weights=TYPE_WEIGHTS, k=1)[0]
    return {
        "name": name,
        "phone": unique_phone(used_phones),
        "email": unique_email(name, i, used_emails),
        "type": user_type,
        "telegramUsername": maybe_telegram(user_type),
        "session": session_id,  # always assigned, e.g., S9/S30/D20
    }

def main():
    assert N > len(SESSIONS) == S, "N must be > S and sessions must be exactly S."
    used_phones, used_emails = set(), set()
    persons = []

    # Ensure each session appears at least once
    for i, sess in enumerate(SESSIONS, start=1):
        persons.append(generate_person(i, sess, used_phones, used_emails))

    # Fill the rest with random sessions from the same S
    for i in range(len(SESSIONS) + 1, N + 1):
        persons.append(generate_person(i, random.choice(SESSIONS), used_phones, used_emails))

    # Sanity checks
    unique_sessions = {p["session"] for p in persons}
    names = [p["name"] for p in persons]
    assert len(unique_sessions) == S
    assert all(re.match(r"^[A-Z]([1-9][0-9]?)$", s) for s in unique_sessions)
    assert all(p["type"] in {"student","ta"} for p in persons)
    assert len(set(names)) == len(names), "Names must be unique."

    data = {"persons": persons}
    with open(OUTPUT, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
        f.write("\n")  # ensure newline at EOF

    counts = Counter(p["type"] for p in persons)
    print(f"Wrote {N} persons to {OUTPUT}")
    print("Type distribution:", dict(counts))
    print("Unique sessions:", len(unique_sessions))
    print("Unique names:", len(set(names)))

if __name__ == "__main__":
    main()
