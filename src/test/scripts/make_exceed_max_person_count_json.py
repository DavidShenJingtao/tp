#!/usr/bin/env python3
import json
import random
import string
from collections import Counter

random.seed(42)  # reproducible

N = 2501
OUTPUT = "../data/JsonSerializableAddressBookTest/exceedMaxPersonCountAddressBook.json"

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

# Optional middle initials: "" = none, or "A.", "B.", ...
MIDDLE_INITIALS = [""] + [f"{c}" for c in string.ascii_uppercase]

# Sessions pool (limit to withing max sessions count)
SESSIONS = (
    [f"S{i}" for i in range(1, 21)]
)

TYPES = ["student", "ta", "instructor", "staff"]
TYPE_WEIGHTS = [0.7, 0.1, 0.1, 0.1]  # mostly students

# ---- Unique name generator by index (deterministic, collision-free) ----
F_LEN = len(FIRST_NAMES)
L_LEN = len(LAST_NAMES)
M_LEN = len(MIDDLE_INITIALS)
TOTAL_NAME_SPACE = F_LEN * L_LEN * M_LEN  # 50 * 40 * 27 = 54,000

def unique_name_by_index(i_zero_based: int) -> str:
    """Map an index to a unique (First [Middle.] Last) name."""
    if i_zero_based >= TOTAL_NAME_SPACE:
        # If we somehow exceed name space, suffix with a counter to stay unique.
        base = unique_name_by_index(i_zero_based % TOTAL_NAME_SPACE)
        return f"{base} {i_zero_based // TOTAL_NAME_SPACE}"
    f = FIRST_NAMES[i_zero_based % F_LEN]
    l = LAST_NAMES[(i_zero_based // F_LEN) % L_LEN]
    m = MIDDLE_INITIALS[(i_zero_based // (F_LEN * L_LEN)) % M_LEN]
    return f"{f} {m + ' ' if m else ''}{l}"

def unique_phone(used):
    # Singapore-style 8-digit mobiles typically start with 8 or 9
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
    # Telegram handle rule: starts with '@' + 5–32 chars [A-Za-z0-9_]
    length = random.randint(5, 32)
    first_char = random.choice(string.ascii_letters)
    rest_pool = string.ascii_letters + string.digits + "_"
    rest = "".join(random.choices(rest_pool, k=length - 1))
    return "@" + first_char + rest

def maybe_telegram(user_type):
    # higher chance for students to have a handle
    has = random.random() < (0.6 if user_type == "student" else 0.3)
    return random_handle() if has else None

def maybe_session(user_type):
    # students and TAs always get sessions, others none
    if user_type in ("student", "ta"):
        return random.choice(SESSIONS)
    return None

def generate_person(i, used_phones, used_emails):
    user_type = random.choices(TYPES, weights=TYPE_WEIGHTS, k=1)[0]
    name = unique_name_by_index(i - 1)  # ensure unique name per index
    return {
        "name": name,
        "phone": unique_phone(used_phones),
        "email": unique_email(name, i, used_emails),
        "type": user_type,
        "telegramUsername": maybe_telegram(user_type),
        "session": maybe_session(user_type),
    }

def main():
    used_phones = set()
    used_emails = set()
    persons = [generate_person(i+1, used_phones, used_emails) for i in range(N)]
    data = {"persons": persons}
    with open(OUTPUT, "w", encoding="utf-8") as f:
        json.dump(data, f, indent=2, ensure_ascii=False)
        f.write("\n")  # ensure newline at EOF

    # sanity checks
    counts = Counter(p["type"] for p in persons)
    unique_names = len({p["name"] for p in persons})
    print(f"Wrote {N} persons to {OUTPUT}")
    print("Type distribution:", dict(counts))
    print("Unique names:", unique_names, "(should equal N)")

if __name__ == "__main__":
    main()
