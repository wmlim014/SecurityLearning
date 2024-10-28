######################################
#   Name           : Lim Wen Mi      #
#   Submission for : Assignment 1    #
#   File name      : Connect.py      #
#   Date           : 28/10/2024      #
######################################

import sys
import time
import hashlib

PASSWORD_FILE = "Passwords.txt"

def save_user(username, password):
    """Saves username and password in Passwords.txt file."""
    with open(PASSWORD_FILE, "a") as file:
        file.write(f"{username},{password}\n")

def load_users():
    """Loads users from Passwords.txt into a dictionary."""
    users = {}
    try:
        with open(PASSWORD_FILE, "r") as file:
            for line in file:
                user, pwd = line.strip().split(',')
                users[user] = pwd
    except FileNotFoundError:
        pass
    return users

def generate_pin(username, password, interval):
    """Generates a 6-digit PIN based on username, password, and time interval."""
    key = f"{username}{password}{interval}".encode()
    hash_value = hashlib.sha256(key).hexdigest()
    pin = int(hash_value, 16) % 1000000
    return f"{pin:06}"

def verify_user(username, password, pin):
    """Verifies the username, password, and PIN."""
    users = load_users()
    if username in users and users[username] == password:
        current_interval = int(time.time() // 15)
        expected_pin = generate_pin(username, password, current_interval)
        return expected_pin == pin
    return False

def main():
    if len(sys.argv) < 3:
        print("Usage: Connect <username> <new> OR Connect <username> <password> <pin>")
        sys.exit(1)

    username = sys.argv[1]
    option = sys.argv[2]

    if option == "new":
        password = input("Enter a new password: ")
        confirm_password = input("Confirm password: ")
        if password == confirm_password:
            save_user(username, password)
            print("User registered successfully.")
        else:
            print("Passwords do not match.")
    else:
        password = option
        pin = sys.argv[3]
        if verify_user(username, password, pin):
            print("Authentication successful.")
        else:
            print("Authentication failed.")

if __name__ == "__main__":
    main()
